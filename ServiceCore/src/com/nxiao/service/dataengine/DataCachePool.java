package com.nxiao.service.dataengine;


public class DataCachePool 
{	
	IDataCache[] dataCachePool;
	int poolSize;
	int currentDataCacheIndex = 0;
	
	public DataCachePool(int poolSize, IDataCache dataCache) throws Exception
	{
		this.poolSize = poolSize;
		dataCachePool = new IDataCache[poolSize];
		for(int i = 0; i < poolSize; i++)
		{
			dataCachePool[i] = dataCache.newSession();
		}
	}
	
	public IDataCache getNextDataCache()
	{
		if(currentDataCacheIndex == poolSize)
		{
			currentDataCacheIndex = 0;
		}
		IDataCache cache = dataCachePool[currentDataCacheIndex];
		currentDataCacheIndex++;
		return cache;
	}
	
	public IDataCache getDefaultDataCache()
	{
		return dataCachePool[0];
	}
}
