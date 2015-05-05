package com.nxiao.service.dataengine;

import com.google.protobuf.InvalidProtocolBufferException;
import com.twdirect.api.messages.SecurityMessages.SecurityMasterMessage;

import redis.clients.jedis.Jedis;

public class TestJedis {
	
	public static void main(String[] args) throws Exception
	{
		//test();
		//clearValue("6497877H5");
		getValueOfProtobuf("6497877H5");
	}
	
	public static void test()
	{
		Jedis jedis = new Jedis("172.16.43.90");
		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		System.out.println(value);
		jedis.close();
	}
	
	public static void getValue(String key)
	{
		Jedis jedis = new Jedis("172.16.43.90");
		String value = jedis.get(key);
		System.out.println(value);
		jedis.close();
	}
	
	public static void getValueOfProtobuf(String key) throws InvalidProtocolBufferException
	{
		Jedis jedis = new Jedis("172.16.43.90");
		byte[] value = jedis.get(key.getBytes());
		SecurityMasterMessage msg = SecurityMasterMessage.parseFrom(value);
		System.out.println(msg.toString());
		jedis.close();
	}
	
	public static void clearValue(String key)
	{
		Jedis jedis = new Jedis("172.16.43.90");
		jedis.del(key);
		jedis.close();
	}
}
