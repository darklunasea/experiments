package com.nxiao.service.core.query;

import org.json.simple.JSONObject;

public class QueryResponse
{
	static final String KEY = "key";
	static final String DATA = "data";
	static final String ERROR = "error";

	JSONObject response = new JSONObject();

	public QueryResponse()
	{
	}

	public QueryResponse(String key, String data)
	{
		response.put(KEY, key);
		response.put(DATA, data);
	}

	public QueryResponse(String key, String data, String error)
	{
		response.put(KEY, key);
		response.put(DATA, data);
		response.put(ERROR, error);
	}

	public QueryResponse(String error)
	{
		response.put(ERROR, error);
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
