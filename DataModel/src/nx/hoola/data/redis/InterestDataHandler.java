package nx.hoola.data.redis;

import java.util.List;

import nx.hoola.data.scheme.KeyScheme;
import redis.clients.jedis.Tuple;

public class InterestDataHandler extends BasicData
{
	public InterestDataHandler(RedisEngine redis)
	{
		super(redis);
	}

	/**
	 * @param interestTag
	 */
	public void addToAllInterestsList(String interestTag)
	{
		String interestsList = KeyScheme.AllInterestTagsList.getKey(null);
		redis().addMemberOnSet(interestsList, interestTag);
	}
	
	/**
	 * @param interestTag
	 * @param eventId
	 * @param eventScore
	 */
	public void addEventToInterest(String interestTag, String eventId, Double eventScore)
	{
		String tagEventList = KeyScheme.InterestTagEvents.getKey(interestTag);
		redis().increaseMemberScoreOnSortedSet(tagEventList, eventId, eventScore);;
	}
	
	/**
	 * @param interestTag
	 * @param eventId
	 * @param IncrScore
	 */
	public void increaseEventScoreUnderInterest(String interestTag, String eventId, Double IncrScore)
	{
		String tagEventList = KeyScheme.InterestTagEvents.getKey(interestTag);
		redis().increaseMemberScoreOnSortedSet(tagEventList, eventId, IncrScore);
	}
	
	/**
	 * @param interestTag
	 * @return
	 */
	public List<Tuple> getAllEventsUnderInterest(String interestTag)
	{
		String tagEventList = KeyScheme.InterestTagEvents.getKey(interestTag);
		return redis().getAllMembersOnSortedSet(tagEventList);
	}
}
