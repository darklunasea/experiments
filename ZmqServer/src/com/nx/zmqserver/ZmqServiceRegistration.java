package com.nx.zmqserver;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class ZmqServiceRegistration
{
	final static Logger logger = Logger.getLogger(ZmqServiceRegistration.class);
	int rrIndex;

	// service registration
	ConcurrentHashMap<String, ConcurrentHashMap<byte[], Boolean>> serviceReg;
	Object lock;

	public ZmqServiceRegistration()
	{
		rrIndex = 0;
		lock = new Object();
		serviceReg = new ConcurrentHashMap<String, ConcurrentHashMap<byte[], Boolean>>();
	}

	public boolean isServiceRegistered(String service)
	{
		synchronized (lock)
		{
			return serviceReg.containsKey(service);
		}
	}

	public byte[] getFreeWorker(String service)
	{
		synchronized (lock)
		{
			ConcurrentHashMap<byte[], Boolean> workerReg = serviceReg.get(service);
			for (byte[] id : workerReg.keySet())
			{
				if (workerReg.get(id))
				{
					serviceReg.get(service).put(id, false);
					return id;
				}
			}
		}
		return null;
	}

	public void onWorkerResponse(String service, byte[] workerId)
	{
		synchronized (lock)
		{
			if (!serviceReg.containsKey(service))
			{
				// register the worker under service and mark it as free
				ConcurrentHashMap<byte[], Boolean> workerReg = new ConcurrentHashMap<byte[], Boolean>();
				workerReg.put(workerId, true);
				serviceReg.put(service, workerReg);
				logger.info("Worker [" + new String(workerId) + "] register to service [" + service + "]");
			}
			else
			{
				if (!serviceReg.get(service).containsKey(workerId))
				{
					logger.info("Worker [" + new String(workerId) + "] register to service [" + service + "]");
				}
				// if first time register, mark it as free
				// in any case, if a worker is responding, mark it as free
				serviceReg.get(service).put(workerId, true);
			}
		}
	}

}
