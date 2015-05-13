package com.nxiao.service.core;

import java.util.HashMap;
import java.util.Map;

public class ServiceContext
{
	private Map<IServiceParam,Object> contextMap;
	
	public ServiceContext()
	{
		contextMap = new HashMap<IServiceParam,Object>();
	}
	
	public <T> void set(IServiceParam parameter, T value)
	{
		contextMap.put(parameter, value);
	}
	
	public <T> T get(IServiceParam parameter)
	{
		return (T)contextMap.get(parameter);
	}
}
