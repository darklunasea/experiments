package nx.hoola.data.redis;

import java.util.List;

import nx.hoola.data.scheme.KeyScheme;

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
		String locationsList = KeyScheme.AllLocations.getKey(null);
		redis().addMemberOnSet(locationsList, locationId);
	}

	/**
	 * @param locationId
	 * @return
	 */
	public boolean isValidLocation(String locationId)
	{
		String locationsList = KeyScheme.AllLocations.getKey(null);
		return redis().isMemberOnSet(locationsList, locationId);
	}

	/**
	 * @return
	 */
	public List<String> getAllLocations()
	{
		String locationsList = KeyScheme.AllLocations.getKey(null);
		return redis().getAllMembersOnSet(locationsList);
	}
	
	/**
	 * @param locationId
	 * @param eventId
	 * @param isPublic
	 */
	public void addPublicEventToLocation(String locationId, String eventId, boolean isPublic)
	{
		if (isPublic)
		{
			String eventsByLocation = KeyScheme.LocationOfActiveEvents.getKey(locationId);
			redis().addMemberOnSet(eventsByLocation, eventId);
		}
	}

	/**
	 * @param locationId
	 * @return
	 */
	public List<String> getAllPublicEventsByLocation(String locationId)
	{
		String eventsByLocation = KeyScheme.LocationOfActiveEvents.getKey(locationId);
		return redis().getAllMembersOnSet(eventsByLocation);
	}
}
