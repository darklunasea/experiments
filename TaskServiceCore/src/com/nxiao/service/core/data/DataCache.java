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

	public String getDataByKeyInTable(String table, String key)
	{
		String data = getRedisEngine().getDataInTable(table, key);
		return data;
	}
	
	public void setDataByKeyInTable(String table, String key, String data)
	{
		getRedisEngine().setDataInTable(table, key, data);
	}
	
	public boolean existKeyInTable(String table, String key)
	{
		return getRedisEngine().existInTable(table, key);
	}
	
	public String getNewIdInTable(String table)
	{
		return String.valueOf(getRedisEngine().getNextIdInTable(table));
	}
}
