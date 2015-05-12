package com.nxiao.service.core;

import com.nxiao.service.processor.ITaskProcessor;

public class TaskHandler
{
	TaskType taskType;
	ITaskReceiver taskReceiver;
	ITaskProcessor taskProcessor;

	public TaskHandler(TaskType taskType, ITaskReceiver taskReceiver, ITaskProcessor taskProcessor)
	{
		this.taskType = taskType;
		this.taskReceiver = taskReceiver;
		this.taskProcessor = taskProcessor;
	}

	public TaskType getTaskType()
	{
		return taskType;
	}

	public ITaskReceiver getTaskReceiver()
	{
		return taskReceiver;
	}

	public ITaskProcessor getTaskProcessor()
	{
		return taskProcessor;
	}

}
