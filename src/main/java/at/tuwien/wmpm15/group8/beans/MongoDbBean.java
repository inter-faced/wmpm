package at.tuwien.wmpm15.group8.beans;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

import at.tuwien.wmpm15.group8.utils.CredentialsReader;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDbBean  {

	private MongoClient mongoClient;
	
	
	public MongoDbBean()
	{
		Properties prop= CredentialsReader.read();
		MongoCredential credential = MongoCredential.createMongoCRCredential(prop.getProperty("mongodb.userName"), prop.getProperty("mongodb.dbName"), prop.getProperty("mongodb.password").toCharArray());
		
		
			mongoClient = new MongoClient(new ServerAddress(prop.getProperty("mongodb.url"),Integer.parseInt(prop.getProperty("mongodb.port"))), Arrays.asList(credential));
		

		

	}

	public MongoClient getConnection()
	{
		return mongoClient;
	}



}