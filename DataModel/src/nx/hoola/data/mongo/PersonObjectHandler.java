package nx.hoola.data.mongo;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class PersonObjectHandler
{
	private static final String COLLECTION_KEY = "persons";
	
	private final DB db;
	private final DBCollection collection;

	public PersonObjectHandler(DB db)
	{
		this.db = db;
		this.collection = db.getCollection(COLLECTION_KEY);
	}
	
	public void createNewPerson(Person person)
	{
		if(!person.hasId())
		{
			collection.insert(person.getDbObject());
		}
	}
	
	public void updatePerson(Person person)
	{
		if(person.hasId())
		{
			collection.insert(person.getDbObject());
		}
	}
	
	public Person findPersonById(String id)
	{
		DBObject p = collection.findOne(new BasicDBObject("_id", new ObjectId(id)));
		if(p != null)
		{
			new Person((BasicDBObject) p);
		}
		return null;
	}
}
