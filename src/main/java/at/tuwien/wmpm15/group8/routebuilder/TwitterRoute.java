package at.tuwien.wmpm15.group8.routebuilder;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import twitter4j.Status;

public class TwitterRoute extends RouteBuilder {

    static int count = 0;

    public void configure() {

        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");

        final String user = "David_Alaba";


        from("jms:queue:twitter.queue")
            .to("twitter://timeline/user?count=3&user=" + user + "&consumerKey={{twitter.consumerKey}}&consumerSecret={{twitter.consumerSecret}}&accessToken={{twitter.accessToken}}&accessTokenSecret={{twitter.accessTokenSecret}}");

        // poll twitter search for new tweets
        fromF("twitter://timeline/user?count=3&user=" + user + "&consumerKey={{twitter.consumerKey}}&consumerSecret={{twitter.consumerSecret}}&accessToken={{twitter.accessToken}}&accessTokenSecret={{twitter.accessTokenSecret}}")
                .process(new Processor() { // set message header ID
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        count++;
                        Message msg = exchange.getIn();

                        Status status = exchange.getIn().getBody(Status.class);
                        if (count == 1) {
                            JSONObject obj = new JSONObject();
                            obj.put("name", user);
                            obj.put("favouritesCount", status.getUser().getFavouritesCount());
                            obj.put("followersCount", status.getUser().getFollowersCount());
                            obj.put("tweetCount", status.getUser().getStatusesCount());

                            msg.setBody(obj.toString());
                            msg.setHeader("tostore", true);
                        }
                    }
                })
                .filter(header("tostore").isEqualTo(true))
                .transform(body().convertToString())
                .to("file:target/messages/twitter");
    }
}
