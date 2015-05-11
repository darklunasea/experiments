package com.nxiao.service.core;

import com.nxiao.service.core.exception.ServiceProcessException;

public interface ITaskProcessor
{
	void processTask(ITask task) throws ServiceProcessException;
}
