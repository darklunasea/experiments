package com.nxiao.service.configuration;

import java.io.IOException;
import java.util.Properties;

public class BaseConfig 
{
	protected static Properties getProp(Properties prop, String propertyFile) throws IOException 
	{
		if (prop == null) 
		{
			prop = new PropertyUtil().load(propertyFile);
		}
		return prop;
	}
}
