package com.nxiao.service.dataengine;

import java.io.IOException;

import com.nxiao.service.configuration.ConnectionConfig;

import redis.clients.jedis.Jedis;

public class RedisEngine 
{	
	protected Jedis jedis;
	protected String host;
	protected int port;
	
	public RedisEngine() throws IOException
	{
		String host = ConnectionConfig.getAsString("Redis_Host");
		Integer port = ConnectionConfig.getAsInt("Redis_Port");
		jedis = new Jedis(host, port);
	}
	
	public RedisEngine(String host, int port)
	{
		this.host = host;
		this.port = port;
		jedis = new Jedis(host, port);
	}
	
	public void close()
	{
		if(jedis != null)
		{
			jedis.close();
		}
	}
	
	public void setData(String key, String data)
	{
		jedis.set(key, data);
	}
	
	public void setData(byte[] key, byte[] data)
	{
		jedis.set(key, data);
	}
	
	public String getStringData(String key)
	{
		return jedis.get(key);
	}
	
	public byte[] getByteData(byte[] key)
	{
		return jedis.get(key);
	}
}
