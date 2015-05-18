package com.nxiao.service.core.data;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisEngine implements IDataEngine
{
	static final String ID_KEY = "$id";
	
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

	public boolean existInTable(String table, String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.hexists(table, key);
		}
	}

	public void setDataInTable(String table, String key, String data)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.hset(table, key, data);
		}
	}

	public String getDataInTable(String table, String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.hget(table, key);
		}
	}

	public void publish(String channel, String message)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.publish(channel, message);
		}
	}
	
	public Long getNextIdInTable(String table)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.hincrBy(table, ID_KEY, 1);
		}
	}
	
	public Long getNextId(String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.incr(key);
		}
	}
}
