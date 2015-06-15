package at.tuwien.wmpm15.group8;




	import at.tuwien.wmpm15.group8.utils.CredentialsReader;
	import com.mongodb.MongoClient;
	import com.mongodb.MongoCredential;
	import com.mongodb.ServerAddress;

	import java.util.Arrays;
	import java.util.Properties;

	public class TestMongoDbBean  {

		private MongoClient mongoClient;


		public TestMongoDbBean()
		{
			Properties prop= CredentialsReader.read();
			MongoCredential credential = MongoCredential.createMongoCRCredential(prop.getProperty("mongodb.userName"), prop.getProperty("mongodb.testdbName"), prop.getProperty("mongodb.password").toCharArray());

			mongoClient = new MongoClient(new ServerAddress(prop.getProperty("mongodb.testurl"),Integer.parseInt(prop.getProperty("mongodb.testport"))), Arrays.asList(credential));

		}

		public MongoClient getConnection()
		{
			return mongoClient;
		}



	}