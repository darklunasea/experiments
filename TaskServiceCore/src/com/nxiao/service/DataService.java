package com.nxiao.service;

import java.util.ArrayList;
import java.util.List;

import com.nxiao.service.core.BasicTaskService;
import com.nxiao.service.core.TaskHandler;
import com.nxiao.service.core.TaskReceiver;
import com.nxiao.service.core.TaskType;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceStartUpException;
import com.nxiao.service.core.publisher.ZeroMqPublisher;
import com.nxiao.service.processor.QueryTaskProcessor;
import com.nxiao.service.processor.UpdateTaskProcessor;

public class DataService
{
	public static void main(String[] args) throws ServiceStartUpException
	{
		DataService service = new DataService("data_service");
		service.start();
	}

	BasicTaskService service;

	public DataService(String serviceName) throws ServiceStartUpException
	{
		int processorPoolSize = 5;
		int redisConnPoolSize = 5;

		// init data cache
		String redisHost = "172.16.43.90";		
		DataCache dataCache = new DataCache(redisConnPoolSize, redisHost, serviceName);

		// init receiver and processors
		List<TaskHandler> handlers = new ArrayList<TaskHandler>();

		// Add Query functionality
		int queryClientPort = 9001;
		int queryWorkerPort = 9002;
		TaskReceiver queryReceiver = new TaskReceiver(serviceName, TaskType.QUERY, queryClientPort, queryWorkerPort);
		QueryTaskProcessor queryProcessor = new QueryTaskProcessor(queryWorkerPort, dataCache);
		TaskHandler queryHandler = new TaskHandler(TaskType.QUERY, queryReceiver, queryProcessor);

		// Add Update functionality
		int updateClientPort = 9003;
		int updateWorkerPort = 9004;
		TaskReceiver updateReceiver = new TaskReceiver(serviceName, TaskType.UPDATE, updateClientPort, updateWorkerPort);
		// init publisher
		int publisherPort = 9005;
		ZeroMqPublisher publisher = new ZeroMqPublisher(publisherPort);
		UpdateTaskProcessor updateProcessor = new UpdateTaskProcessor(updateWorkerPort, dataCache, publisher);
		TaskHandler updateHandler = new TaskHandler(TaskType.UPDATE, updateReceiver, updateProcessor);

		handlers.add(queryHandler);
		handlers.add(updateHandler);
		this.service = new BasicTaskService(serviceName, handlers, processorPoolSize);
	}

	public void start()
	{
		this.service.start();
	}
}
