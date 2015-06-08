package nx.hoola.datamodel;

import java.util.List;

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
		String expEventList = KeySchema.TimeOfExpireEvents.getKey(date);
		redis().addMemberOnSet(expEventList, eventId);
	}
	
	/**
	 * @param date
	 * @return
	 */
	public List<String> gatAllExpiredEventsByDate(String date)
	{
		String expEventList = KeySchema.TimeOfExpireEvents.getKey(date);
		return redis().getAllMembersOnSet(expEventList);
	}
	
	/**
	 * @param date
	 * @param eventId
	 */
	public void addPublicEventToDate(String date, String eventId)
	{
		if(isEventPublic(eventId))
		{
			String pubEventList = KeySchema.TimeOfActivePublicEvents.getKey(date);
			redis().addMemberOnSet(pubEventList, eventId);
		}
	}
	
	/**
	 * @param date
	 * @return
	 */
	public List<String> getAllPublicEventsOfDate(String date)
	{
		String pubEventList = KeySchema.TimeOfActivePublicEvents.getKey(date);
		return redis().getAllMembersOnSet(pubEventList);
	}
}
