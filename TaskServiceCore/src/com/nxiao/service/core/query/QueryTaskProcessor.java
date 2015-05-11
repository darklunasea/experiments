package com.nxiao.service.core.query;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.nxiao.service.core.ITask;
import com.nxiao.service.core.ITaskProcessor;
import com.nxiao.service.core.data.DataCache;
import com.nxiao.service.core.exception.ServiceProcessException;

public class QueryTaskProcessor implements ITaskProcessor
{
	private Logger logger = Logger.getLogger(this.getClass());

	DataCache dataCache;
	Socket responder;

	public QueryTaskProcessor(DataCache dataCache, int queryWorkerPort)
	{
		this.dataCache = dataCache;		

		//init response callback connection
		Context context = ZMQ.context(1);
		responder = context.socket(ZMQ.REQ);
		String uri = String.format("tcp://localhost:%d", queryWorkerPort);
		responder.connect(uri);
	}

	public void processTask(ITask task) throws ServiceProcessException
	{
		String callbackId = task.getCallbackId();
		String key = task.getKey();
		String data = "";
		String error = "";

		if (key == null || key.isEmpty())
		{
			error = "Cannot find key in request.";
		}

		// retrieve data
		try
		{
			data = dataCache.getDataByKey(key);
		}
		catch (Exception e)
		{
			error = "Failed to get data from data cache by key: " + key + ". Reason: " + e.getMessage();
			logger.error(error, e);
		}

		try
		{
			// construct response
			QueryResponse response = new QueryResponse(key, data, error);

			// send response back to client
			sendResponse(callbackId, response.getStringResponse());
		}
		catch (Exception e)
		{
			error = "Failed to send response back to client. key: " + key + ". Reason: " + e.getMessage();
			logger.error(error, e);
		}
	}
	
	public void sendResponse(String callbackId, String response)
	{
		responder.sendMore(callbackId);
		responder.sendMore("");
		responder.send(response, 0);
	}
}