package at.tuwien.wmpm15.group8;

/**
 * testing mongo db tainling cursor 
 * adapted from camel mongodb test
 */

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.junit.Test;

import at.tuwien.wmpm15.group8.beans.MongoDbBean;
import at.tuwien.wmpm15.group8.simulator.AppSubmissionSimulator;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;


public class MongoTest extends AbstractMongoDbTest {
    
    private DBCollection cappedTestCollection;
    private String cappedTestCollectionName; 
    
    @Test
    public void testTenRecords() throws Exception {
        assertEquals(0, cappedTestCollection.count());
        MockEndpoint mock = getMockEndpoint("mock:test");
        mock.expectedMessageCount(10);
       
        // create a capped collection with max = 10
        cappedTestCollection = db.createCollection(cappedTestCollectionName, 
                BasicDBObjectBuilder.start().add("capped", true).add("size", 100000).add("max", 10).get());
        
        for (int i = 0; i < 10; i++) {
            cappedTestCollection.insert(BasicDBObjectBuilder.start("increasing", i).add("string", "value" + i).get(), WriteConcern.SAFE);
        }
        assertEquals(10, cappedTestCollection.count());

        addTestRoutes();
        context.startRoute("tailableCursorConsumer1");
        Thread.sleep(1000);
        mock.assertIsSatisfied();
        context.stopRoute("tailableCursorConsumer1");

    }
    
    @Test
    public void testNoRecords() throws Exception {
        assertEquals(0, cappedTestCollection.count());
        MockEndpoint mock = getMockEndpoint("mock:test");
        mock.expectedMessageCount(0);
       
        // create a capped collection with max = 1000
        cappedTestCollection = db.createCollection(cappedTestCollectionName, 
                BasicDBObjectBuilder.start().add("capped", true).add("size", 100000).add("max", 10).get());
        assertEquals(0, cappedTestCollection.count());

        addTestRoutes();
        context.startRoute("tailableCursorConsumer1");
        Thread.sleep(1000);
        mock.assertIsSatisfied();
        context.stopRoute("tailableCursorConsumer1");

    }
    
    @Test
    public void testSimulator() throws Exception {
        assertEquals(0, cappedTestCollection.count());
        MockEndpoint mock = getMockEndpoint("mock:test");
        mock.expectedMessageCount(3);
        
		AppSubmissionSimulator simpulator= new AppSubmissionSimulator();		
		simpulator.startSimulation(2000);
		
        addTestRoutes();
        context.startRoute("tailableCursorConsumer1");
        Thread.sleep(1000);
        mock.assertIsSatisfied();
        context.stopRoute("tailableCursorConsumer1");

    }
    public void assertAndResetMockEndpoint(MockEndpoint mock) throws Exception {
        mock.assertIsSatisfied();
        mock.reset();
    }
    
    @Override
    public void initTestCase() {
       super.initTestCase();
        // drop the capped collection and let each test create what they need
        cappedTestCollectionName = properties.getProperty("mongodb.testCappedAppCollection");
        cappedTestCollection = db.getCollection(cappedTestCollectionName);

        cappedTestCollection.drop();
       
        
        assertEquals(0, cappedTestCollection.count());
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        
    	 JndiContext jndiContext = new JndiContext();
    	 TestMongoDbBean mgBean=new TestMongoDbBean();
         jndiContext.bind("myDb", mgBean.getConnection());

         CamelContext context = new DefaultCamelContext(jndiContext);
         context.getProperties().put(Exchange.LOG_DEBUG_BODY_MAX_CHARS, "3000");
         context.getShutdownStrategy().setTimeout(5); // for twitter messages that should not be processed

         return context;
    }
    
    protected void addTestRoutes() throws Exception {
        context.addRoutes(new RouteBuilder() {
            
            @Override
            public void configure() throws Exception {

            	
            	PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        		pc.setLocation("classpath:credentials.properties");//classpath:mongodb.properties

                
                from("mongodb:myDb?database={{mongodb.testdbName}}&collection={{mongodb.testCappedAppCollection}}&tailTrackIncreasingField=increasing")
                    .id("tailableCursorConsumer1")
                    .autoStartup(false)
                    .to("mock:test");
                
                
            }
        });
    }
    
}