package com.nxiao.service.zeromq;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.nxiao.service.exception.ServiceStartUpException;

public class BasicZeroMqClient
{
	private Logger logger = Logger.getLogger(this.getClass());

	Context context;
	Socket requester;

	public BasicZeroMqClient(String targetAddress, int targetPort) throws ServiceStartUpException
	{
		context = ZMQ.context(1);
		// Socket to talk to server
		requester = context.socket(ZMQ.REQ);
		requester.connect(String.format("tcp://%s:%d", targetAddress, targetPort));
		logger.info("ZeroMq client launched and connected.");
	}

	public String sendRequest(String requestId, String request) throws Exception
	{
		if (requestId == null || request == null)
			throw new Exception("Invalid empty request.");

		// send request
		requester.sendMore(requestId);
		requester.send(request, 0);

		// get response
		byte[] reqId = requester.recv(0);
		byte[] reply = requester.recv(0);

		return new String(reply);
	}

	public void close()
	{
		requester.close();
		context.term();
	}
}
