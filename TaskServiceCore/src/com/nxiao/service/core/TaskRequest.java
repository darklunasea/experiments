package com.nxiao.service.core;

import org.json.simple.JSONObject;

public class TaskRequest
{
	static final String SERVICE = "service";
	static final String TASK = "task";
	static final String TABLE = "table";
	static final String KEY = "key";
	static final String DATA = "data";

	JSONObject request = new JSONObject();

	public TaskRequest()
	{
		
	}

	public TaskRequest(String service, String task, String table)
	{
		request.put(SERVICE, service);
		request.put(TASK, task);
		request.put(TABLE, table);
	}
	
	public void setKey(String key)
	{
		request.put(KEY, key);
	}
	
	public void setData(String data)
	{
		request.put(DATA, data);
	}

	public JSONObject getJsonRequest()
	{
		return request;
	}

	public String getStringRequest()
	{
		return request.toString();
	}
}
