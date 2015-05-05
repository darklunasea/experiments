package com.nxiao.service.zeromq;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.nxiao.service.exception.ServiceProcessException;

public abstract class BasicZeroMqWorker extends Thread
{
	private Logger logger = Logger.getLogger(this.getClass());

	Context context;
	Socket responder;

	public BasicZeroMqWorker(String brokerBackendHost, int brokerBackendPort)
	{
		logger.info("Creating ZeroMq worker...");

		context = ZMQ.context(1);
		// Socket to talk to server
		responder = context.socket(ZMQ.REP);
		responder.connect(String.format("tcp://%s:%d", brokerBackendHost, brokerBackendPort));

		logger.info("ZeroMq worker created.");
	}

	public void close()
	{
		responder.close();
		context.term();
	}

	public abstract JSONObject process(JSONObject req) throws ServiceProcessException;

	@Override
	public void run()
	{
		while (!Thread.currentThread().isInterrupted())
		{
			JSONObject response = new JSONObject();

			// Wait for next request from client
			byte[] byteRequestId = responder.recv(0);
			byte[] byteRequest = responder.recv(0);

			// invalid request
			if (byteRequestId == null || byteRequest == null || byteRequestId.length < 1 || byteRequest.length < 2)
			{
				String err = "Invalid request. Missing request ID or content. Ignored.";
				logger.debug(err);
				response.put("error", err);
				sendResponse(new String(byteRequestId), response);
				continue;
			}

			String requestId = new String(byteRequestId);
			String request = new String(byteRequest);
			logger.debug("Recieved request. Request ID: " + requestId + ", Request: " + request);

			// parse request to json
			JSONObject req = new JSONObject();
			try
			{
				req = (JSONObject) JSONValue.parse(request);
			}
			catch (Exception e)
			{
				String err = "Unable to parse request to Json. Malformat request. Reason: " + e.getMessage();
				logger.error(err, e);
				response.put("error", err);
				sendResponse(requestId, response);
				continue;
			}

			// process request
			try
			{
				response = process(req);
			}
			catch (ServiceProcessException e)
			{
				String err = "Failed to process request. Reason: " + e.getMessage();
				logger.error(err, e);
				response.put("error", err);
				sendResponse(requestId, response);
				continue;
			}

			// Send reply back to client
			sendResponse(requestId, response);
		}
	}

	private void sendResponse(String requestId, JSONObject response)
	{
		responder.sendMore(requestId);
		responder.send(response.toString(), 0);
	}
}
