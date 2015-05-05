package com.nxiao.service.publisher;

import org.apache.log4j.Logger;

import com.nxiao.service.configuration.ConnectionConfig;
import com.tradeweb.twd.corej.jni.tervela.TervelaPublisher;

public class BasicTervelaPublisher implements IPublisher{

	private Logger logger = Logger.getLogger(this.getClass());
	
	private TervelaPublisher pub;
	private int pubHandle;

	public BasicTervelaPublisher(String publisher, String publisherName) throws Exception
	{
		pubHandle = init(publisher, publisherName);
	}
	
	@Override
	public void post(byte[] message) throws Exception
	{
		int res = pub.publisherPostInByte(pubHandle, message, message.length);
		if(res < 0)
		{
			throw new Exception("Error posting message to Tervela. Error code: " + res);
		}
	}
	
	private int init(String publisher, String publisherName) throws Exception{
		pub = new TervelaPublisher();
		
		logger.info("Initializing tervela publisher...");
		
		// init tervela
		String tervelaConfigFile = ConnectionConfig.getAsString("Tervela_Pub_Config");
		int res = pub.initializeTervela(
				tervelaConfigFile, publisherName,
				0, 'Y');
		if(res < 0)
		{
			throw new Exception("Error initializing Tervela. Error code: " + res);
		}

		// start publisher
		int pubHandle = pub.startPublisher(publisher, false);
		if(pubHandle < 0)
		{
			pub.terminateTervela();
			throw new Exception("Error starting Tervela. pubHandle returned: " + pubHandle);			
		}
		
		logger.info("Tervela publisher started. pubHandle = " + pubHandle);
		
		return pubHandle;
	}
}
