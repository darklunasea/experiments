package com.nx.zmqserver;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.twd.common.ipc.IpcZmqDealer;

public class ZmqWorker implements Runnable
{
	final static Logger logger = Logger.getLogger(ZmqWorker.class);

	Gson gson;

	String service;
	IMsgHandler handler;
	boolean isStop;

	IpcZmqDealer dealer;
	String workerId;

	public ZmqWorker(int workerResponsePort, IMsgHandler handler, int index)
	{
		this.handler = handler;
		this.service = handler.getServiceName();
		isStop = false;
		gson = new GsonBuilder().create();
		workerId = handler.getServiceName() + "_" + index;
		dealer = new IpcZmqDealer(workerId.getBytes(), "localhost", workerResponsePort, 100);
	}

	public void stop()
	{
		isStop = true;
	}

	@Override
	public void run()
	{
		try
		{
			logger.info("Zmq worker (" + workerId + ") started.");

			register();
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e1)
			{
				//
			}

			while (!isStop && !Thread.currentThread().isInterrupted())
			{
				try
				{
					String msg = dealer.receiveStr();	
					
					if (msg == null || msg.isEmpty())
					{
						Thread.sleep(100);
						continue;
					}

					ZmqWorkerRequest req;
					try
					{
						req = gson.fromJson(msg, ZmqWorkerRequest.class);
					}
					catch (JsonSyntaxException e)
					{
						logger.error("Invalid worker request format. Request: " + msg);
						return;
					}

					try
					{
						String ret = handler.process(req.getData());
						sendResponse(req.getClientId(), ret);
					}
					catch (Exception e)
					{
						sendError(req.getClientId(), e.getMessage());
					}
				}
				catch (Exception e)
				{
					logger.error("Worker (" + workerId + "): Error processing request. Reason: " + e.getMessage());
				}
			}
		}
		finally
		{
			dealer.close();
			logger.info("Zmq worker (" + workerId + ") stopped.");
		}
	}

	private void register()
	{
		ZmqWorkerResponse resp = new ZmqWorkerResponse();
		resp.setService(service);
		dealer.send(gson.toJson(resp));
	}

	private void sendResponse(byte[] clientId, String response)
	{
		ZmqWorkerResponse resp = new ZmqWorkerResponse();
		resp.setService(service);
		resp.setClientId(clientId);
		resp.setData(response);
		dealer.send(gson.toJson(resp));
	}

	private void sendError(byte[] clientId, String error)
	{
		ZmqWorkerResponse resp = new ZmqWorkerResponse();
		resp.setService(service);
		resp.setClientId(clientId);
		resp.setError(error);
		dealer.send(gson.toJson(resp));
	}
}
