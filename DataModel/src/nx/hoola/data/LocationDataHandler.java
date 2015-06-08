package nx.hoola.data;

import java.util.List;

public class LocationDataHandler extends BasicData
{
	public LocationDataHandler(RedisEngine redis)
	{
		super(redis);
	}

	/**
	 * @param locationId
	 */
	public void addToAllLocationsList(String locationId)
	{
		String locationsList = KeySchema.AllLocations.getKey(null);
		redis().addMemberOnSet(locationsList, locationId);
	}

	/**
	 * @param locationId
	 * @return
	 */
	public boolean isValidLocation(String locationId)
	{
		String locationsList = KeySchema.AllLocations.getKey(null);
		return redis().isMemberOnSet(locationsList, locationId);
	}
	
	/**
	 * @return
	 */
	public List<String> getAllLocations()
	{
		String locationsList = KeySchema.AllLocations.getKey(null);
		return redis().getAllMembersOnSet(locationsList);
	}

	/**
	 * @param locationId
	 * @param eventId
	 */
	public void addEventToLocation(String locationId, String eventId)
	{
		if (isEventPublic(eventId))
		{
			String eventsByLocation = KeySchema.LocationOfActivePublicEvents.getKey(locationId);
			redis().addMemberOnSet(eventsByLocation, eventId);
		}
	}
	
	/**
	 * @param locationId
	 * @return
	 */
	public List<String> getAllEventsByLocation(String locationId)
	{
		String eventsByLocation = KeySchema.LocationOfActivePublicEvents.getKey(locationId);
		return redis().getAllMembersOnSet(eventsByLocation);
	}
}
