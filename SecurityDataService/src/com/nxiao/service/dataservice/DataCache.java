package com.nxiao.service.dataservice;

import org.apache.log4j.Logger;

import com.nxiao.service.configuration.ConnectionConfig;
import com.nxiao.service.dataengine.IDataCache;
import com.nxiao.service.dataengine.RedisEngine;
import com.nxiao.service.exception.ServiceStartUpException;

public class DataCache implements IDataCache
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	protected RedisEngine redisEngine;

	public DataCache() throws ServiceStartUpException
	{
		redisEngine = getNewRedisEngine();
	}
	
	public DataCache(RedisEngine redisEngine) 
	{
		this.redisEngine = redisEngine;
	}

	public DataCache newSession() throws Exception 
	{
		return new DataCache(getNewRedisEngine());
	}
	
	public RedisEngine getNewRedisEngine() throws ServiceStartUpException
	{
		try 
		{
			String redisHost = ConnectionConfig.getAsString("Redis_Host");
			Integer redisPort = ConnectionConfig.getAsInt("Redis_Port");
			RedisEngine newRedisEngine = new RedisEngine(redisHost, redisPort);
			return newRedisEngine;
		} 
		catch (Exception e) 
		{
			throw new ServiceStartUpException("Failed to get redis engine. Reason: " + e.getMessage(), e);
		}	
	}

	public String getDataByKey(String key) throws Exception 
	{
		String data = redisEngine.getStringData(key);
		return data;
	}

	public void setDataByKey(String key, String data) throws Exception 
	{	
		redisEngine.setData(key, data);
	}
}
