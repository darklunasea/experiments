package com.nxiao.service.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil 
{
	protected Properties load(String propertyFile) throws IOException 
	{
		InputStream inputStream = getClass().getResourceAsStream(propertyFile);

		if (inputStream != null) 
		{
			Properties prop = new Properties();
			prop.load(inputStream);
			inputStream.close();
			return prop;
		} 
		else 
		{
			throw new IOException("property file '" + propertyFile
					+ "' not found in the classpath");
		}
	}
}
