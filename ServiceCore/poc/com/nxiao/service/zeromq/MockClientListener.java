package com.nxiao.service.zeromq;

import java.net.UnknownHostException;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.protobuf.InvalidProtocolBufferException;

public class MockClientListener 
{
	public static void main(String[] args) throws UnknownHostException, InterruptedException, InvalidProtocolBufferException
	{
        Context context = ZMQ.context(1);
        Socket listener = context.socket(ZMQ.ROUTER);
        listener.bind("tcp://*:" + 9003);
        
        while(true)
        {
        	listener.recv(0);
        	byte[] id = listener.recv(0);
        	byte[] resp = listener.recv(0);
        	System.out.println("received: " + new String(resp));
        }
	}
}
