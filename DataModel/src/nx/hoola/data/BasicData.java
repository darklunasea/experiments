package nx.hoola.data;

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
		String allPersonList = KeySchema.AllPersonsList.getKey(null);
		return redis().isMemberOnSet(allPersonList, personId);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public boolean isEventPublic(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isPublicFlagOffset = Long.valueOf(KeySchema.IsPublicEventOffset.getKey(null));
		return redis().getBitOnKey(eventStatus, isPublicFlagOffset);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public boolean isEventActive(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isActiveFlagOffset = Long.valueOf(KeySchema.IsActiveEventOffset.getKey(null));
		return redis().getBitOnKey(eventStatus, isActiveFlagOffset);
	}
}
