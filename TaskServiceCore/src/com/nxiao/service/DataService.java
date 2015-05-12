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
		DataService service = new DataService();
		service.start();
	}

	BasicTaskService service;

	public DataService() throws ServiceStartUpException
	{
		String serviceName = "test_service";

		int processorPoolSize = 10;

		// init data cache
		String redisHost = "172.16.43.90";
		int redisConnPoolSize = 10;
		DataCache dataCache = new DataCache(redisConnPoolSize, redisHost, serviceName);

		// init receiver and processors
		List<TaskHandler> handlers = new ArrayList<TaskHandler>();

		// Add Query functionality
		int queryClientPort = 9001;
		int queryWorkerPort = 9002;
		TaskReceiver queryReceiver = new TaskReceiver(TaskType.QUERY, queryClientPort, queryWorkerPort);
		QueryTaskProcessor queryProcessor = new QueryTaskProcessor(dataCache, queryWorkerPort);
		TaskHandler queryHandler = new TaskHandler(TaskType.QUERY, queryReceiver, queryProcessor);

		// Add Update functionality
		int updateClientPort = 9003;
		int updateWorkerPort = 9004;
		TaskReceiver updateReceiver = new TaskReceiver(TaskType.UPDATE, updateClientPort, updateWorkerPort);
		// init publisher
		int publisherPort = 9005;
		ZeroMqPublisher publisher = new ZeroMqPublisher(publisherPort);
		UpdateTaskProcessor updateProcessor = new UpdateTaskProcessor(dataCache, updateWorkerPort, publisher);
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
