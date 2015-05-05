package com.nxiao.service.configuration;

import java.io.IOException;
import java.util.Properties;

public class ConnectionConfig extends BaseConfig
{
	private static final String propertyFile = "connection.properties";

	private static Properties prop;

	public static String getAsString(String key) throws IOException 
	{
		return getProp(prop, propertyFile).getProperty(key);
	}
	
	public static Integer getAsInt(String key) throws IOException 
	{
		return Integer.valueOf(getProp(prop, propertyFile).getProperty(key));
	}
}
