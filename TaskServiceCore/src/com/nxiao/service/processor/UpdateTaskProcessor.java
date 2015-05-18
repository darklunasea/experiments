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
		this.validator.addValidateFields("table", "data");
		logger.info("Update Task Processor created.");
	}

	public ITaskProcessor newSession() throws ServiceStartUpException
	{
		return new UpdateTaskProcessor(this.serviceContext, this.workerPort,
				this.dataCache, this.publisher);
	}

	protected TaskResponse process(JSONObject request)
			throws ServiceProcessException
	{
		String error = "";
		String table = (String) request.get("table");
		String key = (String) request.get("key");
		String newData = (String) request.get("data");

		// save data
		try
		{
			if (newData.isEmpty())
			{
				error = "Update data is empty.";
			}

			if (key == null || key.isEmpty())
			{
				// new
				key = dataCache.getNewIdInTable(table);
				logger.info("New key [" + key + "] generated for data [" + newData + "]");
				setDataAndPublish(table, key, newData);
			}
			else
			{
				// update
				String oldData = dataCache.getDataByKeyInTable(table, key);
				if (!newData.equals(oldData))
				{
					setDataAndPublish(table, key, newData);
				}
			}
		}
		catch (Exception e)
		{
			error = "Failed to update data for key [" + key + "] in table ["
					+ table + "]. Reason- " + e.getMessage();
			logger.error(error, e);
		}

		return new TaskResponse(key, "", error);
	}

	private void setDataAndPublish(String table, String key, String newData)
	{
		dataCache.setDataByKeyInTable(table, key, newData);
		publishUpdate(table, key, newData);
	}

	protected void publishUpdate(String table, String key, String data)
	{
		String topic = publishTopicPrefix + table;
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
