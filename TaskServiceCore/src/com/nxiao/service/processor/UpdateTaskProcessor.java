package com.nxiao.service.processor;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;
import com.nxiao.service.core.publisher.IPublisher;

public class UpdateTaskProcessor extends TaskProcessor implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	DataCache dataCache;
	IPublisher publisher;

	public UpdateTaskProcessor(int updateWorkerPort, DataCache dataCache, IPublisher publisher)
	{
		super(updateWorkerPort);
		this.dataCache = dataCache;
		this.publisher = publisher;
		logger.info("Update Task Processor created.");
	}

	public ITaskProcessor newSession() throws ServiceStartUpException
	{
		return new UpdateTaskProcessor(this.workerPort, this.dataCache, this.publisher);
	}

	protected String validate(JSONObject request) throws ServiceProcessException
	{
		String validationError = null;
		if (!request.containsKey("key"))
		{
			validationError = "Cannot find key in request.";
		}
		if (!request.containsKey("data"))
		{
			validationError = "Cannot find update data in request.";
		}
		return validationError;
	}

	protected TaskResponse process(JSONObject request) throws ServiceProcessException
	{
		String error = "";
		String key = (String) request.get("key");
		String data = (String) request.get("data");

		// save data
		try
		{
			dataCache.setDataByKey(key, data);
		}
		catch (Exception e)
		{
			error = "Failed to update data. Key: " + key + ", data: " + data + ". Reason: " + e.getMessage();
			logger.error(error, e);
		}

		return new TaskResponse(key, "", error);
	}
}
