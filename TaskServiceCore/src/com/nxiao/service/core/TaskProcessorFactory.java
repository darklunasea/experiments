package com.nxiao.service.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;
import com.nxiao.service.processor.ITaskProcessor;

public class TaskProcessorFactory extends Thread
{
	private Logger logger = Logger.getLogger(this.getClass());

	Queue<ITask> taskQueue;
	Map<TaskType, ITaskProcessor> processors;
	
	boolean startedUpFlag = false;

	public TaskProcessorFactory(Map<TaskType, ITaskProcessor> originalProcessors) throws ServiceStartUpException
	{
		taskQueue = new ConcurrentLinkedQueue<ITask>();
		this.processors = new HashMap<TaskType, ITaskProcessor>();
		for(Map.Entry<TaskType, ITaskProcessor> originalProcessor : originalProcessors.entrySet())
		{
			processors.put(originalProcessor.getKey(), originalProcessor.getValue().newSession());
		}
	}
	
	public boolean startedUp()
	{
		return startedUpFlag;
	}

	public void addTask(ITask task)
	{
		taskQueue.add(task);
	}

	public void processTask(ITask task) throws ServiceProcessException
	{
		ITaskProcessor processor = processors.get(task.getTaskType());
		if (processor == null)
		{
			throw new ServiceProcessException("Task type [" + task.getTaskType()
					+ "] doesn't have corresponding processor registered.");
		}
		processor.processTask(task);
	}

	@Override
	public void run()
	{
		logger.info("Task Processor start running.");
		startedUpFlag = true;
		
		while (!Thread.currentThread().isInterrupted())
		{
			if(taskQueue.isEmpty())
			{
				try
				{
					sleep(10);
				}
				catch (InterruptedException e)
				{
					logger.error("Error waiting for new task. Reason: " + e.getMessage(), e);;
				}
			}
			else
			{
				try
				{
					// get the next task
					ITask task = taskQueue.poll();
					// process task
					processTask(task);
				}
				catch (Exception e)
				{
					logger.error("Error processing task! Reason: " + e.getMessage(), e);
				}
			}
		}
	}
}
