package com.nxiao.service.core.data;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.nxiao.service.core.exception.ServiceStartUpException;

public class DataCache
{
	private Logger logger = Logger.getLogger(this.getClass());

	DataEnginePool redisEnginePool;

	public DataCache(int redisEnginePoolSize, String redisHost, String serviceName) throws ServiceStartUpException
	{
		try
		{
			logger.info("Initializing Data Cache...");
			RedisEngine redisEngine = createRedisEngine(redisHost, serviceName);
			this.redisEnginePool = new DataEnginePool(redisEnginePoolSize, redisEngine);
			logger.info("Data Cache initialized.");
		}
		catch (Exception e)
		{
			throw new ServiceStartUpException("Failed to initialize data cache. Reason: " + e.getMessage(), e);
		}
	}

	private RedisEngine createRedisEngine(String redisHost, String serviceName) throws IOException
	{
		RedisEngine redisEngine = new RedisEngine(redisHost, serviceName);
		return redisEngine;
	}
	
	private RedisEngine getRedisEngine()
	{
		return (RedisEngine)redisEnginePool.getDataEngine();
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
}
