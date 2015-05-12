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

	TaskType taskType;
	TaskManager taskManager;
	Context context;
	Socket frontend;
	Socket backend;
	Poller items;

	public TaskReceiver(TaskType taskType, int clientRequestPort, int workerResponsePort) throws ServiceStartUpException
	{
		logger.info("Creating Receiver for [" + taskType.toString() + "]...");

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
				
				logger.info("Response received from worker for client: " + new String(callbackId) + ", " + new String(msg));
				
				frontend.send(callbackId, ZMQ.SNDMORE);
				frontend.sendMore("");
				frontend.send(msg, 0);
			}
		}
	}

	private void onReceiveRequest(String requestId, String request)
	{
		try
		{
			Task task = createNewTask(requestId, request);
			taskManager.addTask(task);
		}
		catch (ServiceProcessException e)
		{
			logger.error("Failed to add task. Reason: " + e.getMessage());
			TaskResponse resp = new TaskResponse(e.getMessage());
			frontend.send(resp.getStringResponse());
		}
	}

	private Task createNewTask(String requestId, String request) throws ServiceProcessException
	{
		String key = getKeyFromRequest(request);
		Task task = new Task(requestId, taskType, request, key);
		return task;
	}

	private String getKeyFromRequest(String request) throws ServiceProcessException
	{
		try
		{
			JSONObject req = (JSONObject) JSONValue.parse(request);
			String key = (String) req.get("key");
			if (key == null || key.isEmpty())
			{
				throw new ServiceProcessException("Invalid request. No Keyword found in the request.");
			}
			return key;
		}
		catch (Exception e)
		{
			throw new ServiceProcessException("Invalid request. Malformat in Json.");
		}
	}
}
