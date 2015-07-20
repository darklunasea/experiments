package com.nx.zmqserver;

public class ZmqServerPoc
{
	public static void main(String[] args) throws Exception
	{
		int client_port = 15000;
		int worker_port = 15001;
		ZmqServer server = new ZmqServer(client_port, worker_port);

		IMsgHandler handler1 = new ExampleMsgHandler1();
		IMsgHandler handler2 = new ExampleMsgHandler2();

		server.addMsgHandler(handler1, 2);	// handler1 serves for service1. scale for 2 sessions
		server.addMsgHandler(handler2, 3);	// hanlder2 serves for service2. scale for 3 sessions

		server.start();
	}
}

class ExampleMsgHandler1 implements IMsgHandler
{
	@Override
	public String getServiceName()
	{
		return "service1";
	}

	@Override
	public String process(String data) throws Exception
	{
		return "handler1: " + data;
	}
}

class ExampleMsgHandler2 implements IMsgHandler
{
	@Override
	public String getServiceName()
	{
		return "service2";
	}

	@Override
	public String process(String data) throws Exception
	{
		return "handler2: " + data;
	}
}