package nx.hoola.data.redis;

import java.util.List;

import nx.hoola.data.scheme.KeyScheme;

public class TimeDataHandler extends BasicData
{
	public TimeDataHandler(RedisEngine redis)
	{
		super(redis);
	}

	/**
	 * @param date
	 * @param eventId
	 */
	public void addEventByExpiredDate(String date, String eventId)
	{
		String expEventList = KeyScheme.TimeOfExpireEvents.getKey(date);
		redis().addMemberOnSet(expEventList, eventId);
	}

	/**
	 * @param date
	 * @return
	 */
	public List<String> gatAllExpiredEventsByDate(String date)
	{
		String expEventList = KeyScheme.TimeOfExpireEvents.getKey(date);
		return redis().getAllMembersOnSet(expEventList);
	}

	/**
	 * @param date
	 * @param eventId
	 */
	public void addPublicEventToDate(String date, String eventId, boolean isPublic)
	{
		if (isPublic)
		{
			String pubEventList = KeyScheme.TimeOfActivePublicEvents.getKey(date);
			redis().addMemberOnSet(pubEventList, eventId);
		}
	}

	/**
	 * @param date
	 * @return
	 */
	public List<String> getAllPublicEventsOfDate(String date)
	{
		String pubEventList = KeyScheme.TimeOfActivePublicEvents.getKey(date);
		return redis().getAllMembersOnSet(pubEventList);
	}
}
