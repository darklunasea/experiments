package com.nxiao.service.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;
import com.nxiao.service.processor.ITaskProcessor;

public class TaskManager extends Thread
{
	private Logger logger = Logger.getLogger(this.getClass());

	Queue<ITask> taskQueue;
	Map<Integer, TaskProcessorFactory> processorBucketMap;
	int numOfBuckets;

	boolean startedUpFlag = false;

	public TaskManager(int numOfBuckets, Map<TaskType, ITaskProcessor> processors)
			throws ServiceStartUpException
	{
		logger.info("Initializing task manager...");

		taskQueue = new ConcurrentLinkedQueue<ITask>();

		// initialize processors
		if (numOfBuckets < 1)
		{
			throw new ServiceStartUpException("Invalid bucket number.");
		}
		this.numOfBuckets = numOfBuckets;
		initProcessorBuckets(numOfBuckets, processors);

		logger.info("Task manager initialized.");
	}

	private void initProcessorBuckets(int numOfBucket, Map<TaskType, ITaskProcessor> processors) throws ServiceStartUpException
	{
		logger.info("Creating processor buckets...");
		processorBucketMap = new HashMap<Integer, TaskProcessorFactory>();
		for (int bucketIndex = 0; bucketIndex < numOfBuckets; bucketIndex++)
		{
			processorBucketMap.put(bucketIndex, new TaskProcessorFactory(processors));
		}
		logger.info("Processor buckets created.");
	}

	public boolean startedUp()
	{
		return startedUpFlag;
	}

	public void addTask(ITask newTask)
	{
		taskQueue.add(newTask);
	}

	@Override
	public void run()
	{
		logger.info("Task Manager start running.");

		// start all the processors
		startAllProcessors(processorBucketMap);
		logger.info("All task processors are started. Number of processors: " + getNumOfProcessors());

		logger.info("Start dispatching tasks.");
		startedUpFlag = true;
		// start dispatching tasks
		while (!Thread.currentThread().isInterrupted())
		{
			if (taskQueue.isEmpty())
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
					// dispatch task
					int bucketIndex = getBucketIndex(task.getKey());
					assignTaskByBucket(bucketIndex, task);
				}
				catch (Exception e)
				{
					logger.error("Error running Task Manager! Reason: " + e.getMessage());
				}
			}
		}
	}

	private void startAllProcessors(Map<Integer, TaskProcessorFactory> processors)
	{
		for (Map.Entry<Integer,TaskProcessorFactory> processorEntry : processors.entrySet())
		{
			try
			{
				TaskProcessorFactory processor = processorEntry.getValue();
				processor.start();
				while (!processor.startedUp())
				{
					sleep(1);
					// wait for task manager to start up all the processors
				}
			}
			catch (Exception e)
			{
				logger.error("Error starting up processor, index: " + processorEntry.getKey() + ". Reason: " + e.getMessage(), e);
			}
		}
	}

	private int getNumOfProcessors()
	{
		return processorBucketMap.size();
	}

	private int getBucketIndex(String key) throws ServiceProcessException
	{
		try
		{
			int bucketIndex = Math.abs(key.hashCode()) % getNumOfProcessors();
			return bucketIndex;
		}
		catch (Exception e)
		{
			throw new ServiceProcessException("Unable to get bucket index. Key: " + key + ", number of buckets: "
					+ getNumOfProcessors());
		}
	}

	public void assignTaskByBucket(int bucketIndex, ITask task) throws ServiceProcessException
	{
		if (bucketIndex < 0 || bucketIndex >= numOfBuckets)
		{
			throw new ServiceProcessException("Invalid bucket index: " + bucketIndex + ". Total number of buckets is: "
					+ numOfBuckets);
		}
		TaskProcessorFactory processor = processorBucketMap.get(bucketIndex);
		processor.addTask(task);
	}
}
