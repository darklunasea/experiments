package com.nxiao.service.processor;

import com.nxiao.service.core.ITask;
import com.nxiao.service.core.exception.ServiceProcessException;
import com.nxiao.service.core.exception.ServiceStartUpException;

public interface ITaskProcessor
{
	void processTask(ITask task) throws ServiceProcessException;
	ITaskProcessor newSession() throws ServiceStartUpException;
}
