package com.nxiao.service.core;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.nxiao.service.core.exception.ServiceProcessException;

public class TaskProcessor extends Thread implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	Queue<ITask> taskQueue;
	Map<TaskType, ITaskProcessor> processors;
	
	boolean startedUpFlag = false;

	public TaskProcessor(Map<TaskType, ITaskProcessor> processors)
	{
		taskQueue = new ConcurrentLinkedQueue<ITask>();
		this.processors = processors;
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
			throw new ServiceProcessException("Task type [" + task.getTaskType() + "] doesn't have corresponding processor registered.");
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
			while (!taskQueue.isEmpty())
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
