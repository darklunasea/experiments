package com.darklunasea.dataengine;

import redis.clients.jedis.Jedis;

public class RedisEngine 
{	
	private Jedis jedis;
	
	public RedisEngine(String host)
	{
		jedis = new Jedis(host);
	}
	
	public void close()
	{
		if(jedis != null)
		{
			jedis.close();
		}
	}
	
	public void addByCusip(String cusip, String data)
	{
		jedis.set(cusip, data);
	}
	
	public String getByCusip(String cusip)
	{
		String data = jedis.get(cusip);
		return data;
	}
}
