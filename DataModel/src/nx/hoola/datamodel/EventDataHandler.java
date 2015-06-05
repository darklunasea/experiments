package nx.hoola.datamodel;

import java.util.List;

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
	
	public void addEventLocations(String eventId, String... locationIds)
	{
		String eventLocations = KeySchema.EventLocations.getKey(eventId);
		redis().addMembersOnSet(eventLocations, locationIds);
	}
	
	public void removeEventLocations(String eventId, String... locationIds)
	{
		String eventLocations = KeySchema.EventLocations.getKey(eventId);
		redis().removeMembersOnSet(eventLocations, locationIds);
	}
	
	public List<String> getEventLocations(String eventId)
	{
		String eventLocations = KeySchema.EventLocations.getKey(eventId);
		return redis().getAllMembersOnSet(eventLocations);
	}
	
	public void setEventPublic(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isPublicFlagOffset = Long.valueOf(KeySchema.IsPublicEventOffset.getKey(null));
		redis().setBitOnKey(eventStatus, isPublicFlagOffset, true);
	}
	
	public void setEventPrivate(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isPublicFlagOffset = Long.valueOf(KeySchema.IsPublicEventOffset.getKey(null));
		redis().setBitOnKey(eventStatus, isPublicFlagOffset, false);
	}
	
	public void setEventActive(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isActiveFlagOffset = Long.valueOf(KeySchema.IsActiveEventOffset.getKey(null));
		redis().setBitOnKey(eventStatus, isActiveFlagOffset, true);
	}
	
	public void setEventInactive(String eventId)
	{
		String eventStatus = KeySchema.EventStatus.getKey(eventId);
		Long isActiveFlagOffset = Long.valueOf(KeySchema.IsActiveEventOffset.getKey(null));
		redis().setBitOnKey(eventStatus, isActiveFlagOffset, false);
	}
	
	public void addEventInterestTags(String eventId, String... interestIds)
	{
		String eventInterestTags = KeySchema.EventInterestTags.getKey(eventId);
		redis().addMembersOnSet(eventInterestTags, interestIds);
	}
	
	public void removeEventInterestTags(String eventId, String... interestIds)
	{
		String eventInterestTags = KeySchema.EventInterestTags.getKey(eventId);
		redis().removeMembersOnSet(eventInterestTags, interestIds);
	}
	
	public List<String> getAllEventInterestTags(String eventId)
	{
		String eventInterestTags = KeySchema.EventInterestTags.getKey(eventId);
		return redis().getAllMembersOnSet(eventInterestTags);
	}
	
	public void addEventEditors(String eventId, String... personIds)
	{
		String eventEditors = KeySchema.EventEditors.getKey(eventId);
		redis().addMembersOnSet(eventEditors, personIds);
	}
	
	public void removeEventEditors(String eventId, String... personIds)
	{
		String eventEditors = KeySchema.EventEditors.getKey(eventId);
		redis().removeMembersOnSet(eventEditors, personIds);
	}
	
	public List<String> getAllEventEditors(String eventId)
	{
		String eventEditors = KeySchema.EventEditors.getKey(eventId);
		return redis().getAllMembersOnSet(eventEditors);
	}
	
	
}
