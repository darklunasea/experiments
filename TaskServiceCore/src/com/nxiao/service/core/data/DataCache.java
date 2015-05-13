package com.nxiao.service.core.data;

import org.apache.log4j.Logger;

import com.nxiao.service.core.exception.ServiceStartUpException;

public class DataCache
{
	private Logger logger = Logger.getLogger(this.getClass());

	RedisEngine redisEngine;

	public DataCache(int redisEnginePoolSize, String redisHost) throws ServiceStartUpException
	{
		try
		{
			logger.info("Initializing Data Cache...");
			redisEngine = new RedisEngine(redisHost);
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

	public String getDataByKey(String schema, String key)
	{
		String data = getRedisEngine().getData(schema, key);
		return data;
	}
	
	public void setDataByKey(String schema, String key, String data)
	{
		getRedisEngine().setData(schema, key, data);
	}
	
	public boolean existKey(String schema, String key)
	{
		return getRedisEngine().exist(schema, key);
	}
}
