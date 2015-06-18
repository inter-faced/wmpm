package at.tuwien.wmpm15.group8;

import at.tuwien.wmpm15.group8.beans.MongoDbBean;
import at.tuwien.wmpm15.group8.routebuilder.*;
import at.tuwien.wmpm15.group8.simulator.AppSubmissionSimulator;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;

import javax.jms.ConnectionFactory;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {


        Registry.setUp();


    }

}