package com.nxiao.service.zeromq;

import com.nxiao.service.zeromq.BasicZeroMqBroker;

public class BasicZeroMqHostPoc 
{
	public static void main(String[] args)
	{
		BasicZeroMqBroker host = new BasicZeroMqBroker(9001, 9002);
		host.start();
	}
}
