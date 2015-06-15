package at.tuwien.wmpm15.group8.simulator;

import java.util.Arrays;
import java.util.Properties;

import org.bson.Document;

import at.tuwien.wmpm15.group8.utils.CredentialsReader;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;

public class AppSubmissionSimulator {

	String cappedCollectionNam;
	String enrichedCollectionName;
	MongoCredential credential ;
	MongoClient mongoClient;
	MongoDatabase db;
	MongoCollection <Document> sourceCollection ;
	MongoCollection <Document> destCollection ;
	MongoCollection <Document> enrchedApplicantsCollection ;
	


	public AppSubmissionSimulator(){

		Properties prop = CredentialsReader.read();

		cappedCollectionNam = prop.getProperty("mongodb.webdbApplicantsCollectionCapped");
		enrichedCollectionName = prop.getProperty("mongodb.webdbApplicantsCollectionEnriched");
		
		credential = MongoCredential.createMongoCRCredential(prop.getProperty("mongodb.userName"), prop.getProperty("mongodb.webdbName"), prop.getProperty("mongodb.password").toCharArray());

		mongoClient = new MongoClient(new ServerAddress(prop.getProperty("mongodb.webdburl"),Integer.parseInt(prop.getProperty("mongodb.webdbport"))), Arrays.asList(credential));

		db= mongoClient.getDatabase(prop.getProperty("mongodb.webdbName"));

		sourceCollection = db.getCollection(prop.getProperty("mongodb.webdbApplicantsCollection"));

		destCollection = db.getCollection(cappedCollectionNam);

		enrchedApplicantsCollection = db.getCollection(enrichedCollectionName);
		
		prepareSimulation();

	}




	protected void prepareSimulation()
	{
		destCollection.drop();

		CreateCollectionOptions options= new CreateCollectionOptions();		
		options.capped(true);		
		options.sizeInBytes(1000000);		
		options.maxDocuments(4000);

		db.createCollection(cappedCollectionNam , options);

		destCollection=db.getCollection(cappedCollectionNam);
		
		
		enrchedApplicantsCollection.drop();
		db.createCollection(enrichedCollectionName);
		
	}

	
	

	public int startSimulation(){

		return startSimulation(0);
	}



	public int startSimulation(long sleepInterval)
	{
		int nrOfApplications=0;
		MongoCursor <Document> cursor = sourceCollection.find().iterator();

		try {
			while(cursor.hasNext()) {
				nrOfApplications++;
				Document currentDoc=cursor.next();
				//System.out.println(currentDoc);
				destCollection.insertOne(currentDoc);
				Thread.sleep(sleepInterval);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			cursor.close();
		}


		mongoClient.close();

		System.out.println("AppSubmissionSimulator: " + nrOfApplications +" applications added to the capped collection successfully");
		return nrOfApplications;
	}



	public static void main (String ...args)
	{
		AppSubmissionSimulator simpulator= new AppSubmissionSimulator();

		simpulator.startSimulation(2000);

	}
}
