package at.tuwien.wmpm15.group8.beans;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import at.tuwien.wmpm15.group8.utils.CredentialsReader;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class ProcessCriteria {

	public void determine(@Body Object bodyObj, @Headers Map<String,Object> headers) {


		Properties prop= CredentialsReader.read();
		
		MongoCredential credential = MongoCredential.createMongoCRCredential(prop.getProperty("mongodb.userName"), prop.getProperty("mongodb.hrdepdbName"), prop.getProperty("mongodb.password").toCharArray());

		MongoClient mongoClient = new MongoClient(new ServerAddress(prop.getProperty("mongodb.hrdepurl"),Integer.parseInt(prop.getProperty("mongodb.hrdepport"))), Arrays.asList(credential));

		 

		MongoDatabase db= mongoClient.getDatabase(prop.getProperty("mongodb.hrdepdbName"));
		
		MongoCollection <Document> collectionNames = db.getCollection(prop.getProperty("mongodb.hrdepCriteriaCollection"));


		Document res= collectionNames.find().first();

		JSONParser jsonParser = new JSONParser();
		
		try {

			JSONObject jsonCriteria = (JSONObject) jsonParser.parse(res.toJson());
			JSONObject jsonApplicant = (JSONObject) jsonParser.parse(bodyObj.toString());


			//System.out.println("jsonCriteria: "+jsonCriteria);

			boolean isQualified=true;

			Set<?> keys = jsonCriteria.keySet(); //geting all first level keys e.g. languages
			Iterator<?> keyIterator = keys.iterator();

			while ( keyIterator.hasNext())
			{
				Object key= keyIterator.next();
				
				JSONObject jsonApplicantEval = (JSONObject) jsonApplicant.get(key);
				JSONObject jsonCriteriaEval = (JSONObject) jsonCriteria.get(key);

				Set<?> innerKeys = jsonCriteriaEval.keySet(); // geting all inner keys within the parent object. eg. german and english
				Iterator<?> innerKeyIterator = innerKeys.iterator();

				while ( innerKeyIterator.hasNext())
				{
					Object innerKey= innerKeyIterator.next();
					//System.out.println( "KEY: "+ innerKey);

					if (!innerKey.equals("$oid") ) //exclude processing id
					{
						JSONArray  criterias = (JSONArray) jsonCriteriaEval.get(innerKey);

						boolean isPartlyQualified = false;

						for (int i=0; i<criterias.size(); i++)
						{
							Object toCheck=jsonApplicantEval.get(innerKey);
							isPartlyQualified |= toCheck.equals(criterias.get(i)); // ORing possible values for one specific criteria eg. german proficiency should be either mothertongue or business-level


						}

						isQualified &= isPartlyQualified;

					}
				}

			}


			if(isQualified) 
				headers.put("status","qualified");
			else 
				headers.put("status","rejected");




		} catch (ParseException e) {
			e.printStackTrace();
		}

		
	}




}
