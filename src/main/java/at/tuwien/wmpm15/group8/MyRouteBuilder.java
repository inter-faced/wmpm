package at.tuwien.wmpm15.group8;


import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

import org.apache.camel.component.twitter.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.TwitterComponent;
import twitter4j.*;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {

        // here is a sample which processes the input files
        // (leaving them in place - see the 'noop' flag)
        // then performs content based routing on the message using XPath
        from("file:src/data?noop=true")
            .choice()
                .when(xpath("/person/city = 'London'"))
                    .log("UK message")
                    .to("file:target/messages/uk")
                .otherwise()
                    .log("Other message")
                    .to("file:target/messages/others");

        // has to be changed to our twitter account, this is just provided from camel for testing
        String consumerKey = "NMqaca1bzXsOcZhP2XlwA";
        String consumerSecret = "VxNQiRLwwKVD0K9mmfxlTTbVdgRpriORypnUbHhxeQw";
        String accessToken = "26693234-W0YjxL9cMJrC0VZZ4xdgFMymxIQ10LeL1K8YlbBY";
        String accessTokenSecret = "BZD51BgzbOdFstWZYsqB5p5dbuuDV12vrOdatzhY4E";

        // setup Twitter component
        TwitterComponent tc = getContext().getComponent("twitter", TwitterComponent.class);
        tc.setAccessToken(accessToken);
        tc.setAccessTokenSecret(accessTokenSecret);
        tc.setConsumerKey(consumerKey);
        tc.setConsumerSecret(consumerSecret);

        String user = "derStandardat";

        // poll twitter search for new tweets
        fromF("twitter://timeline/user?type=direct&user=" + user)

                .process(new Processor() { // set message header ID
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Status msg = exchange.getIn().getBody(Status.class);
                        System.out.println("--------<<<<Sysout>>>------ Message Text: " + msg.getText());
                        System.out.println("--------<<<<Sysout>>>------ Retweet count: " + msg.getRetweetCount());
                    }
                })
                .transform(body().convertToString())
                //.log(">> Twitter Poll : ${body}")
                .to("file:target/messages/twitter");
    }

}