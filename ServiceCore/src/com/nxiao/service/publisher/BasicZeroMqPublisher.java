package com.nxiao.service.publisher;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;

public class BasicZeroMqPublisher implements IPublisher {

	private Logger logger = Logger.getLogger(this.getClass());

	private ZMQ.Socket pub;

	public BasicZeroMqPublisher(int port) {
		init(port);
	}

	@Override
	public void post(byte[] message) throws Exception {
		pub.send(message, 0);
	}

	private void init(int port) {
		logger.info("Initializing ZeroMq publisher...");
		String connect_str = String.format("tcp://*:%d", port);

		ZMQ.Context context = ZMQ.context(1);
		pub = context.socket(ZMQ.PUSH);
		pub.bind(connect_str);

		logger.info("ZeroMq publisher started on port: " + port);
	}

}
