package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

/**
 * Created by alex on 15.05.15.
 */
public class SendToDepartment extends RouteBuilder {


    public void configure() throws Exception {


        from("file:src/data/attachments/Tanscript.jpg?noop=true")
                .setHeader("subject", simple("New application:"))
                .log("Sending Mail!")
                .to("smtp://address?password=&to=address");

        /*CamelContext context = new DefaultCamelContext();



        // create the exchange with the mail message that is multipart with a file and a Hello World text/plain message.
        Exchange exchange = endpoint.createExchange();
        Message in = exchange.getIn();
     //   in.setBody("Hello World");
     //   in.addAttachment("logo.jpeg", new DataHandler(new FileDataSource("src/test/data/logo.jpeg")));

        // create a producer that can produce the exchange (= send the mail)
        Producer producer = endpoint.createProducer();
        // start the producer
        producer.start();
        // and let it go (processes the exchange by sending the email)
        producer.process(exchange);
        */


    }
}
