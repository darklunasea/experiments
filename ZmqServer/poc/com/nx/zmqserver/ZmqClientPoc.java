package com.nx.zmqserver;

public class ZmqClientPoc
{
	public static void main(String[] args)
	{
		String clientId = "test_client2";		
		ZmqClient client = new ZmqClient(clientId, "localhost", 15000, 1000);
		
		String service = "service2";
		String request = "request2";		
		
		ZmqClientRequest req = new ZmqClientRequest();
		req.setService(service);
		req.setData(request);
		
		client.send(req);
		String ret = client.receive();
		
		System.out.println(ret);
		
		client.close();
	}
}
