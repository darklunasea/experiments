package main.java.com.nxiao.eventapp;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

import static spark.Spark.setIpAddress;
import static spark.Spark.setPort;
import static spark.SparkBase.staticFileLocation;

public class Bootstrap
{
	private static final String MONGODB_DB_HOST = "192.168.2.3";
	private static final int MONGODB_DB_PORT = 27017;
	private static final String MONGODB_DB_NAME = "testapp";
	
	private static final String IP_ADDRESS = "localhost";
	private static final int PORT = 8080;

	public static void main(String[] args) throws Exception
	{
		setIpAddress(IP_ADDRESS);
		setPort(PORT);
		staticFileLocation("/public");
		new TodoResource(new TodoService(mongo()));
	}

	private static DB mongo() throws Exception
	{
		String host = MONGODB_DB_HOST;
		int port = MONGODB_DB_PORT;
		String dbname = MONGODB_DB_NAME;
		String username = "";
		String password = "";
		MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
		MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), mongoClientOptions);
		mongoClient.setWriteConcern(WriteConcern.SAFE);
		DB db = mongoClient.getDB(dbname);
		return db;
		/*if (db.authenticate(username, password.toCharArray()))
		{
			return db;
		}
		else
		{
			throw new RuntimeException("Not able to authenticate with MongoDB");
		}*/
	}
}
