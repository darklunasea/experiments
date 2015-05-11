package com.nxiao.service.core;

public enum ServiceContext
{
	PORT_GROUP_START(9000),
	;
	
	private Object value;	
	private <T> ServiceContext(T value)
	{
		this.value = value;
	}
	
	public <T> T value()
	{
		return (T)value;
	}
}
