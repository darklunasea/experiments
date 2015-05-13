package com.nxiao.service.processor;

import org.json.simple.JSONObject;

import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;

public abstract class DataTaskProcessor extends TaskProcessor implements ITaskProcessor
{
	DataCache dataCache;

	public DataTaskProcessor(int workerPort, DataCache dataCache)
	{
		super(workerPort);
		this.dataCache = dataCache;
	}
	public abstract ITaskProcessor newSession() throws ServiceStartUpException;
	protected abstract TaskResponse process(JSONObject request) throws ServiceProcessException;

	@Override
	protected String validate(JSONObject request) throws ServiceProcessException
	{
		String validationError = "";
		if (!request.containsKey("schema"))
		{
			validationError = validationError + "Cannot find 'schema' in request.";
		}
		if (!request.containsKey("key"))
		{
			validationError = validationError + "Cannot find 'key' in request.";
		}
		return validationError;
	}	
}
