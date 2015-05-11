package com.nxiao.service.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.publisher.IPublisher;
import com.nxiao.service.core.query.QueryTaskProcessor;

public class TaskProcessor extends Thread implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	Queue<ITask> taskQueue;
	Map<TaskType, ITaskProcessor> processorRegistration;
	
	boolean startedUpFlag = false;

	public TaskProcessor(DataCache dataCache, IPublisher publisher)
	{
		taskQueue = new ConcurrentLinkedQueue<ITask>();
		processorRegistration = new HashMap<TaskType, ITaskProcessor>();
		registerProcessors(dataCache, publisher);		
	}

	private void registerProcessors(DataCache dataCache, IPublisher publisher)
	{
		// add the Query handler
		processorRegistration.put(TaskType.QUERY, new QueryTaskProcessor(dataCache));
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
		ITaskProcessor processor = getProcessorByTaskType(task.getTaskType());
		if (processor == null)
		{
			throw new ServiceProcessException("Task type doesn't have corresponding processor: " + task.getTaskType());
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

	private ITaskProcessor getProcessorByTaskType(TaskType taskType)
	{
		return processorRegistration.get(taskType);
	}
}
