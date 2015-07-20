package com.nx.zmqserver;

public interface IMsgHandler
{
	String getServiceName();

	String process(String data) throws Exception;
}
