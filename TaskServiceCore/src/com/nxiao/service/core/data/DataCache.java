package com.nxiao.service.core.data;

import org.apache.log4j.Logger;

import com.nxiao.service.core.exception.ServiceStartUpException;

public class DataCache
{
	private Logger logger = Logger.getLogger(this.getClass());

	RedisEngine redisEngine;

	public DataCache(int redisEnginePoolSize, String redisHost, String serviceName) throws ServiceStartUpException
	{
		try
		{
			logger.info("Initializing Data Cache...");
			redisEngine = new RedisEngine(redisHost, serviceName);
			logger.info("Data Cache initialized.");
		}
		catch (Exception e)
		{
			throw new ServiceStartUpException("Failed to initialize data cache. Reason: " + e.getMessage(), e);
		}
	}
	
	protected RedisEngine getRedisEngine()
	{
		return redisEngine;
	}

	public String getDataByKey(String key)
	{
		String data = getRedisEngine().getData(key);
		return data;
	}
	
	public void setDataByKey(String key, String data)
	{
		getRedisEngine().setData(key, data);
	}
	
	public boolean existKey(String key)
	{
		return getRedisEngine().exist(key);
	}
}
