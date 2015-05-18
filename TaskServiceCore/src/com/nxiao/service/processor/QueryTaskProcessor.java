package com.nxiao.service.processor;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.nxiao.service.core.ServiceContext;
import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;

public class QueryTaskProcessor extends DataTaskProcessor implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	public QueryTaskProcessor(ServiceContext serviceContext, int queryWorkerPort, DataCache dataCache)
	{
		super(serviceContext, queryWorkerPort, dataCache);
		this.validator.addValidateFields("table", "key");
		logger.info("Query Task Processor created.");
	}

	public ITaskProcessor newSession() throws ServiceStartUpException
	{
		return new QueryTaskProcessor(this.serviceContext, this.workerPort, this.dataCache);
	}

	public TaskResponse process(JSONObject request) throws ServiceProcessException
	{
		String error = "";
		String table = (String) request.get("table");
		String key = (String) request.get("key");

		// retrieve data
		String data = "";
		try
		{
			if (dataCache.existKeyInTable(table, key))
			{
				data = dataCache.getDataByKeyInTable(table, key);
			}
			else
			{
				error = "Requested key [" + key + "] doesn't exist in table [" + table + "]";
				logger.info(error);
			}
		}
		catch (Exception e)
		{
			error = "Failed to get data from table [" + table + "] by key [" + key + "]. Reason- " + e.getMessage();
			logger.error(error, e);
		}

		return new TaskResponse(key, data, error);
	}
}
