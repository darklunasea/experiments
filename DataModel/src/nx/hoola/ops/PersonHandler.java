package nx.hoola.ops;

import nx.hoola.data.PersonDataHandler;
import nx.hoola.data.RedisEngine;
import nx.hoola.exception.ExceptionType;
import nx.hoola.exception.PersonException;

public class PersonHandler
{
	PersonDataHandler person;

	public PersonHandler(RedisEngine redis)
	{
		person = new PersonDataHandler(redis);
	}

	public void createNewPerson(String newPersonId) throws PersonException
	{
		if (person.isPersonExist(newPersonId))
		{
			throw new PersonException(ExceptionType.PersonAlreadyExist, "Person [" + newPersonId + "] already exist");
		}
		
		
	}
}
