package com.nxiao.service;

import redis.clients.jedis.Jedis;

public class TestJedis {
	
	public static void main(String[] args) throws Exception
	{
		//test();
		//clearValue("6497877H5");
		setValue("1", "test1");
		setValue("3", "test3");
		setValue("5", "test5");
		setValue("7", "test7");
		setValue("9", "test9");
	}
	
	public static void setValue(String key, String val)
	{
		Jedis jedis = new Jedis("172.16.43.90");
		jedis.hset("test_service", key, val);
		jedis.close();
	}
	
	public static void getValue(String key)
	{
		Jedis jedis = new Jedis("172.16.43.90");
		String value = jedis.get(key);
		System.out.println(value);
		jedis.close();
	}
	
	public static void clearValue(String key)
	{
		Jedis jedis = new Jedis("172.16.43.90");
		jedis.del(key);
		jedis.close();
	}
}
