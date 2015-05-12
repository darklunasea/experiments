package com.nxiao.service.processor;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;

public class QueryTaskProcessor extends TaskProcessor implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	DataCache dataCache;

	public QueryTaskProcessor(int queryWorkerPort, DataCache dataCache)
	{
		super(queryWorkerPort);
		this.dataCache = dataCache;
		logger.info("Query Task Processor created.");
	}

	public ITaskProcessor newSession() throws ServiceStartUpException
	{
		return new QueryTaskProcessor(this.workerPort, this.dataCache);
	}

	protected String validate(JSONObject request) throws ServiceProcessException
	{
		String validationError = null;
		if (!request.containsKey("key"))
		{
			validationError = "Cannot find key in request.";
		}
		return validationError;
	}

	public TaskResponse process(JSONObject request) throws ServiceProcessException
	{
		String error = "";
		String key = (String) request.get("key");

		// retrieve data
		String data = "";
		try
		{
			if (dataCache.existKey(key))
			{
				data = dataCache.getDataByKey(key);
			}
			else
			{
				error = "Requested key doesn't exist in data cache. Key: " + key;
				logger.info(error);
			}
		}
		catch (Exception e)
		{
			error = "Failed to get data from data cache by key: " + key + ". Reason: " + e.getMessage();
			logger.error(error, e);
		}

		return new TaskResponse(key, data, error);
	}
}
