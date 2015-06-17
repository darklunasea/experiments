package nx.hoola.data.mongo;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

public class Person
{
	private String _id;
	private String name;
	private String email;
	private String pw;

	public Person(BasicDBObject dbObject)
	{
		this._id = ((ObjectId) dbObject.get("_id")).toString();
		this.name = dbObject.getString("name");
		this.email = dbObject.getString("email");
		this.pw = dbObject.getString("pw");
	}
	
	public boolean hasId()
	{
		return _id != null && !_id.isEmpty();
	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}

	public String getPw()
	{
		return pw;
	}

	public BasicDBObject getDbObject()
	{
		BasicDBObject obj;
		if(hasId())
		{
			obj = new BasicDBObject("_id", new ObjectId(_id));
		}
		else
		{
			obj = new BasicDBObject();
		}
		obj.append("name", name).append("email", email).append("pw", pw);
		return obj;
	}
}
