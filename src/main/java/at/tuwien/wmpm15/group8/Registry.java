package at.tuwien.wmpm15.group8;

import at.tuwien.wmpm15.group8.beans.MongoDbBean;
import at.tuwien.wmpm15.group8.routebuilder.*;
import at.tuwien.wmpm15.group8.simulator.AppSubmissionSimulator;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.XsltBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.ProcessorEndpoint;
import org.apache.camel.util.jndi.JndiContext;

import javax.jms.ConnectionFactory;
import javax.xml.transform.TransformerFactory;

/**
 * Created by alex on 18.06.15.
 */
public class Registry {


    public static void setUp() throws Exception {
        JndiRegistry registry=new JndiRegistry(new JndiContext());

        AppSubmissionSimulator simulator= new AppSubmissionSimulator();
        simulator.startSimulation();

        JndiContext jndiContext = new JndiContext();
        MongoDbBean mgBean=new MongoDbBean();
        jndiContext.bind("myDb", mgBean.getConnection());

        CamelContext context = new DefaultCamelContext(jndiContext);
        context.getProperties().put(Exchange.LOG_DEBUG_BODY_MAX_CHARS, "3000");
        context.getShutdownStrategy().setTimeout(3); // for twitter messages that should not be processed

        // Set up the ActiveMQ JMS Components
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        //add all the Routes here
        context.addRoutes(new MulticastRoute());
        context.addRoutes(new TwitterRoute());
        context.addRoutes(new FtpRoute());
        context.addRoutes(new MongoDbRoute());
        context.addRoutes(new SendToDepartment());
        context.addRoutes(new DepartmentAnswerRoute());
        context.addRoutes(new SaveToDbRoute());
        context.addRoutes(new AnswerConsumer());
        context.addRoutes(new InvitationRoute());
        context.addRoutes(new MailQueue());


        context.start();
        Thread.sleep(60000);

    }

}
