package at.tuwien.wmpm15.group8.beans;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

public class MongoDbBean  {

	private MongoClient mongoClient;
	
	
	public MongoDbBean()
	{
		Properties prop= getMongoDbProperty();
		MongoCredential credential = MongoCredential.createMongoCRCredential(prop.getProperty("mongodb.userName"), prop.getProperty("mongodb.webdbName"), prop.getProperty("mongodb.password").toCharArray());
		
		
			try {
				mongoClient = new MongoClient(new ServerAddress(prop.getProperty("mongodb.webdburl"),Integer.parseInt(prop.getProperty("mongodb.webdbport"))), Arrays.asList(credential));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		

		

	}

	public MongoClient getConnection()
	{
		return mongoClient;
	}


	private Properties getMongoDbProperty()
	{
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "credentials.properties";
			input = MongoDbBean.class.getClassLoader().getResourceAsStream(filename);
			if(input==null){
				System.out.println("Sorry, unable to find " + filename);
				return null;
			}

			prop.load(input);


			System.out.println("dbName: "+ prop.getProperty("mongodb.webdbName"));
			System.out.println("userName: "+prop.getProperty("mongodb.userName"));
			System.out.println("password: "+prop.getProperty("mongodb.password"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} 
		}
		return prop;
	}
}