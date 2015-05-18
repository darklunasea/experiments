package com.nxiao.service.processor;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.nxiao.service.core.ITask;
import com.nxiao.service.core.ServiceContext;
import com.nxiao.service.core.TaskResponse;
import com.nxiao.service.core.exception.ServiceProcessException;

public abstract class TaskProcessor implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	protected ServiceContext serviceContext;
	protected int workerPort;
	protected Validator validator;
	Socket responder;

	public TaskProcessor(ServiceContext serviceContext, int workerPort)
	{
		this.serviceContext = serviceContext;
		this.workerPort = workerPort;
		this.validator = new Validator();

		// init response callback connection
		Context context = ZMQ.context(1);
		responder = context.socket(ZMQ.DEALER);
		String uri = String.format("tcp://localhost:%d", workerPort);
		responder.connect(uri);
	}

	public void processTask(ITask task) throws ServiceProcessException
	{
		String callbackId = task.getCallbackId();
		String taskContent = task.getTaskContent();

		JSONObject request;
		try
		{
			request = (JSONObject) JSONValue.parse(taskContent);
		}
		catch (Exception e)
		{
			String error = "Unable to parse request to JSON. Request is malformat. Reason: " + e.getMessage();
			logger.error(error, e);
			sendResponseToClient(callbackId, new TaskResponse(error));
			return;
		}

		String validationError = validate(request).toString();
		if (validationError == null || validationError.isEmpty())
		{
			TaskResponse response = process(request);
			sendResponseToClient(callbackId, response);
		}
		else
		{
			String error = "Reqeust is not valid: " + validationError;
			logger.info(error);
			sendResponseToClient(callbackId, new TaskResponse(error));
			return;
		}
	}

	protected String validate(JSONObject request) throws ServiceProcessException
	{
		return validator.validate(request);
	}

	abstract protected TaskResponse process(JSONObject request) throws ServiceProcessException;

	protected void sendResponseToClient(String callbackId, TaskResponse response) throws ServiceProcessException
	{
		try
		{
			// send response back to client
			sendResponse(callbackId, response.getStringResponse());
		}
		catch (Exception e)
		{
			String error = "Failed to send response back to client. callbackId: " + callbackId + ". Reason: " + e.getMessage();
			logger.error(error, e);
			throw new ServiceProcessException(error, e);
		}
	}

	protected void sendResponse(String callbackId, String response)
	{
		responder.sendMore(callbackId);
		responder.sendMore("");
		responder.send(response, 0);
	}
}
