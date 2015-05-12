package com.nxiao.service.core.data;

import redis.clients.jedis.Jedis;


public class RedisEngine implements IDataEngine
{	
	Jedis jedis;
	String host;
	String serviceKey;
	
	public RedisEngine(String host, String serviceKey)
	{
		this.host = host;
		this.serviceKey = serviceKey;
		jedis = new Jedis(host);
	}
	
	public IDataEngine newSession() throws Exception
	{
		return new RedisEngine(this.host, this.serviceKey);
	}
	
	public void close()
	{
		if(jedis != null)
		{
			jedis.close();
		}
	}
	
	public boolean exist(String key)
	{
		return jedis.hexists(serviceKey, key);
	}
	
	public void setData(String key, String data)
	{
		jedis.hset(serviceKey, key, data);
	}
	
	public String getData(String key)
	{
		return jedis.hget(serviceKey, key);
	}
	
	public void publish(String channel, String message)
	{
		jedis.publish(channel, message);
	}
}
