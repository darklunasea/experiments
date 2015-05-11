package com.nxiao.service.core.publisher;

public interface IPublisher
{
	void post(String topic, String message) throws Exception;
}
