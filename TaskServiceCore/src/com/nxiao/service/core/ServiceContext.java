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
	
	public <T> String validate(Class<T> paramClass)
	{
		for(T param : paramClass.getEnumConstants())
		{
			if(!contextMap.containsKey(param))
			{
				return "Missing param [" + param + "] in context";
			}
		}
		return "";
	}
}
