package com.nxiao.service;

import org.json.simple.JSONObject;

public class QueryClientTest
{
	public static void main(String[] args) throws Exception
	{
		String host = "localhost";
		Integer port = 9001;

		BasicZeroMqClient client = new BasicZeroMqClient(host, port, "test_query_client1");
		
		int testRound = 5000;
		for(int i = 0; i < testRound; i++)
		{
			for(int j = 0; j < 20; j++)
			{
				String key = String.valueOf(j);
				sendReq(key, client);
			}
		}		

		client.close();
	}

	private static void sendReq(String key, BasicZeroMqClient client) throws Exception
	{
		// construct request
		JSONObject req = new JSONObject();
		req.put("service", "data_service");
		req.put("task", "query");
		req.put("key", key);		

		// send request
		System.out.println("Sending request: " + req.toString());
		String reply = client.sendRequest(req.toString());
		System.out.println("Received response: " + reply);
	}
}
