package com.nxiao.service.core;

import org.json.simple.JSONObject;

public class TaskResponse
{
	static final String KEY = "key";
	static final String DATA = "data";
	static final String ERROR = "error";
	static final String SUCCESS = "success";

	JSONObject response = new JSONObject();

	public TaskResponse()
	{
		response.put(SUCCESS, true);
	}

	public TaskResponse(String key, String data)
	{
		response.put(KEY, key);
		response.put(DATA, data);
		response.put(SUCCESS, true);
	}

	public TaskResponse(String key, String data, String error)
	{
		response.put(KEY, key);
		response.put(DATA, data);
		response.put(ERROR, error);
		boolean isSuccess = error == null || error.isEmpty()? true : false;
		response.put(SUCCESS, isSuccess);
	}

	public TaskResponse(String error)
	{
		response.put(ERROR, error);
		response.put(SUCCESS, false);
	}
	
	public void setKey(String key)
	{
		response.put(KEY, key);
	}
	
	public void setData(String data)
	{
		response.put(DATA, data);
	}
	
	public void setError(String error)
	{
		response.put(ERROR, error);
		response.put(SUCCESS, false);
	}

	public JSONObject getJsonResponse()
	{
		return response;
	}

	public String getStringResponse()
	{
		return response.toString();
	}
}
