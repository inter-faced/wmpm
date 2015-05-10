package at.tuwien.wmpm15.group8;

import javax.jms.ConnectionFactory;

import at.tuwien.wmpm15.group8.routebuilder.AnExampleRoute;
import at.tuwien.wmpm15.group8.routebuilder.FtpRoute;
import at.tuwien.wmpm15.group8.routebuilder.LinkedinRouteBuilder;
import at.tuwien.wmpm15.group8.routebuilder.TwitterRoute;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        //configure Camel’s JMS component with an appropriate ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
       
        //add all the Routes here
       // context.addRoutes(new AnExampleRoute());
       // context.addRoutes(new TwitterRoute());
        context.addRoutes(new FtpRoute());
       // context.addRoutes(new LinkedinRouteBuilder());

        
       // Component linkedinComponent = new org.apache.camel.component.linkedin(); 

        		context.start();
    }

}

