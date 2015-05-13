package com.nxiao.service.core.data;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisEngine implements IDataEngine
{
	String host;
	JedisPool pool;

	public RedisEngine(String host)
	{
		this.host = host;
		pool = new JedisPool(new JedisPoolConfig(), host);
		pool.getResource();
	}

	public IDataEngine newSession() throws Exception
	{
		return new RedisEngine(this.host);
	}

	public void close()
	{
		if (pool != null)
		{
			pool.destroy();
		}
	}

	public boolean exist(String schema, String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.hexists(schema, key);
		}
	}

	public void setData(String schema, String key, String data)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.hset(schema, key, data);
		}
	}

	public String getData(String schema, String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.hget(schema, key);
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
