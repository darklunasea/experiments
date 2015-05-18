package com.nxiao.service.processor;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

public class Validator
{
	Set<String> validationFields;

	public Validator()
	{
		this.validationFields = new HashSet<String>();
	}
	
	public void addValidateFields(String... fields)
	{
		for(String field : fields)
		{
			validationFields.add(field);
		}
	}
	
	public String validate(JSONObject json)
	{
		StringBuilder validationError = new StringBuilder();
		for (String field : validationFields)
		{
			if (!json.containsKey(field))
			{
				validationError.append("Cannot find '" + field + "' in request.");
			}
		}
		return validationError.toString();
	}
}
