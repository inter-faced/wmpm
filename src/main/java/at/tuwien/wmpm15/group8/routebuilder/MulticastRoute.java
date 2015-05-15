package at.tuwien.wmpm15.group8.routebuilder;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class MulticastRoute extends RouteBuilder {

    public void configure() {

        from("jms:queue:test.queue")
                .process(new Processor() { // set message header ID
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        Message msg = exchange.getIn();
                        String body = exchange.getIn().getBody(String.class);


                        System.out.println(body);

                    }
                })
                .multicast()
                .parallelProcessing()
                .log("-->>testqueue: ${body}")
                .to("jms:queue:twitter.queue");
    }
}
