package com.nxiao.service.processor;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.nxiao.service.core.ServiceContext;
import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.TaskServiceParam;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;
import com.nxiao.service.core.publisher.IPublisher;

public class UpdateTaskProcessor extends DataTaskProcessor implements
		ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	String publishTopicPrefix;
	IPublisher publisher;

	public UpdateTaskProcessor(ServiceContext serviceContext,
			int updateWorkerPort, DataCache dataCache, IPublisher publisher)
	{
		super(serviceContext, updateWorkerPort, dataCache);
		String serviceName = serviceContext.get(TaskServiceParam.ServiceName);
		this.publishTopicPrefix = serviceName + ".";
		this.publisher = publisher;
		logger.info("Update Task Processor created.");
	}

	public ITaskProcessor newSession() throws ServiceStartUpException
	{
		return new UpdateTaskProcessor(this.serviceContext, this.workerPort,
				this.dataCache, this.publisher);
	}

	protected String validate(JSONObject request)
			throws ServiceProcessException
	{
		String validationError = super.validate(request);
		if (!request.containsKey("data"))
		{
			validationError = validationError
					+ "Cannot find update 'data' in request.";
		}
		return validationError;
	}

	protected TaskResponse process(JSONObject request)
			throws ServiceProcessException
	{
		String error = "";
		String schema = (String) request.get("schema");
		String key = (String) request.get("key");
		String newData = (String) request.get("data");

		// save data
		try
		{
			if (newData.isEmpty())
			{
				error = "Update data is empty.";
			}
			String oldData = dataCache.getDataByKey(schema, key);
			if(!newData.equals(oldData))
			{
				dataCache.setDataByKey(schema, key, newData);
				publishUpdate(schema, key, newData);
			}			
		}
		catch (Exception e)
		{
			error = "Failed to update data for key [" + key + "] in schema ["
					+ schema + "]. Reason- " + e.getMessage();
			logger.error(error, e);
		}

		return new TaskResponse(key, "", error);
	}

	protected void publishUpdate(String schema, String key, String data)
	{
		String topic = publishTopicPrefix + schema;
		String message = new TaskResponse(key, data).getStringResponse();
		try
		{
			publisher.post(topic, message);
		}
		catch (Exception e)
		{
			logger.error("Failed to publish update to topic [" + topic + "]. Reason: " + e.getMessage(), e);
		}
	}
}
