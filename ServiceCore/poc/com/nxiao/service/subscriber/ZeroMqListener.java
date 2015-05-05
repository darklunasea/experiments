package com.nxiao.service.subscriber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;

import com.nxiao.service.configuration.ConnectionConfig;
import com.twdirect.api.messages.OfferMessages.OfferReqMessage;
import com.twdirect.api.messages.OfferReqApi;

public class ZeroMqListener {
    
    public static void main(String[] args) throws Exception
    {
    	int zeroMqPort = ConnectionConfig.getAsInt("Offer_Ecn_ZeroMq_Pub_Port");
    	ZeroMqListener listener = new ZeroMqListener(zeroMqPort);
    	listener.start();
    }
    
    private Logger logger = Logger.getLogger(this.getClass());

	private ZMQ.Socket sub;

	public ZeroMqListener(int port) 
	{
		init(port);
	}

	public void start() throws Exception 
	{
	    long ms = System.currentTimeMillis();
	    long msgCt = 0;
	    while (!Thread.currentThread ().isInterrupted ()) {
		msgCt++;
	            byte[] contents = sub.recv(0);
	            //OfferReqMessage rec = OfferReqMessage.parseFrom(contents);
	            //System.out.println("Received : OfferId = " + rec.getOfferID());
	            if(System.currentTimeMillis() - ms >= 1000)
	            {
	        	System.out.println(msgCt);
	        	ms = System.currentTimeMillis();
	        	msgCt = 0;
	            }
	        }
	}

	public byte[] convertToProtobufMessage(String message) throws IOException 
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		OfferReqMessage reqMsg = OfferReqApi.FromContributorString(message);
		reqMsg.writeTo(stream);
		
		return stream.toByteArray();
	}

	private void init(int port) 
	{
		logger.info("Initializing ZeroMq subscriber...");
		String connect_str = String.format("tcp://localhost:%d", port);

		ZMQ.Context context = ZMQ.context(1);
		sub = context.socket(ZMQ.PULL);
		sub.connect(connect_str);

		logger.info("ZeroMq subscriber started on port: " + port);
	}
}
