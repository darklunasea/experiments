package com.nxiao.service.processor;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;
import com.nxiao.service.core.publisher.IPublisher;

public class UpdateTaskProcessor extends DataTaskProcessor implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	IPublisher publisher;

	public UpdateTaskProcessor(int updateWorkerPort, DataCache dataCache, IPublisher publisher)
	{
		super(updateWorkerPort, dataCache);
		this.publisher = publisher;
		logger.info("Update Task Processor created.");
	}

	public ITaskProcessor newSession() throws ServiceStartUpException
	{
		return new UpdateTaskProcessor(this.workerPort, this.dataCache, this.publisher);
	}

	protected String validate(JSONObject request) throws ServiceProcessException
	{
		String validationError = super.validate(request);
		if (!request.containsKey("data"))
		{
			validationError = validationError + "Cannot find update 'data' in request.";
		}
		return validationError;
	}

	protected TaskResponse process(JSONObject request) throws ServiceProcessException
	{
		String error = "";
		String schema = (String) request.get("schema");
		String key = (String) request.get("key");
		String data = (String) request.get("data");

		// save data
		try
		{
			if (data.isEmpty())
			{
				error = "Update data is empty.";
			}
			dataCache.setDataByKey(schema, key, data);
		}
		catch (Exception e)
		{
			error = "Failed to update data for key [" + key + "] in schema [" + schema + "]. Reason- " + e.getMessage();
			logger.error(error, e);
		}

		return new TaskResponse(key, "", error);
	}
}
