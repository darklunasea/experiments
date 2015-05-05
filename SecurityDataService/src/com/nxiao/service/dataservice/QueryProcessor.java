package com.nxiao.service.dataservice;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.nxiao.service.dataengine.DataCachePool;
import com.nxiao.service.exception.ServiceProcessException;
import com.nxiao.service.exception.ServiceStartUpException;
import com.nxiao.service.zeromq.BasicZeroMqWorker;

public class QueryProcessor extends BasicZeroMqWorker
{	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static final int DATA_CACHE_POOL_SIZE = 10;
	
	String brokerBackendHost;
	int brokerBackendPort;
	protected DataCachePool dataCachePool;
	protected DataCache dataCache;

	public QueryProcessor(String brokerBackendHost, int brokerBackendPort) throws ServiceStartUpException
	{
		super(brokerBackendHost, brokerBackendPort);
		
		logger.info("Creating Query Processor...");
		
		this.brokerBackendHost = brokerBackendHost;
		this.brokerBackendPort = brokerBackendPort;
		dataCache = new DataCache();
		initDataCachePool(dataCache);
		
		logger.info("Query Processor created.");
	}
	
	public QueryProcessor(String brokerBackendHost, int brokerBackendPort, DataCache dataCache)
	{
		super(brokerBackendHost, brokerBackendPort);		
		this.dataCache = dataCache;
	}
	
	public QueryProcessor newSession()
	{
		DataCache nextDataCache = (DataCache) dataCachePool.getNextDataCache();
		return new QueryProcessor(brokerBackendHost, brokerBackendPort, nextDataCache);
	}
	
	private void initDataCachePool(DataCache dataCache) throws ServiceStartUpException 
	{
		logger.info("Initializing Data Cache Pool...");
		try 
		{
			dataCachePool = new DataCachePool(DATA_CACHE_POOL_SIZE, dataCache);
			logger.info("Data Cache Pool initialized.");
		} 
		catch (Exception e) 
		{
			throw new ServiceStartUpException("Unable to initialize data cache pool. Reason: " + e.getMessage(), e);
		}
	}
	
	@Override
	public JSONObject process(JSONObject req) throws ServiceProcessException
	{
		logger.debug("Procesing query request. Request: " + req.toString());
		
		String key = (String) req.get("key");
		if(key == null || key.isEmpty())
		{
			throw new ServiceProcessException("Cannot find key in request.");
		}	
		
		String data;
		try 
		{
			data = dataCache.getDataByKey(key);			
		} 
		catch (Exception e) 
		{
			String err = "Failed to get data from data cache by key: " + key + ". Reason: " + e.getMessage();
			logger.error(err, e);
			throw new ServiceProcessException(err, e);
		}
		
		JSONObject response = new JSONObject();
		response.put("key", key);
		response.put("data", data);
		
		return response;
	}
}
