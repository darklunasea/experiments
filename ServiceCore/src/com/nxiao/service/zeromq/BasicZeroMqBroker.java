package com.nxiao.service.zeromq;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class BasicZeroMqBroker extends Thread
{
	private Logger logger = Logger.getLogger(this.getClass());

	Context context;
	Socket frontend;
	Socket backend;
	Poller items;

	public BasicZeroMqBroker(int frontengPort, int backengPort)
	{
		logger.info("Creating ZeroMq broker...");

		// Init ZeroMq context and socket
		context = ZMQ.context(1);

		frontend = context.socket(ZMQ.ROUTER);
		backend = context.socket(ZMQ.DEALER);

		frontend.bind("tcp://*:" + frontengPort);
		backend.bind("tcp://*:" + backengPort);

		// Initialize poll set
		items = new Poller(2);
		items.register(frontend, Poller.POLLIN);
		items.register(backend, Poller.POLLIN);

		logger.info("ZeroMq broker created.");
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
		logger.info("ZeroMq broker started.");
		
		boolean more = false;
		byte[] message;

		// Switch messages between sockets
		while (!Thread.currentThread().isInterrupted())
		{
			// poll and memorize multipart detection
			items.poll();

			if (items.pollin(0))
			{
				while (true)
				{
					// receive message
					message = frontend.recv(0);
					more = frontend.hasReceiveMore();

					// Broker it
					backend.send(message, more ? ZMQ.SNDMORE : 0);
					if (!more)
					{
						break;
					}
				}
			}
			if (items.pollin(1))
			{
				while (true)
				{
					// receive message
					message = backend.recv(0);
					more = backend.hasReceiveMore();
					// Broker it
					frontend.send(message, more ? ZMQ.SNDMORE : 0);
					if (!more)
					{
						break;
					}
				}
			}
		}
	}
}
