package com.nxiao.service.processor;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.nxiao.service.core.ITask;
import com.nxiao.service.core.ITaskProcessor;
import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.publisher.IPublisher;

public class UpdateTaskProcessor implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	DataCache dataCache;
	IPublisher publisher;
	Socket responder;

	public UpdateTaskProcessor(DataCache dataCache, int updateWorkerPort, IPublisher publisher)
	{
		this.dataCache = dataCache;
		this.publisher = publisher;

		// init response callback connection
		Context context = ZMQ.context(1);
		responder = context.socket(ZMQ.REQ);
		String uri = String.format("tcp://localhost:%d", updateWorkerPort);
		responder.connect(uri);
	}
	
	protected String validateUpdateData(String data) throws ServiceProcessException
	{
		//do nothing
		return null;
	}

	public void processTask(ITask task) throws ServiceProcessException
	{
		String callbackId = task.getCallbackId();
		String key = task.getKey();
		String taskContent = task.getTaskContent();

		String data = "";
		String error = "";

		if (key == null || key.isEmpty())
		{
			error = "Cannot find key in request.";
		}
		
		// parse request to Json
		try
		{			
			JSONObject request = (JSONObject) JSONValue.parse(taskContent);			
			if(request.containsKey("data"))
			{
				data = (String) request.get("data");
			}
			else
			{
				error = "Cannot find to update data in request. Required key is 'data'.";
				logger.error(error);
				sendResponse(callbackId, key, "", error);
				return;
			}
		}
		catch (Exception e)
		{
			error = "Unable to parse request to JSON. Request is malformat. Reason: " + e.getMessage();
			logger.error(error, e);
			sendResponse(callbackId, key, "", error);
			return;
		}
		
		// validate and save data
		try
		{
			String validationError = validateUpdateData(data);
			if(validationError == null || validationError.length() == 0)
			{
				dataCache.setDataByKey(key, data);
			}
			else
			{
				error = "Data is not saved to cache. Not valid to update: " + validationError;
				logger.info(error);
				sendResponse(callbackId, key, data, error);
				return;
			}
		}
		catch (Exception e)
		{
			error = "Failed to update data. Key: " + key + ", data: " + data +". Reason: " + e.getMessage();
			logger.error(error, e);
			sendResponse(callbackId, key, data, error);
			return;
		}
		
		sendResponse(callbackId, key, data, error);
	}

	private void sendResponse(String callbackId, String key, String data, String error)
	{
		try
		{
			// construct response
			TaskResponse response = new TaskResponse(key, data, error);

			// send response back to client
			sendResponseToClient(callbackId, response.getStringResponse());
		}
		catch (Exception e)
		{
			error = "Failed to send response back to client. key: " + key + ". Reason: " + e.getMessage();
			logger.error(error, e);
		}
	}

	public void sendResponseToClient(String callbackId, String response)
	{
		responder.sendMore(callbackId);
		responder.sendMore("");
		responder.send(response, 0);
	}
}
