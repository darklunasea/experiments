package com.nxiao.service.core;

import org.apache.log4j.Logger;

import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceStartUpException;
import com.nxiao.service.core.publisher.ZeroMqPublisher;

public class BasicTaskService extends Thread
{
	static final int QUERY_CLIENT_PORT_OFFSET = 1;
	static final int QUERY_WORKER_PORT_OFFSET = 2;
	static final int UPDATE_CLIENT_PORT_OFFSET = 3;
	static final int UPDATE_WORKER_PORT_OFFSET = 4;
	static final int PUBLISHER_PORT_OFFSET = 5;
	
	String serviceName;	
	TaskManager taskManager;
	TaskReceiver queryTaskReceiver;
	
	int queryClientPort;
	int queryWorkerPort;
	int publisherPort;

	public static void main(String[] args) throws ServiceStartUpException
	{		
		BasicTaskService service = new BasicTaskService("test_service");
		service.start();
	}

	private Logger logger = Logger.getLogger(this.getClass());

	public BasicTaskService(String serviceName) throws ServiceStartUpException
	{
		logger.info("Initializing Data Service...");
		
		this.serviceName = serviceName;
		int portGroupStartNum = ServiceContext.PORT_GROUP_START.value();
		
		queryClientPort = portGroupStartNum + QUERY_CLIENT_PORT_OFFSET;
		queryWorkerPort = portGroupStartNum + QUERY_WORKER_PORT_OFFSET;
		publisherPort = portGroupStartNum + PUBLISHER_PORT_OFFSET;
		
		String redisHost = "172.16.43.90";
		DataCache dataCache = initDataCache(redisHost);
		ZeroMqPublisher publisher = initPublisher(publisherPort);

		taskManager = initTaskManager(dataCache, publisher);
		queryTaskReceiver = initQueryTaskReceiver(taskManager, queryClientPort, queryWorkerPort);

		logger.info("----- Data Service has been initialized successfully -----");
	}

	@Override
	public void run()
	{
		try
		{
			logger.info("Starting up Service [" + serviceName + "]...");
			taskManager.start();
			while (!taskManager.startedUp())
			{
				sleep(1);
				// wait for task manager to start up all the processors
			}

			queryTaskReceiver.start();
		}
		catch (Exception e)
		{
			logger.error("Error during start up of service! Error: " + e.getMessage(), e);
		}
	}

	private TaskReceiver initQueryTaskReceiver(TaskManager taskManager, int queryClientPort, int queryWorkerPort) throws ServiceStartUpException
	{		
		TaskReceiver queryTaskReceiver = new TaskReceiver(TaskType.QUERY, taskManager, queryClientPort, queryWorkerPort);
		return queryTaskReceiver;
	}

	private TaskManager initTaskManager(DataCache dataCache, ZeroMqPublisher publisher)
			throws ServiceStartUpException
	{
		int numberOfProcessorBuckets = 10;
		TaskManager taskManager = new TaskManager(numberOfProcessorBuckets, dataCache, publisher);
		return taskManager;
	}

	private DataCache initDataCache(String redisHost) throws ServiceStartUpException
	{
		int redisConnPoolSize = 10;
		DataCache dataCache = new DataCache(redisConnPoolSize, redisHost, serviceName);
		return dataCache;
	}

	private ZeroMqPublisher initPublisher(int publisherPort) throws ServiceStartUpException
	{
		return new ZeroMqPublisher(publisherPort);
	}
}
