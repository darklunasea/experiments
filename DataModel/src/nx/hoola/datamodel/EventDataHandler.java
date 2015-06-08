package nx.hoola.datamodel;

import java.util.List;

public class EventDataHandler extends BasicData
{
	public EventDataHandler(RedisEngine redis)
	{
		super(redis);
	}
	
	/**
	 * @param eventId
	 */
	public void addToAllPublicEventsList(String eventId)
	{
		if(isEventPublic(eventId))
		{
			String actPubEventsList = KeySchema.AllActivePublicEventsList.getKey(null);
			redis().addMemberOnSet(actPubEventsList, eventId);
		}		
	}
	
	/**
	 * @param eventId
	 * @param locationIds
	 */
	public void addEventLocations(String eventId, String... locationIds)
	{
		String eventLocations = KeySchema.EventLocations.getKey(eventId);
		redis().addMembersOnSet(eventLocations, locationIds);
	}
	
	/**
	 * @param eventId
	 * @param locationIds
	 */
	public void removeEventLocations(String eventId, String... locationIds)
	{
		String eventLocations = KeySchema.EventLocations.getKey(eventId);
		redis().removeMembersOnSet(eventLocations, locationIds);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getEventLocations(String eventId)
	{
		String eventLocations = KeySchema.EventLocations.getKey(eventId);
		return redis().getAllMembersOnSet(eventLocations);
	}
	
	/**
	 * @param eventId
	 */
	public void setEventPublic(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isPublicFlagOffset = Long.valueOf(KeySchema.IsPublicEventOffset.getKey(null));
		redis().setBitOnKey(eventStatus, isPublicFlagOffset, true);
	}
	
	/**
	 * @param eventId
	 */
	public void setEventPrivate(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isPublicFlagOffset = Long.valueOf(KeySchema.IsPublicEventOffset.getKey(null));
		redis().setBitOnKey(eventStatus, isPublicFlagOffset, false);
	}
	
	/**
	 * @param eventId
	 */
	public void setEventActive(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isActiveFlagOffset = Long.valueOf(KeySchema.IsActiveEventOffset.getKey(null));
		redis().setBitOnKey(eventStatus, isActiveFlagOffset, true);
	}
	
	/**
	 * @param eventId
	 */
	public void setEventInactive(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isActiveFlagOffset = Long.valueOf(KeySchema.IsActiveEventOffset.getKey(null));
		redis().setBitOnKey(eventStatus, isActiveFlagOffset, false);
	}
	
	/**
	 * @param eventId
	 * @param interestIds
	 */
	public void addEventInterestTags(String eventId, String... interestIds)
	{
		String eventInterestTags = KeySchema.EventInterestTags.getKey(eventId);
		redis().addMembersOnSet(eventInterestTags, interestIds);
	}
	
	/**
	 * @param eventId
	 * @param interestIds
	 */
	public void removeEventInterestTags(String eventId, String... interestIds)
	{
		String eventInterestTags = KeySchema.EventInterestTags.getKey(eventId);
		redis().removeMembersOnSet(eventInterestTags, interestIds);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllEventInterestTags(String eventId)
	{
		String eventInterestTags = KeySchema.EventInterestTags.getKey(eventId);
		return redis().getAllMembersOnSet(eventInterestTags);
	}
	
	/**
	 * @param eventId
	 * @param personIds
	 */
	public void addEventEditors(String eventId, String... personIds)
	{
		String eventEditors = KeySchema.EventEditors.getKey(eventId);
		redis().addMembersOnSet(eventEditors, personIds);
	}
	
	/**
	 * @param eventId
	 * @param personIds
	 */
	public void removeEventEditors(String eventId, String... personIds)
	{
		String eventEditors = KeySchema.EventEditors.getKey(eventId);
		redis().removeMembersOnSet(eventEditors, personIds);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllEventEditors(String eventId)
	{
		String eventEditors = KeySchema.EventEditors.getKey(eventId);
		return redis().getAllMembersOnSet(eventEditors);
	}
	
	/**
	 * @param eventId
	 * @param eventIds
	 */
	public void addLinkedEvents(String eventId, String... eventIds)
	{
		String linkedEvents = KeySchema.EventLinkedEvents.getKey(eventId);
		redis().addMembersOnSet(linkedEvents, eventIds);
	}
	
	/**
	 * @param eventId
	 * @param eventIds
	 */
	public void removeLinkedEvents(String eventId, String... eventIds)
	{
		String linkedEvents = KeySchema.EventLinkedEvents.getKey(eventId);
		redis().removeMembersOnSet(linkedEvents, eventIds);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllLinkedEvents(String eventId)
	{
		String linkedEvents = KeySchema.EventLinkedEvents.getKey(eventId);
		return redis().getAllMembersOnSet(linkedEvents);
	}
	
	/**
	 * @param eventId
	 * @param personId
	 */
	public void addAttendPerson(String eventId, String personId)
	{
		String attendPersons = KeySchema.EventAttendedByPersons.getKey(eventId);
		redis().addMemberOnSet(attendPersons, personId);
	}
	
	/**
	 * @param eventId
	 * @param personId
	 */
	public void removeAttendPerson(String eventId, String personId)
	{
		String attendPersons = KeySchema.EventAttendedByPersons.getKey(eventId);
		redis().removeMemberOnSet(attendPersons, personId);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllAttendPersons(String eventId)
	{
		String attendPersons = KeySchema.EventAttendedByPersons.getKey(eventId);
		return redis().getAllMembersOnSet(attendPersons);
	}
	
	/**
	 * @param eventId
	 * @param personId
	 */
	public void addFavoritePerson(String eventId, String personId)
	{
		String favPersons = KeySchema.EventFavoritedByPersons.getKey(eventId);
		redis().addMemberOnSet(favPersons, personId);
	}
	
	/**
	 * @param eventId
	 * @param personId
	 */
	public void removeFavoritePerson(String eventId, String personId)
	{
		String favPersons = KeySchema.EventFavoritedByPersons.getKey(eventId);
		redis().removeMemberOnSet(favPersons, personId);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public List<String> getAllFavoritePersons(String eventId)
	{
		String favPersons = KeySchema.EventFavoritedByPersons.getKey(eventId);
		return redis().getAllMembersOnSet(favPersons);
	}
	
	/**
	 * @param eventId
	 * @param incrVal
	 */
	public void increaseEventScore(String eventId, Long incrVal)
	{
		String eventScore = KeySchema.EventBasicScore.getKey(eventId);
		redis().increaseKeyLongValue(eventScore, incrVal);
	}
	
	/**
	 * @param eventId
	 * @param decrVal
	 */
	public void decreaseEventScore(String eventId, Long decrVal)
	{
		String eventScore = KeySchema.EventBasicScore.getKey(eventId);
		redis().decreaseKeyLongValue(eventScore, decrVal);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public Long getEventScore(String eventId)
	{
		String eventScore = KeySchema.EventBasicScore.getKey(eventId);
		return redis().getKeyLongValue(eventScore);
	}
	
	/**
	 * @param eventId
	 * @param incrVal
	 */
	public void increaseEventRating(String eventId, Long incrVal)
	{
		String eventRating = KeySchema.EventRatingScore.getKey(eventId);
		redis().increaseKeyLongValue(eventRating, incrVal);
	}
	
	/**
	 * @param eventId
	 * @param decrVal
	 */
	public void decreaseEventRating(String eventId, Long decrVal)
	{
		String eventRating = KeySchema.EventRatingScore.getKey(eventId);
		redis().decreaseKeyLongValue(eventRating, decrVal);
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public Long getEventRating(String eventId)
	{
		String eventRating = KeySchema.EventRatingScore.getKey(eventId);
		return redis().getKeyLongValue(eventRating);
	}
}
