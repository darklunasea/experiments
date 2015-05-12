package com.nxiao.service;

import org.json.simple.JSONObject;

public class BasicZeroMqClientPoc
{
	public static void main(String[] args) throws Exception
	{
		String host = "localhost";
		Integer port = 9001;

		BasicZeroMqClient client = new BasicZeroMqClient(host, port, "test_query_client");
		
		int testRound = 100;
		for(int i = 0; i < testRound; i++)
		{
			for(int j = 0; j < 10; j++)
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
		req.put("key", key);

		// send request
		System.out.println("Sending request: " + req.toString());
		String reply = client.sendRequest(req.toString());
		System.out.println("Received response: " + reply);
	}
}
