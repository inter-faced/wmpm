package at.tuwien.wmpm15.group8.simulator;

import java.util.Arrays;
import java.util.Properties;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import at.tuwien.wmpm15.group8.utils.CredentialsReader;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;

public class AppSubmissionSimulator {

	String cappedCollectionNam;

	MongoCredential credential ;
	MongoClient mongoClient;
	MongoDatabase db;
	MongoCollection <Document> sourceCollection ;
	MongoCollection <Document> destCollection ;

	
	public AppSubmissionSimulator(){
		Properties prop = CredentialsReader.read();
		cappedCollectionNam = prop.getProperty("mongodb.webdbApplicantsCollectionCapped");

		 credential = MongoCredential.createMongoCRCredential(prop.getProperty("mongodb.userName"), prop.getProperty("mongodb.webdbName"), prop.getProperty("mongodb.password").toCharArray());

		 mongoClient = new MongoClient(new ServerAddress(prop.getProperty("mongodb.webdburl"),Integer.parseInt(prop.getProperty("mongodb.webdbport"))), Arrays.asList(credential));



		 db= mongoClient.getDatabase(prop.getProperty("mongodb.webdbName"));

		sourceCollection = db.getCollection(prop.getProperty("mongodb.webdbApplicantsCollection"));

		destCollection = db.getCollection(cappedCollectionNam);
		
		prepareSimulation();

	}
	
	public void prepareSimulation()
	{
		destCollection.drop();
		CreateCollectionOptions options= new CreateCollectionOptions();
		options.capped(true);
		options.sizeInBytes(1000000);
		options.maxDocuments(4000);
		db.createCollection(cappedCollectionNam , options);
		destCollection=db.getCollection(cappedCollectionNam);
	}
	
	public void startSimulation(long sleepInterval)
	{
	







		MongoCursor <Document> cursor = sourceCollection.find().iterator();

		try {
			while(cursor.hasNext()) {
				Document currentDoc=cursor.next();
				System.out.println(currentDoc);
				destCollection.insertOne(currentDoc);
			//	Thread.sleep(sleepInterval);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			cursor.close();
		}


	//	mongoClient.close();

	}



	public static void main (String ...args)
	{
		AppSubmissionSimulator simpulator= new AppSubmissionSimulator();
		
		simpulator.startSimulation(2000);

	}
}
