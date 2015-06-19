package nx.hoola.ops;

import java.util.List;

import nx.hoola.data.mongo.Person;
import nx.hoola.data.mongo.PersonObjectHandler;
import nx.hoola.data.redis.EventDataHandler;
import nx.hoola.data.redis.PersonDataHandler;
import nx.hoola.data.redis.RedisEngine;
import nx.hoola.data.scheme.DataScheme;
import nx.hoola.exception.ExceptionType;
import nx.hoola.exception.PersonException;

import com.mongodb.DB;

public class PersonHandler extends BasicHandler
{
	PersonDataHandler personData; // dynamic data
	PersonObjectHandler personObject; // static data

	EventDataHandler eventData;

	public PersonHandler(RedisEngine redis, DB mongo)
	{
		personData = new PersonDataHandler(redis);
		personObject = new PersonObjectHandler(mongo);

		eventData = new EventDataHandler(redis);
	}

	public void createNewPerson(Person person) throws PersonException
	{
		if (isPersonExist(personData, person.getName()))
		{
			throw new PersonException(ExceptionType.PersonAlreadyExist, "Person [" + person.getName()
					+ "] already exist");
		}
		personObject.createNewPerson(person);
		personData.addToAllPersonsList(person.getName());
	}

	public void addFriend(String personId, String friendId)
	{
		personData.addFriend(personId, friendId);
	}

	public void attendEvent(String personId, String eventId)
	{
		//person logic
		personData.attendEvent(personId, eventId);
		personData.addToAllEventsHistory(personId, eventId);
		// increase interest points
		List<String> interests = eventData.getAllEventInterestTags(eventId);
		updatePersonInterestGridByScore(personData, personId, interests, DataScheme.P_AttendEventInterestScore.doubleValue());
		
		//event logic
		eventData.addAttendPerson(eventId, personId);
		// increase event score
		eventData.increaseEventScore(eventId, DataScheme.E_AttendedRewardScore.longValue());
	}

	public void unattendEvent(String personId, String eventId)
	{
		//person logic
		personData.unattendEvent(personId, eventId);
		personData.removeFromAllEventsHistory(personId, eventId);
		// decrease interest points
		List<String> interests = eventData.getAllEventInterestTags(eventId);
		updatePersonInterestGridByScore(personData, personId, interests, DataScheme.P_UnattendEventInterestScore.doubleValue());
		
		//event logic
		eventData.removeAttendPerson(eventId, personId);
		// decrease event score
		eventData.increaseEventScore(eventId, DataScheme.E_UnattendedRewardScore.longValue());
	}

	public void favEvent(String personId, String eventId)
	{
		//person logic
		personData.markFavoriteEvent(personId, eventId);
		personData.addToAllEventsHistory(personId, eventId);
		// increase interest points
		List<String> interests = eventData.getAllEventInterestTags(eventId);
		updatePersonInterestGridByScore(personData, personId, interests, DataScheme.P_FavEventInterestScore.doubleValue());
		
		//event logic
		eventData.addMarkFavPerson(eventId, personId);
		// increase event score
		eventData.increaseEventScore(eventId, DataScheme.E_FavedRewardScore.longValue());
	}

	public void unfavEvent(String personId, String eventId)
	{
		//person logic
		personData.unmarkFavoriteEvent(personId, eventId);
		personData.removeFromAllEventsHistory(personId, eventId);
		// decrease interest points
		List<String> interests = eventData.getAllEventInterestTags(eventId);
		updatePersonInterestGridByScore(personData, personId, interests, DataScheme.P_UnfavEventInterestScore.doubleValue());
		
		//event logic
		eventData.removeMarkFavPerson(eventId, personId);
		// decrease event score
		eventData.increaseEventScore(eventId, DataScheme.E_UnfavedRewardScore.longValue());
	}
}
