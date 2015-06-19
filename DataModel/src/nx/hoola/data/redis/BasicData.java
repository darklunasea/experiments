package nx.hoola.data.redis;

import nx.hoola.data.scheme.KeyScheme;

class BasicData
{
	private RedisEngine redis;
	
	public BasicData(RedisEngine redis)
	{
		this.redis = redis;
	}
	
	protected RedisEngine redis()
	{
		return redis;
	}

	/**
	 * @param personId
	 * @return
	 */
	public boolean isPersonExist(String personId)
	{
		String allPersonList = KeyScheme.AllPersonsList.getKey(null);
		return redis().isMemberOnSet(allPersonList, personId);
	}
}
