package nx.hoola.ops;

import nx.hoola.data.mongo.Person;
import nx.hoola.data.mongo.PersonObjectHandler;
import nx.hoola.data.redis.PersonDataHandler;
import nx.hoola.data.redis.RedisEngine;
import nx.hoola.exception.ExceptionType;
import nx.hoola.exception.PersonException;

import com.mongodb.DB;

public class PersonHandler
{
	PersonDataHandler personData;		//dynamic data
	PersonObjectHandler personObject;	//static data

	public PersonHandler(RedisEngine redis, DB mongo)
	{
		personData = new PersonDataHandler(redis);
		personObject = new PersonObjectHandler(mongo);
	}
	
	public boolean isPersonExist(String personId)
	{
		return personData.isPersonExist(personId);
	}

	public void createNewPerson(Person person) throws PersonException
	{
		if (isPersonExist(person.getName()))
		{
			throw new PersonException(ExceptionType.PersonAlreadyExist, "Person [" + person.getName() + "] already exist");
		}

		personObject.createNewPerson(person);
		personData.addToAllPersonsList(person.getName());
	}
	
	public void updatePersonData(Person person)
	{
		
	}
	
	public void addFriend(String personId, String friendId)
	{
		personData.addFriend(personId, friendId);
	}
	
	public void attendEvent(String personId, String eventId)
	{
		personData.attendEvent(personId, eventId);
		personData.addToAllEventsHistory(personId, eventId);
	}
	
	public void unattendEvent(String personId, String eventId)
	{
		personData.unattendEvent(personId, eventId);
		personData.removeFromAllEventsHistory(personId, eventId);
	}
}
