package nx.server.zmq;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class ZmqProxy implements Runnable
{
	final static Logger logger = Logger.getLogger(ZmqProxy.class);

	final int MAX_GET_FREE_WORKER_RETRY_TIME = 5;
	
	Gson gson;
	
	boolean isStop;

	Socket clientSocket;
	Socket workerSocket;
	Poller poll;

	ZmqServiceRegistration serviceReg;

	public ZmqProxy(int clientRequestPort, int workerResponsePort)
	{
		isStop = false;
		gson = new GsonBuilder().create();
		serviceReg = new ZmqServiceRegistration();

		initZmq(clientRequestPort, workerResponsePort);
	}

	protected void initZmq(int clientRequestPort, int workerResponsePort)
	{
		Context zmqContext = ZMQ.context(1);
		clientSocket = zmqContext.socket(ZMQ.ROUTER);
		workerSocket = zmqContext.socket(ZMQ.ROUTER);

		clientSocket.setLinger(0);
		workerSocket.setLinger(0);

		clientSocket.setReceiveTimeOut(100);
		workerSocket.setReceiveTimeOut(100);

		clientSocket.bind("tcp://*:" + clientRequestPort);
		workerSocket.bind("tcp://*:" + workerResponsePort);

		poll = new Poller(2);
		poll.register(clientSocket, Poller.POLLIN);
		poll.register(workerSocket, Poller.POLLIN);

		logger.info("Zmq proxy binds to client port [" + clientRequestPort + "] and worker port ["
				+ workerResponsePort + "]");
	}

	public void stop()
	{
		isStop = true;
	}

	@Override
	public void run()
	{
		logger.info("Zmq proxy started.");
		try
		{
			while (!isStop && !Thread.currentThread().isInterrupted())
			{
				if (poll.poll(100) == -1)
				{
					continue;
				}
				if (poll.pollin(0))
				{
					// pass client request to worker
					byte[] clientId = clientSocket.recv(0);
					byte[] req = clientSocket.recv(0);

					logger.debug("Client: clientId = " + new String(clientId) + ", req = " + new String(req));

					handleRequest(clientId, req);
				}
				if (poll.pollin(1))
				{
					// pass worker response to client
					byte[] workerId = workerSocket.recv(0);
					byte[] resp = workerSocket.recv(0);

					logger.debug("Worker: workerId = " + new String(workerId) + ", resp = " + new String(resp));

					handleResponse(workerId, resp);
				}
			}
		}
		finally
		{
			close();
			logger.info("Zmq proxy stopped.");
		}
		
	}
	
	protected void close()
	{
		clientSocket.close();
		workerSocket.close();
	}
	
	private void handleRequest(byte[] clientId, byte[] req)
	{
		if (req == null)
		{
			// broken request, abort
			return;
		}

		ZmqClientRequest request;
		try
		{
			request = gson.fromJson(new String(req), ZmqClientRequest.class);
		}
		catch (JsonSyntaxException e)
		{
			logger.error("Invalid request from client [" + new String(clientId)
					+ "]. Format should be ZmqClientRequest. Request: " + new String(req));
			String resp = createErrorResponse("Invalid request format in Json.");
			sendToClient(clientId, resp);
			return;
		}

		String service = request.getService();
		if (!serviceReg.isServiceRegistered(service))
		{
			// no worker registered for this service
			// send error to client
			String resp = createErrorResponse("Requested service [" + service + "] is not available.");
			sendToClient(clientId, resp);
			return;
		}

		// dispatch task to a free worker
		byte[] workerId = null;
		int retry = 0;
		while (retry < MAX_GET_FREE_WORKER_RETRY_TIME)
		{
			workerId = serviceReg.getFreeWorker(service);
			if (workerId != null)
			{
				break;
			}
			else
			{
				// all the workers are busy, wait for a while
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					//
				}
				retry++;
				continue;
			}
		}

		if (workerId == null)
		{
			// notify client that this service is busy
			String resp = createErrorResponse("Requested service [" + service + "] is busy. Please retry later.");
			sendToClient(clientId, resp);
			return;
		}

		// send request to the worker
		ZmqWorkerRequest workerReq = new ZmqWorkerRequest();
		workerReq.setClientId(clientId);
		workerReq.setData(request.getData());
		sendToWorker(workerId, gson.toJson(workerReq));
	}
	
	private void handleResponse(byte[] workerId, byte[] resp)
	{
		ZmqWorkerResponse msg;
		try
		{
			msg = gson.fromJson(new String(resp), ZmqWorkerResponse.class);
		}
		catch (JsonSyntaxException e)
		{
			logger.error("Invalid response from worker [" + new String(workerId)
					+ "]. Format should be ZmqWorkerResponse. Response: " + new String(resp));
			return;
		}

		serviceReg.onWorkerResponse(msg.getService(), workerId);		

		byte[] clientId = msg.getClientId();
		if (clientId == null || clientId.length == 0)
		{
			return;
		}

		// send to client
		ZmqClientResponse response = new ZmqClientResponse();
		response.setData(msg.getData());
		response.setError(msg.getError());
		String responseStr = gson.toJson(response);

		sendToClient(clientId, responseStr);
	}
	
	private String createErrorResponse(String error)
	{
		ZmqClientResponse resp = new ZmqClientResponse();
		resp.setError(error);
		return gson.toJson(resp);
	}

	private void sendToClient(byte[] clientId, String response)
	{
		clientSocket.send(clientId, ZMQ.SNDMORE);
		clientSocket.send(response, 0);
	}

	private void sendToWorker(byte[] workerId, String request)
	{
		workerSocket.send(workerId, ZMQ.SNDMORE);
		workerSocket.send(request, 0);
	}
}
