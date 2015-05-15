package at.tuwien.wmpm15.group8;

import javax.jms.ConnectionFactory;

import at.tuwien.wmpm15.group8.routebuilder.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import  at.tuwien.wmpm15.group8.beans.MongoDbBean;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
    	
    	
    	JndiContext jndiContext = new JndiContext();
    	MongoDbBean mgBean=new MongoDbBean();    	
		jndiContext.bind("myDb", mgBean.getConnection());
    	    	
        CamelContext context = new DefaultCamelContext(jndiContext);
               
        // Set up the ActiveMQ JMS Components
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
    	
    	
    	
        

        //add all the Routes here
        context.addRoutes(new AnExampleRoute());
        context.addRoutes(new TwitterRoute());
        context.addRoutes(new FtpRoute());
        context.addRoutes(new MongoDbRoute());
        context.addRoutes(new SendToDepartment());
        
  
        context.start();


        ProducerTemplate template = context.createProducerTemplate(); // there should be a producer, otherwise route seems not working!
        Object result =  template.requestBody("direct:findAll", "{}");

    }

}

