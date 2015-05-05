package com.nxiao.service.dataservice;

import org.apache.log4j.Logger;

import com.nxiao.service.exception.ServiceStartUpException;

public class DataService extends Thread
{	
	private Logger logger = Logger.getLogger(this.getClass());
	
	QueryHost queryHost;
	
	public DataService() throws ServiceStartUpException
	{		
		logger.info("Initializing Data Service...");
		
		queryHost = new QueryHost();	
		logger.info("Query Host has been initialized.");
		
		logger.info("----- Data Service has been initialized successfully -----");
	}
	
	@Override
	public void run()
	{
		logger.info("Starting up Data Service...");
				
		//Start the Query Host
		queryHost.start();
		logger.info("Query Host is started.");		
	}
}
