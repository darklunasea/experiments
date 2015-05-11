package com.nxiao.service.core;

public interface ITask
{
	String getCallbackId();
	TaskType getTaskType();
	String getTaskContent();
	String getKey();
}
