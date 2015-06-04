package nx.hoola.datamodel;

public class EventDataHandler extends BasicData
{
	public EventDataHandler(RedisEngine redis)
	{
		super(redis);
	}
	
	public void addToActivePublicEventsList(String eventId)
	{
		String actPubEventsList = KeySchema.AllActivePublicEventsList.getKey(null);
		redis().addMemberOnSet(actPubEventsList, eventId);
	}
}
