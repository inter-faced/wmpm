package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.builder.RouteBuilder;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

public class SendToDepartment extends RouteBuilder {

    public void configure() throws Exception {


// create an exchange with a normal body and attachment to be produced as email
        Endpoint endpoint = getContext().getEndpoint("smtps://smtp.gmail.com?username=username@gmail.com&password=pw&to=recipient@gmail.com&debugMode=true");
// create the exchange with the mail message that is multipart with a file and a Hello World text/plain message.
        Exchange exchange = endpoint.createExchange();
        Message in = exchange.getIn();
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
