package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

public class SendToDepartment extends RouteBuilder {

    public void configure() throws Exception {

        // important note: log in with the credentials provided on Google Drive in order to be able to use this route

        // configure properties component for credentials.properties
        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");


        // create an exchange with a normal body and attachment to be produced as email
        Endpoint endpoint = getContext().getEndpoint("{{smtps.outserver}}?username={{smtps.username}}&password={{smtps.password}}&to={{smtps.to}}&debugMode=true");
        // create the exchange with the mail message that is multipart with a file and a Hello World text/plain message.
        Exchange exchange = endpoint.createExchange();
        Message in = exchange.getIn();
        in.setHeader("subject", ("New Application"));
        in.setBody("Hello World");
        in.addAttachment("LOR.jpg", new DataHandler(new FileDataSource("src/data/attachments/LOR.jpg")));

        // create a producer that can produce the exchange (= send the mail)
        Producer producer = endpoint.createProducer();
        // start the producer
        producer.start();
        // and let it go (processes the exchange by sending the email)
        producer.process(exchange);


    }


}
