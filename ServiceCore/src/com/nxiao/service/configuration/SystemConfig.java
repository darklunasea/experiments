package com.nxiao.service.configuration;

import java.io.IOException;
import java.util.Properties;

public class SystemConfig  extends BaseConfig
{
	private static final String propertyFile = "system.properties";

	private static Properties prop;

	public static String getAsString(String key) throws IOException 
	{
		return getProp(prop, propertyFile).getProperty(key);
	}
}
