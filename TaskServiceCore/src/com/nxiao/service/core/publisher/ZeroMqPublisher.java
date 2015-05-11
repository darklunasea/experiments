package com.nxiao.service.core.publisher;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class ZeroMqPublisher implements IPublisher
{
	Socket publisher;
	
	public ZeroMqPublisher(int port)
	{		
		Context context = ZMQ.context(1);
        this.publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:" + port);
	}

	@Override
	public void post(String topic, String message) throws Exception
	{
		publisher.sendMore(topic);
		publisher.send(message);
	}
}
