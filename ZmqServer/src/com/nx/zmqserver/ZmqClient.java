package com.nx.zmqserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twd.common.ipc.IpcZmqDealer;

public class ZmqClient
{
	Gson gson;
	IpcZmqDealer client;
	
	public ZmqClient(String clientId, String host, int port, Integer timeout)
	{
		gson = new GsonBuilder().create();
		client = new IpcZmqDealer(clientId.getBytes(), host, port, timeout);
	}
	
	public void send(ZmqClientRequest req)
	{
		client.send(gson.toJson(req));
	}
	
	public String receive()
	{
		return client.receiveStr();
	}
	
	public void close()
	{
		client.close();
	}
}
