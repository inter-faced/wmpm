package at.tuwien.wmpm15.group8.routebuilder;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.twitter.TwitterComponent;
import twitter4j.Status;

public class TwitterRoute extends RouteBuilder {

    static int count = 0;

    public void configure() {

        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");

        String user = "sebastiankurz";


        // poll twitter search for new tweets
        fromF("twitter://timeline/user?type=direct&user=" + user + "&consumerKey={{twitter.consumerKey}}&consumerSecret={{twitter.consumerSecret}}&accessToken={{twitter.accessToken}}&accessTokenSecret={{twitter.accessTokenSecret}}")
                .process(new Processor() { // set message header ID
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Status msg = exchange.getIn().getBody(Status.class);

                        System.out.println("--------<<<<Sysout>>>------ Message Text: " + msg.getText());
                        System.out.println("--------<<<<Sysout>>>------ Retweet count: " + msg.getRetweetCount());
                        System.out.println("--------<<<<Sysout>>>------ Retweet user: " + msg.getUser().getFavouritesCount());
                        count++;
                        System.out.println("--------<<<<Sysout>>>------ Count: " + count);

                    }
                })
                .transform(body().convertToString())
                        //.log(">> Twitter Poll : ${body}")
                .to("file:target/messages/twitter");


    }
}
