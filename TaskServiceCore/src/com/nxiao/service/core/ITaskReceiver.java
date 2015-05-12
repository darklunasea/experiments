package com.nxiao.service.core;

public interface ITaskReceiver extends Runnable
{
	void setTaskManager(TaskManager taskManager);
	void start();
}
