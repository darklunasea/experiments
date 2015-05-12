package com.nxiao.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nxiao.service.core.exception.ServiceStartUpException;

public class BasicTaskService extends Thread
{
	String serviceName;
	TaskManager taskManager;
	List<ITaskReceiver> receivers;

	private Logger logger = Logger.getLogger(this.getClass());

	public BasicTaskService(String serviceName, List<TaskHandler> taskHandlers,
			int processorPoolSize) throws ServiceStartUpException
	{
		validateInputs(serviceName, taskHandlers, processorPoolSize);

		logger.info("Initializing Service [" + serviceName + "]...");

		this.serviceName = serviceName;

		Map<TaskType, ITaskProcessor> processors = new HashMap<TaskType, ITaskProcessor>();
		for (TaskHandler handler : taskHandlers)
		{
			// add receiver
			this.receivers.add(handler.getTaskReceiver());
			// add processor
			processors.put(handler.getTaskType(), handler.getTaskProcessor());
		}

		taskManager = new TaskManager(processorPoolSize, processors);
		// assign task manager to the receivers
		for (ITaskReceiver receiver : receivers)
		{
			receiver.setTaskManager(taskManager);
			;
		}

		logger.info("----- Service has been initialized successfully -----");
	}

	private void validateInputs(String serviceName, List<TaskHandler> taskHandlers, int processorPoolSize)
			throws ServiceStartUpException
	{
		if (serviceName == null || serviceName.length() == 0)
		{
			throw new ServiceStartUpException("Please specify service name.");
		}
		if (taskHandlers == null || taskHandlers.size() == 0)
		{
			throw new ServiceStartUpException("At least one task handler is required.");
		}
		if (processorPoolSize <= 1)
		{
			throw new ServiceStartUpException("Processor Pool Size needs to be at least 1.");
		}
	}

	@Override
	public void run()
	{
		try
		{
			logger.info("Starting up Service [" + serviceName + "]...");
			// start the task manager
			taskManager.start();
			while (!taskManager.startedUp())
			{
				sleep(1);
				// wait for task manager to start up all the processors
			}
			// start all the receivers
			for (ITaskReceiver receiver : receivers)
			{
				receiver.start();
			}
		}
		catch (Exception e)
		{
			logger.error("Error during start up of service! Error: " + e.getMessage(), e);
		}
	}
}
