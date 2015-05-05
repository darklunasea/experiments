package com.nxiao.service.dataservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.nxiao.service.exception.ServiceStartUpException;
import com.nxiao.service.zeromq.BasicZeroMqBroker;

public class QueryHost extends Thread
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	static final int THREAD_POOL_SIZE = 10;
	static final int HOST_CLIENT_PORT = 9001;
	static final String HOST_ADDRESS = "localhost";
	static final int HOST_PROCESSOR_PORT = 9002;
	
	BasicZeroMqBroker zeroMqBroker;
	ExecutorService threadPoolExecutor;
	QueryProcessor processor;

	public QueryHost() throws ServiceStartUpException 
	{
		logger.info("Creating Security Query Host...");		
		
		zeroMqBroker = new BasicZeroMqBroker(HOST_CLIENT_PORT, HOST_PROCESSOR_PORT);
		threadPoolExecutor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		processor = new QueryProcessor(HOST_ADDRESS, HOST_PROCESSOR_PORT);
		
		logger.info("Security Query Host created.");
	}	
	
	@Override
	public void run()
	{
		//start the mq broker
		zeroMqBroker.start();
		//create workers
		for(int i = 0; i < THREAD_POOL_SIZE; i++)
		{
			threadPoolExecutor.execute(processor.newSession());
		}
		
		logger.info("===== Security Query Host is started successfully =====");
	}
}
