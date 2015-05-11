package com.nxiao.service.core.exception;

public class ServiceProcessException extends Exception
{
	public ServiceProcessException(String errMsg) 
	{
		super(errMsg);
	}
	
	public ServiceProcessException(String errMsg, Exception e) 
	{
		super(errMsg, e);
	}

}
