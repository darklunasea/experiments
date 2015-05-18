package com.nxiao.service.processor;

import org.json.simple.JSONObject;

import com.nxiao.service.core.ServiceContext;
import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;

public abstract class DataTaskProcessor extends TaskProcessor implements ITaskProcessor
{
	protected DataCache dataCache;

	public DataTaskProcessor(ServiceContext serviceContext, int workerPort, DataCache dataCache)
	{
		super(serviceContext, workerPort);
		this.dataCache = dataCache;
	}
	public abstract ITaskProcessor newSession() throws ServiceStartUpException;
	protected abstract TaskResponse process(JSONObject request) throws ServiceProcessException;
}
