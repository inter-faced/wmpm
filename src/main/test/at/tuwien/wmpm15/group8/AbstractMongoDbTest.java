package at.tuwien.wmpm15.group8;

/**
 * testing mongo db connection 
 * adapted from camel mongodb test
 */



import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;

import at.tuwien.wmpm15.group8.utils.CredentialsReader;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public abstract class AbstractMongoDbTest extends CamelTestSupport {

    protected static DB db;
    protected static DBCollection testCollection;
    protected static DBCollection dynamicCollection;
    
    protected static String dbName;
    protected static String testCollectionName;
    protected static String dynamicCollectionName;

    protected static Properties properties; 
    
    protected ApplicationContext applicationContext;
    
	private static MongoClient mongoClient;
	
	
    public AbstractMongoDbTest() {
        super();
    }
    
    /**
     * Checks whether Mongo is running using the connection URI 
     * @throws IOException 
     */
    @SuppressWarnings("deprecation")
	@BeforeClass
    public static void checkMongoRunning() throws IOException {
    	System.out.println("Init test ...");
    	 properties= CredentialsReader.read();
		MongoCredential credential = MongoCredential.createMongoCRCredential(properties.getProperty("mongodb.userName"), properties.getProperty("mongodb.testdbName"), properties.getProperty("mongodb.password").toCharArray());
		mongoClient = new MongoClient(new ServerAddress(properties.getProperty("mongodb.testurl"),Integer.parseInt(properties.getProperty("mongodb.testport"))), Arrays.asList(credential));
	
        // ping Mongo and populate db and collection
        try {

            dbName = properties.getProperty("mongodb.testdbName");

            db=mongoClient.getDB(dbName);
            
        } catch (Exception e) {
        	System.out.println("exception cought " + e);
            Assume.assumeNoException(e);
        }
        
    }

    @Before
    public void initTestCase() {
        // Refresh the test collection - drop it and recreate it. We don't do this for the database because MongoDB would create large
        // store files each time
        try{
    	testCollectionName = properties.getProperty("mongodb.testCappedAppCollection");
        System.out.println("testCollectionName "+testCollectionName);
        testCollection = db.getCollection(testCollectionName);
        testCollection.drop();
        testCollection = db.getCollection(testCollectionName);
        
        }catch(Exception e)
        {
        	e.printStackTrace();
        }

    }

    @After
    public void cleanup() {
        testCollection.drop();
    }

}