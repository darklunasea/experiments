package com.tradeweb.direct.service.securitydataservice;

import com.nxiao.service.dataservice.DataService;
import com.nxiao.service.exception.ServiceStartUpException;

public class SecurityDataServicePoc 
{
	public static void main(String[] args) throws ServiceStartUpException
	{		
		DataService service = new DataService();
		service.start();
	}
}
