package com.nxiao.service.core.data;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisEngine implements IDataEngine
{
	String host;
	String serviceKey;
	JedisPool pool;

	public RedisEngine(String host, String serviceKey)
	{
		this.host = host;
		this.serviceKey = serviceKey;
		pool = new JedisPool(new JedisPoolConfig(), host);
		pool.getResource();
	}

	public IDataEngine newSession() throws Exception
	{
		return new RedisEngine(this.host, this.serviceKey);
	}

	public void close()
	{
		if (pool != null)
		{
			pool.destroy();
		}
	}

	public boolean exist(String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.hexists(serviceKey, key);
		}
	}

	public void setData(String key, String data)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.hset(serviceKey, key, data);
		}
	}

	public String getData(String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.hget(serviceKey, key);
		}
	}

	public void publish(String channel, String message)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.publish(channel, message);
		}
	}
}
