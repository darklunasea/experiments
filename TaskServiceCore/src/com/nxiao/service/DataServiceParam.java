package com.nxiao.service;

import com.nxiao.service.core.IServiceParam;

public enum DataServiceParam implements IServiceParam
{
	RedisConnPoolSize,
	RedisHost,
	QueryRequestPort,
	UpdateRequestPort,
	QueryResponsePort,
	UpdateResponsePort,
	UpdatePublishPort,
}
