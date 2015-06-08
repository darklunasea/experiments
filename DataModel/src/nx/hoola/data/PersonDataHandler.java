package nx.hoola.data;

import java.util.List;

import redis.clients.jedis.Tuple;

public class PersonDataHandler extends BasicData
{
	public PersonDataHandler(RedisEngine redis)
	{
		super(redis);
	}
	
	/**
	 * @param personId
	 */
	public void addToAllPersonsList(String personId)
	{
		String allPersonsList = KeySchema.AllPersonsList.getKey(null);
		redis().addMemberOnSet(allPersonsList, personId);
	}

	/**
	 * @param personId
	 * @param friendId
	 * @return are the two persons friends
	 */
	public boolean isFriend(String personId, String friendId)
	{
		String friendList = KeySchema.PersonFriends.getKey(personId);
		return redis().isMemberOnSet(friendList, friendId);
	}

	/**
	 * @param personId
	 * @param friendId
	 */
	public void addFriend(String personId, String friendId)
	{
		if (isPersonExist(friendId))
		{
			String friendList = KeySchema.PersonFriends.getKey(personId);
			redis().addMemberOnSet(friendList, friendId);
		}
	}
	
	/**
	 * @param personId
	 * @param friendId
	 */
	public void removeFriend(String personId, String friendId)
	{
		String friendList = KeySchema.PersonFriends.getKey(personId);
		redis().removeMemberOnSet(friendList, friendId);
	}

	/**
	 * @param personId
	 * @return all friends of the person
	 */
	public List<String> getAllFriends(String personId)
	{
		String friendList = KeySchema.PersonFriends.getKey(personId);
		return redis().getAllMembersOnSet(friendList);
	}

	/**
	 * @param personId
	 * @param eventId
	 */
	public void attendEvent(String personId, String eventId)
	{
		String eventList = KeySchema.PersonAttendEvents.getKey(personId);
		redis().addMemberOnSet(eventList, eventId);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void unattendEvent(String personId, String eventId)
	{
		String eventList = KeySchema.PersonAttendEvents.getKey(personId);
		redis().removeMemberOnSet(eventList, eventId);
	}
	
	/**
	 * @param personId
	 * @return all (active) events the person attend
	 */
	public List<String> getAllAttendEvents(String personId)
	{
		String eventList = KeySchema.PersonAttendEvents.getKey(personId);
		return redis().getAllMembersOnSet(eventList);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void markFavoriteEvent(String personId, String eventId)
	{
		String favEventList = KeySchema.PersonFavoriteEvents.getKey(personId);
		redis().addMemberOnSet(favEventList, eventId);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void unmarkFavoriteEvent(String personId, String eventId)
	{
		String favEventList = KeySchema.PersonFavoriteEvents.getKey(personId);
		redis().removeMemberOnSet(favEventList, eventId);
	}
	
	/**
	 * @param personId
	 * @return all (active) events the person favorite
	 */
	public List<String> getAllFavoriteEvents(String personId)
	{
		String favEventList = KeySchema.PersonFavoriteEvents.getKey(personId);
		return redis().getAllMembersOnSet(favEventList);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void invitedToEvent(String personId, String eventId, Double score)
	{
		String inviteEventList = KeySchema.PersonInvitedToEvents.getKey(personId);		
		redis().increaseMemberScoreOnSortedSet(inviteEventList, eventId, score);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void unInvitedToEvent(String personId, String eventId)
	{
		String inviteEventList = KeySchema.PersonInvitedToEvents.getKey(personId);
		redis().removeMembersOnSortedSet(inviteEventList, eventId);
	}
	
	/**
	 * @param personId
	 * @return all (active) events the person is invited to
	 */
	public List<Tuple> getAllInvitedToEvents(String personId)
	{
		String inviteEventList = KeySchema.PersonInvitedToEvents.getKey(personId);
		List<Tuple> list = redis().getAllMembersOnSortedSet(inviteEventList);
		return list;
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void addToAllEventsHistory(String personId, String eventId)
	{
		String allEventsList = KeySchema.PersonAllEvents.getKey(personId);
		redis().addMemberOnSet(allEventsList, eventId);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void removeFromAllEventsHistory(String personId, String eventId)
	{
		String allEventsList = KeySchema.PersonAllEvents.getKey(personId);
		redis().removeMemberOnSet(allEventsList, eventId);
	}
	
	/**
	 * @param personId
	 * @return all events person interacted with
	 */
	public List<String> getAllEventsHistory(String personId)
	{
		String allEventsList = KeySchema.PersonAllEvents.getKey(personId);
		return redis().getAllMembersOnSet(allEventsList);
	}
	
	/**
	 * @param personId
	 * @param interest
	 * @param incrScore
	 */
	public void increaseInterestScore(String personId, String interest, Double incrScore)
	{
		String interestGrid = KeySchema.PersonInterestGrid.getKey(personId);
		redis().increaseMemberScoreOnSortedSet(interestGrid, interest, incrScore);
	}
	
	/**
	 * @param personId
	 * @param interests
	 */
	public void removeInterests(String personId, String... interests)
	{
		String interestGrid = KeySchema.PersonInterestGrid.getKey(personId);
		redis().removeMembersOnSortedSet(interestGrid, interests);
	}
	
	/**
	 * @param personId
	 * @return
	 */
	public List<Tuple> getAllInterests(String personId)
	{
		String interestGrid = KeySchema.PersonInterestGrid.getKey(personId);
		return redis().getAllMembersOnSortedSet(interestGrid);
	}
	
	/**
	 * @param personId
	 * @param incrRepScore
	 */
	public void increaseReputationScore(String personId, long incrRepScore)
	{
		String reputationScore = KeySchema.PersonReputation.getKey(personId);
		redis().increaseKeyLongValue(reputationScore, incrRepScore);
	}
	
	/**
	 * @param personId
	 * @param eventIds
	 */
	public void addToRecommendationList(String personId, String... eventIds)
	{
		String recommendationList = KeySchema.PersonRecommendationEvents.getKey(personId);
		redis().addMembersOnSet(recommendationList, eventIds);
	}
	
	/**
	 * @param personId
	 * @return recommended event list for the person
	 */
	public List<String> getRecommendationList(String personId)
	{
		String recommendationList = KeySchema.PersonRecommendationEvents.getKey(personId);
		return redis().getAllMembersOnSet(recommendationList);
	}
}
