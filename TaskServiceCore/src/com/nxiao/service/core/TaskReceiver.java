package com.nxiao.service.core;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;

public class TaskReceiver extends Thread implements ITaskReceiver
{
	private Logger logger = Logger.getLogger(this.getClass());

	String serviceName;
	TaskType taskType;
	TaskManager taskManager;
	Context context;
	Socket frontend;
	Socket backend;
	Poller items;

	public TaskReceiver(String serviceName, TaskType taskType, int clientRequestPort, int workerResponsePort)
			throws ServiceStartUpException
	{
		logger.info("Creating Receiver for [" + taskType.toString() + "]...");

		this.serviceName = serviceName;
		this.taskType = taskType;

		context = ZMQ.context(1);
		frontend = context.socket(ZMQ.ROUTER);
		backend = context.socket(ZMQ.ROUTER);

		frontend.bind("tcp://*:" + clientRequestPort);
		backend.bind("tcp://*:" + workerResponsePort);

		// Initialize poll set
		items = new Poller(2);
		items.register(frontend, Poller.POLLIN);
		items.register(backend, Poller.POLLIN);

		logger.info("Task Receiver created. Bind to port" + workerResponsePort);
	}

	public void setTaskManager(TaskManager taskManager)
	{
		this.taskManager = taskManager;
	}

	public void close()
	{
		frontend.close();
		backend.close();
		context.term();
	}

	@Override
	public void run()
	{
		logger.info("Task Receiver for type [" + taskType.toString() + "] start running.");
		while (!Thread.currentThread().isInterrupted())
		{
			// poll and memorize multipart detection
			items.poll();

			if (items.pollin(0))
			{
				// pass client request to dealer
				byte[] id = frontend.recv(0);
				frontend.recv(0);
				byte[] msg = frontend.recv(0);

				String requestId = new String(id);
				String request = new String(msg);
				logger.info("Request received from client: " + requestId + ", request: " + request);

				onReceiveRequest(requestId, request);
			}
			if (items.pollin(1))
			{
				// pass worker response to client
				backend.recv(0);
				byte[] callbackId = backend.recv(0);
				backend.recv(0);
				byte[] msg = backend.recv(0);

				logger.info("Response received from worker for client: " + new String(callbackId) + ", "
						+ new String(msg));

				frontend.send(callbackId, ZMQ.SNDMORE);
				frontend.sendMore("");
				frontend.send(msg, 0);
			}
		}
	}

	private void onReceiveRequest(String requestId, String request)
	{
		String error = "";
		JSONObject req;
		try
		{
			req = (JSONObject) JSONValue.parse(request);
		}
		catch (Exception e)
		{
			error = "Invalid request. Malformat in Json. Reason: " + e.getMessage();
			logger.error(error, e);
			sendErrorBackToClient(error);
			return;
		}

		try
		{
			//validate request
			String validationError = null;
			if(!validateRequest(req, validationError))
			{
				//invalid request. notify client.
				logger.error(validationError);
				sendErrorBackToClient(validationError);
				return;
			}
			
			//create task for request
			String key = getKeyFromRequest(req);
			Task task = new Task(requestId, taskType, request, key);
			taskManager.addTask(task);
		}
		catch (ServiceProcessException e)
		{
			error = "Failed to add task. Reason: " + e.getMessage();
			logger.error(error, e);
			sendErrorBackToClient(error);
			return;
		}
	}

	private void sendErrorBackToClient(String error)
	{
		TaskResponse resp = new TaskResponse(error);
		frontend.send(resp.getStringResponse());
	}

	private String getKeyFromRequest(JSONObject req) throws ServiceProcessException
	{
		String key = (String) req.get("key");
		if (key == null || key.isEmpty())
		{
			throw new ServiceProcessException("Invalid request. No Keyword found in the request.");
		}
		return key;
	}

	protected boolean validateRequest(JSONObject request, String validationError) throws ServiceProcessException
	{
		if (!request.containsKey("service"))
		{
			validationError = "Invalid request. No service specified in request.";
		}
		else
		{
			String requestedService = (String) request.get("service");
			if (!serviceName.equalsIgnoreCase(requestedService))
			{
				validationError = "Requested service mismatch. This service is: + " + serviceName
						+ ". Requested service is: "
						+ requestedService;
				return false;
			}
		}

		if (!request.containsKey("task"))
		{
			validationError = "Invalid request. No task specified in request.";
			return false;
		}
		else
		{
			String requestedTask = (String) request.get("task");
			if (!taskType.toString().equalsIgnoreCase(requestedTask))
			{
				// requested task is not a QUERY task
				validationError = "Requested Task mismatch. This task is: + " + taskType.toString()
						+ ". Requested task is: "
						+ requestedTask;
				return false;
			}
		}
		return true;
	}
}
