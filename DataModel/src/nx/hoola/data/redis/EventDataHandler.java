package nx.hoola.data.redis;

import java.util.List;

import nx.hoola.data.scheme.KeyScheme;

public class EventDataHandler extends BasicData
{
	public EventDataHandler(RedisEngine redis)
	{
		super(redis);
	}

	/**
	 * @return
	 */
	public Long getNewEventId()
	{
		String idCounter = KeyScheme.EventIdCounter.getKey(null);
		return redis().getNextIdByKey(idCounter);
	}
	
	/**
	 * @param eventId
	 * @param isPublic
	 * @param isActive
	 */
	public void addToAllPublicEventsList(String eventId, boolean isPublic, boolean isActive)
	{
		if (isPublic && isActive)
		{
			String actPubEventsList = KeyScheme.AllActivePublicEventsList.getKey(null);
			redis().addMemberOnSet(actPubEventsList, eventId);
		}
	}

	/**
	 * @param eventId
	 * @param locationIds
	 */
	public void addEventLocations(String eventId, String... locationIds)
	{
		String eventLocations = KeyScheme.EventLocations.getKey(eventId);
		redis().addMembersOnSet(eventLocations, locationIds);
	}

	/**
	 * @param eventId
	 * @param locationIds
	 */
	public void removeEventLocations(String eventId, String... locationIds)
	{
		String eventLocations = KeyScheme.EventLocations.getKey(eventId);
		redis().removeMembersOnSet(eventLocations, locationIds);
	}

	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getEventLocations(String eventId)
	{
		String eventLocations = KeyScheme.EventLocations.getKey(eventId);
		return redis().getAllMembersOnSet(eventLocations);
	}

	/**
	 * @param eventId
	 * @param interestIds
	 */
	public void addEventInterestTags(String eventId, String... interestIds)
	{
		String eventInterestTags = KeyScheme.EventInterestTags.getKey(eventId);
		redis().addMembersOnSet(eventInterestTags, interestIds);
	}

	/**
	 * @param eventId
	 * @param interestIds
	 */
	public void removeEventInterestTags(String eventId, String... interestIds)
	{
		String eventInterestTags = KeyScheme.EventInterestTags.getKey(eventId);
		redis().removeMembersOnSet(eventInterestTags, interestIds);
	}

	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllEventInterestTags(String eventId)
	{
		String eventInterestTags = KeyScheme.EventInterestTags.getKey(eventId);
		return redis().getAllMembersOnSet(eventInterestTags);
	}

	/**
	 * @param eventId
	 * @param personIds
	 */
	public void addEventEditors(String eventId, String... personIds)
	{
		String eventEditors = KeyScheme.EventEditors.getKey(eventId);
		redis().addMembersOnSet(eventEditors, personIds);
	}

	/**
	 * @param eventId
	 * @param personIds
	 */
	public void removeEventEditors(String eventId, String... personIds)
	{
		String eventEditors = KeyScheme.EventEditors.getKey(eventId);
		redis().removeMembersOnSet(eventEditors, personIds);
	}

	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllEventEditors(String eventId)
	{
		String eventEditors = KeyScheme.EventEditors.getKey(eventId);
		return redis().getAllMembersOnSet(eventEditors);
	}

	/**
	 * @param eventId
	 * @param eventIds
	 */
	public void addLinkedEvents(String eventId, String... eventIds)
	{
		String linkedEvents = KeyScheme.EventLinkedEvents.getKey(eventId);
		redis().addMembersOnSet(linkedEvents, eventIds);
	}

	/**
	 * @param eventId
	 * @param eventIds
	 */
	public void removeLinkedEvents(String eventId, String... eventIds)
	{
		String linkedEvents = KeyScheme.EventLinkedEvents.getKey(eventId);
		redis().removeMembersOnSet(linkedEvents, eventIds);
	}

	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllLinkedEvents(String eventId)
	{
		String linkedEvents = KeyScheme.EventLinkedEvents.getKey(eventId);
		return redis().getAllMembersOnSet(linkedEvents);
	}

	/**
	 * @param eventId
	 * @param personId
	 */
	public void addAttendPerson(String eventId, String personId)
	{
		String attendPersons = KeyScheme.EventAttendedByPersons.getKey(eventId);
		redis().addMemberOnSet(attendPersons, personId);
	}

	/**
	 * @param eventId
	 * @param personId
	 */
	public void removeAttendPerson(String eventId, String personId)
	{
		String attendPersons = KeyScheme.EventAttendedByPersons.getKey(eventId);
		redis().removeMemberOnSet(attendPersons, personId);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public Long getNumberOfAttendPersons(String eventId)
	{
		String attendPersons = KeyScheme.EventAttendedByPersons.getKey(eventId);
		return redis().getNumberOfMembersOnSet(attendPersons);
	}

	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllAttendPersons(String eventId)
	{
		String attendPersons = KeyScheme.EventAttendedByPersons.getKey(eventId);
		return redis().getAllMembersOnSet(attendPersons);
	}

	/**
	 * @param eventId
	 * @param personId
	 */
	public void addMarkFavPerson(String eventId, String personId)
	{
		String favPersons = KeyScheme.EventFavoritedByPersons.getKey(eventId);
		redis().addMemberOnSet(favPersons, personId);
	}

	/**
	 * @param eventId
	 * @param personId
	 */
	public void removeMarkFavPerson(String eventId, String personId)
	{
		String favPersons = KeyScheme.EventFavoritedByPersons.getKey(eventId);
		redis().removeMemberOnSet(favPersons, personId);
	}

	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllFavoritePersons(String eventId)
	{
		String favPersons = KeyScheme.EventFavoritedByPersons.getKey(eventId);
		return redis().getAllMembersOnSet(favPersons);
	}

	/**
	 * @param eventId
	 * @param incrVal
	 */
	public void increaseEventScore(String eventId, Long incrVal)
	{
		String eventScore = KeyScheme.EventBasicScore.getKey(eventId);
		redis().increaseKeyLongValue(eventScore, incrVal);
	}

	/**
	 * @param eventId
	 * @param decrVal
	 */
	public void decreaseEventScore(String eventId, Long decrVal)
	{
		String eventScore = KeyScheme.EventBasicScore.getKey(eventId);
		redis().decreaseKeyLongValue(eventScore, decrVal);
	}

	/**
	 * @param eventId
	 * @return
	 */
	public Long getEventScore(String eventId)
	{
		String eventScore = KeyScheme.EventBasicScore.getKey(eventId);
		return redis().getKeyLongValue(eventScore);
	}

	/**
	 * @param eventId
	 * @param incrVal
	 */
	public void increaseEventRating(String eventId, Long incrVal)
	{
		String eventRating = KeyScheme.EventRatingScore.getKey(eventId);
		redis().increaseKeyLongValue(eventRating, incrVal);
	}

	/**
	 * @param eventId
	 * @param decrVal
	 */
	public void decreaseEventRating(String eventId, Long decrVal)
	{
		String eventRating = KeyScheme.EventRatingScore.getKey(eventId);
		redis().decreaseKeyLongValue(eventRating, decrVal);
	}

	/**
	 * @param eventId
	 * @return
	 */
	public Long getEventRating(String eventId)
	{
		String eventRating = KeyScheme.EventRatingScore.getKey(eventId);
		return redis().getKeyLongValue(eventRating);
	}
}
