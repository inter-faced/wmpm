package at.tuwien.wmpm15.group8.routebuilder;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import twitter4j.Status;

public class TwitterRoute extends RouteBuilder {

    static int count = 0;

    public void configure() {

        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");

        from("direct:twitterpreproc")
                .process(new Processor() { // set message header ID
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message msg = exchange.getIn();

                        String msgbody = exchange.getIn().getBody(String.class);

                        JSONParser jsonParser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(msgbody.toString());

                        JSONObject socialnetworks = (JSONObject) jsonObject.get("socialnetworks");

                        //TODO exception handling
                        JSONObject twitter = (JSONObject) socialnetworks.get("twitter");
                        String twittername = (String) twitter.get("nickname");

                        msg.setBody(twittername);
                    }
                })
                //.log("-->>>Body before twitterfetch: " + "${body}")
                .to("direct:twitterinternal");


        from("direct:twitterinternal")
                //.log("TwitterLOG: ${body}")
                .from("twitter://timeline/user?type=polling&user=" + "David_Alaba" + "&count=0&numberOfPages=1&consumerKey={{twitter.consumerKey}}&consumerSecret={{twitter.consumerSecret}}&accessToken={{twitter.accessToken}}&accessTokenSecret={{twitter.accessTokenSecret}}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message msg = exchange.getIn();
                        Status status = exchange.getIn().getBody(Status.class);

                        count++;
                        if (count == 1) {
                            JSONObject obj = new JSONObject();
                            obj.put("favouritesCount", status.getUser().getFavouritesCount());
                            obj.put("followersCount", status.getUser().getFollowersCount());
                            obj.put("tweetCount", status.getUser().getStatusesCount());
                            msg.setBody(obj.toString());
                            
               //             msg.setHeader("tostore", true);
                        }
                    }
                })
                .log("-->>Twitter Header: ${header.id}")
             //   .filter(header("tostore").isEqualTo(true))
                .log("-->>Twitter Filtered Body: ${body}")
                .transform(body().convertToString())
                //.to("file:target/messages/twitter");
                .to("direct:startAggregator");
        
        
    }
}
