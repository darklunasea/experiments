package com.nxiao.service.core;

public class Task implements ITask
{
	TaskType taskType;
	String key;
	String taskContent;
	String callbackId;

	public Task(String callbackId, TaskType taskType, String taskContent, String key)
	{
		this.callbackId = callbackId;
		this.taskType = taskType;
		this.taskContent = taskContent;
		this.key = key;
	}

	public String getCallbackId()
	{
		return callbackId;
	}

	public TaskType getTaskType()
	{
		return taskType;
	}

	public String getTaskContent()
	{
		return taskContent;
	}

	public String getKey()
	{
		return key;
	}
}
