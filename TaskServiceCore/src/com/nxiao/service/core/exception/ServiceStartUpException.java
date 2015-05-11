package com.nxiao.service.core.exception;

public class ServiceStartUpException extends Exception
{
	public ServiceStartUpException(String errMsg) {
		super(errMsg);
	}

	public ServiceStartUpException(String errMsg, Exception e) {
		super(errMsg, e);
	}

}
