package com.nxiao.service;

import java.util.ArrayList;
import java.util.List;

import com.nxiao.service.core.BasicTaskService;
import com.nxiao.service.core.TaskHandler;
import com.nxiao.service.core.TaskReceiver;
import com.nxiao.service.core.ServiceContext;
import com.nxiao.service.core.TaskServiceParam;
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
		ServiceContext context = new ServiceContext();
		//core service parameters
		context.set(TaskServiceParam.ServiceName, "data_service");
		context.set(TaskServiceParam.ProcessorPoolSize, 5);
		//data service parameters		
		context.set(DataServiceParam.RedisHost, "172.16.43.90");
		context.set(DataServiceParam.QueryRequestPort, 9001);
		context.set(DataServiceParam.UpdateRequestPort, 9002);
		context.set(DataServiceParam.UpdatePublishPort, 9003);
		context.set(DataServiceParam.QueryResponsePort, 9004);
		context.set(DataServiceParam.UpdateResponsePort, 9005);
		context.set(DataServiceParam.RedisConnPoolSize, 5);
		
		DataService service = new DataService(context);
		service.start();
	}

	BasicTaskService service;

	public DataService(ServiceContext serviceContext) throws ServiceStartUpException
	{		
		String validationErr = serviceContext.validate(DataServiceParam.class);
		if(!validationErr.isEmpty())
		{
			throw new ServiceStartUpException(validationErr);
		}
		
		// init data cache
		int redisConnPoolSize = serviceContext.get(DataServiceParam.RedisConnPoolSize);
		String redisHost = serviceContext.get(DataServiceParam.RedisHost);
		DataCache dataCache = new DataCache(redisConnPoolSize, redisHost);

		// init receiver and processors
		List<TaskHandler> handlers = new ArrayList<TaskHandler>();

		// Add Query functionality
		int queryClientPort = serviceContext.get(DataServiceParam.QueryRequestPort);
		int queryWorkerPort = serviceContext.get(DataServiceParam.QueryResponsePort);
		TaskReceiver queryReceiver = new TaskReceiver(TaskType.QUERY, serviceContext, queryClientPort, queryWorkerPort);
		QueryTaskProcessor queryProcessor = new QueryTaskProcessor(serviceContext, queryWorkerPort, dataCache);
		TaskHandler queryHandler = new TaskHandler(TaskType.QUERY, queryReceiver, queryProcessor);

		// Add Update functionality
		int updateClientPort = serviceContext.get(DataServiceParam.UpdateRequestPort);
		int updateWorkerPort = serviceContext.get(DataServiceParam.UpdateResponsePort);
		TaskReceiver updateReceiver = new TaskReceiver(TaskType.UPDATE, serviceContext, updateClientPort, updateWorkerPort);
		// init publisher
		int publisherPort = serviceContext.get(DataServiceParam.UpdatePublishPort);
		ZeroMqPublisher publisher = new ZeroMqPublisher(publisherPort);
		UpdateTaskProcessor updateProcessor = new UpdateTaskProcessor(serviceContext, updateWorkerPort, dataCache, publisher);
		TaskHandler updateHandler = new TaskHandler(TaskType.UPDATE, updateReceiver, updateProcessor);

		handlers.add(queryHandler);
		handlers.add(updateHandler);
		this.service = new BasicTaskService(serviceContext, handlers);
	}

	public void start()
	{
		this.service.start();
	}
}
