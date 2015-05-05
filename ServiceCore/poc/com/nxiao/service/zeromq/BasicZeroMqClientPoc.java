package com.nxiao.service.zeromq;

import javax.xml.bind.DatatypeConverter;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.nxiao.service.zeromq.BasicZeroMqClient;
import com.twdirect.api.messages.SecurityMessages.SecurityMasterMessage;

public class BasicZeroMqClientPoc
{
	public static void main(String[] args) throws Exception
	{
		String host;
		Integer port;
		String cusip;
		if (args == null || args.length == 0)
		{
			host = "localhost";
			port = 9001;
			cusip = "06406JDA0";
		}
		else
		{
			host = args[0];
			port = Integer.valueOf(args[1]);
			cusip = args[2];
		}

		BasicZeroMqClient client = new BasicZeroMqClient(host, port);

		// construct request
		JSONObject req = new JSONObject();
		req.put("cusip", cusip);

		// send request
		System.out.println("Sending request: " + req.toString());
		String reply = client.sendRequest("reqId_123", req.toString());
		System.out.println("Received response: " + reply);

		JSONObject jobj = (JSONObject) JSONValue.parse(reply);
		String data = (String) jobj.get("data");
		SecurityMasterMessage msg = SecurityMasterMessage.parseFrom(DatatypeConverter.parseBase64Binary(data));
		System.out.println("Data converted from protobuf: \n" + msg.toString());

		client.close();
	}
}
