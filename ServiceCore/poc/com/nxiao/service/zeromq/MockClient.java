package com.nxiao.service.zeromq;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class MockClient 
{
	public static void main(String[] args) throws UnknownHostException, InterruptedException
	{
		Context context = ZMQ.context(1);
        Socket sender = context.socket(ZMQ.DEALER);
		String destination = String.format("tcp://localhost:%s", 9001);
        sender.connect(destination);
        
        InetAddress ret = InetAddress.getLocalHost();
        JSONObject req = new JSONObject();
        req.put("callback_host", ret.getHostAddress());
        req.put("callback_port", "9003");
        req.put("cusip", "6497877H5");
        req.put("request", "test");
        
        sender.sendMore("id_123");
        sender.send(req.toString());
	System.out.println("sent: " + req.toString());
        
        /*while(true)
        {
        	sender.send(req.toString());
        	System.out.println("sent: " + req.toString());
        	Thread.sleep(1000);
        }*/
	}
}
