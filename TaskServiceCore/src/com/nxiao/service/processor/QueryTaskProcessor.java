package com.nxiao.service.processor;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;

public class QueryTaskProcessor extends DataTaskProcessor implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	public QueryTaskProcessor(int queryWorkerPort, DataCache dataCache)
	{
		super(queryWorkerPort, dataCache);
		logger.info("Query Task Processor created.");
	}

	public ITaskProcessor newSession() throws ServiceStartUpException
	{
		return new QueryTaskProcessor(this.workerPort, this.dataCache);
	}

	protected String validate(JSONObject request) throws ServiceProcessException
	{
		return super.validate(request);
	}

	public TaskResponse process(JSONObject request) throws ServiceProcessException
	{
		String error = "";
		String schema = (String) request.get("schema");
		String key = (String) request.get("key");

		// retrieve data
		String data = "";
		try
		{
			if (dataCache.existKey(schema, key))
			{
				data = dataCache.getDataByKey(schema, key);
			}
			else
			{
				error = "Requested key [" + key + "] doesn't exist in schema [" + schema + "]";
				logger.info(error);
			}
		}
		catch (Exception e)
		{
			error = "Failed to get data from schema [" + schema + "] by key [" + key + "]. Reason- " + e.getMessage();
			logger.error(error, e);
		}

		return new TaskResponse(key, data, error);
	}
}
