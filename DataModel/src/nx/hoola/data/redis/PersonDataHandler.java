package nx.hoola.data.redis;

import java.util.List;

import nx.hoola.data.scheme.DataScheme;
import nx.hoola.data.scheme.KeyScheme;
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
		String allPersonsList = KeyScheme.AllPersonsList.getKey(null);
		redis().addMemberOnSet(allPersonsList, personId);
	}

	/**
	 * @param personId
	 * @param friendId
	 * @return are the two persons friends
	 */
	public boolean isFriend(String personId, String friendId)
	{
		String friendList = KeyScheme.PersonFriends.getKey(personId);
		return redis().isMemberOnSortedSet(friendList, friendId);
	}

	/**
	 * @param personId
	 * @param friendId
	 */
	public void addFriend(String personId, String friendId)
	{
		if (isPersonExist(friendId))
		{
			String friendList = KeyScheme.PersonFriends.getKey(personId);
			redis().increaseMemberScoreOnSortedSet(friendList, friendId, DataScheme.P_NewFriendInitScore.doubleValue());
		}
	}
	
	/**
	 * @param personId
	 * @param friendId
	 */
	public void removeFriend(String personId, String friendId)
	{
		String friendList = KeyScheme.PersonFriends.getKey(personId);
		redis().removeMemberOnSet(friendList, friendId);
	}

	/**
	 * @param personId
	 * @return all friends of the person
	 */
	public List<String> getAllFriends(String personId)
	{
		String friendList = KeyScheme.PersonFriends.getKey(personId);
		return redis().getAllMembersOnSet(friendList);
	}

	/**
	 * @param personId
	 * @param eventId
	 */
	public void attendEvent(String personId, String eventId)
	{
		String eventList = KeyScheme.PersonAttendEvents.getKey(personId);
		redis().addMemberOnSet(eventList, eventId);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void unattendEvent(String personId, String eventId)
	{
		String eventList = KeyScheme.PersonAttendEvents.getKey(personId);
		redis().removeMemberOnSet(eventList, eventId);
	}
	
	/**
	 * @param personId
	 * @return all (active) events the person attend
	 */
	public List<String> getAllAttendEvents(String personId)
	{
		String eventList = KeyScheme.PersonAttendEvents.getKey(personId);
		return redis().getAllMembersOnSet(eventList);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void markFavoriteEvent(String personId, String eventId)
	{
		String favEventList = KeyScheme.PersonFavoriteEvents.getKey(personId);
		redis().addMemberOnSet(favEventList, eventId);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void unmarkFavoriteEvent(String personId, String eventId)
	{
		String favEventList = KeyScheme.PersonFavoriteEvents.getKey(personId);
		redis().removeMemberOnSet(favEventList, eventId);
	}
	
	/**
	 * @param personId
	 * @return all (active) events the person favorite
	 */
	public List<String> getAllFavoriteEvents(String personId)
	{
		String favEventList = KeyScheme.PersonFavoriteEvents.getKey(personId);
		return redis().getAllMembersOnSet(favEventList);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void invitedToEvent(String personId, String eventId, Double score)
	{
		String inviteEventList = KeyScheme.PersonInvitedToEvents.getKey(personId);		
		redis().increaseMemberScoreOnSortedSet(inviteEventList, eventId, score);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void unInvitedToEvent(String personId, String eventId)
	{
		String inviteEventList = KeyScheme.PersonInvitedToEvents.getKey(personId);
		redis().removeMembersOnSortedSet(inviteEventList, eventId);
	}
	
	/**
	 * @param personId
	 * @return all (active) events the person is invited to
	 */
	public List<Tuple> getAllInvitedToEvents(String personId)
	{
		String inviteEventList = KeyScheme.PersonInvitedToEvents.getKey(personId);
		List<Tuple> list = redis().getAllMembersOnSortedSet(inviteEventList);
		return list;
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void addToAllEventsHistory(String personId, String eventId)
	{
		String allEventsList = KeyScheme.PersonAllEvents.getKey(personId);
		redis().addMemberOnSet(allEventsList, eventId);
	}
	
	/**
	 * @param personId
	 * @param eventId
	 */
	public void removeFromAllEventsHistory(String personId, String eventId)
	{
		String allEventsList = KeyScheme.PersonAllEvents.getKey(personId);
		redis().removeMemberOnSet(allEventsList, eventId);
	}
	
	/**
	 * @param personId
	 * @return all events person interacted with
	 */
	public List<String> getAllEventsHistory(String personId)
	{
		String allEventsList = KeyScheme.PersonAllEvents.getKey(personId);
		return redis().getAllMembersOnSet(allEventsList);
	}
	
	/**
	 * @param personId
	 * @param interest
	 * @param incrScore
	 */
	public void increaseInterestScore(String personId, String interest, Double incrScore)
	{
		String interestGrid = KeyScheme.PersonInterestGrid.getKey(personId);
		redis().increaseMemberScoreOnSortedSet(interestGrid, interest, incrScore);
	}
	
	/**
	 * @param personId
	 * @param interests
	 */
	public void removeInterests(String personId, String... interests)
	{
		String interestGrid = KeyScheme.PersonInterestGrid.getKey(personId);
		redis().removeMembersOnSortedSet(interestGrid, interests);
	}
	
	/**
	 * @param personId
	 * @return
	 */
	public List<Tuple> getAllInterests(String personId)
	{
		String interestGrid = KeyScheme.PersonInterestGrid.getKey(personId);
		return redis().getAllMembersOnSortedSet(interestGrid);
	}
	
	/**
	 * @param personId
	 * @param incrRepScore
	 */
	public void increaseReputationScore(String personId, long incrRepScore)
	{
		String reputationScore = KeyScheme.PersonReputation.getKey(personId);
		redis().increaseKeyLongValue(reputationScore, incrRepScore);
	}
	
	/**
	 * @param personId
	 * @param eventIds
	 */
	public void addToRecommendationList(String personId, String... eventIds)
	{
		String recommendationList = KeyScheme.PersonRecommendationEvents.getKey(personId);
		redis().addMembersOnSet(recommendationList, eventIds);
	}
	
	/**
	 * @param personId
	 * @return recommended event list for the person
	 */
	public List<String> getRecommendationList(String personId)
	{
		String recommendationList = KeyScheme.PersonRecommendationEvents.getKey(personId);
		return redis().getAllMembersOnSet(recommendationList);
	}
}
