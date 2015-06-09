package at.tuwien.wmpm15.group8.routebuilder;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TwitterRoute extends RouteBuilder {

    static int count = 0;

    public void configure() {

        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");

        from("direct:twitterpreproc")
                .process(new Processor() {
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
                        System.out.println("------>>>>>>> get data from twittername:" + twittername);


                        DynamicRouteBuilder dynamicRouteBuilder = new DynamicRouteBuilder("twitter://timeline/user?type=polling&user=" + twittername + "&count=0&numberOfPages=1&consumerKey={{twitter.consumerKey}}&consumerSecret={{twitter.consumerSecret}}&accessToken={{twitter.accessToken}}&accessTokenSecret={{twitter.accessTokenSecret}}",
                                "direct:twitterresult",
                                "eventFilePooler"); //dynamic route name
                        exchange.getContext().addRoutes(dynamicRouteBuilder);
                    }
                })
                .log("-->>Twitter Filtered Body: ${body}")
                .transform(body().convertToString())
                .to("direct:eventFilePooler");

        from("direct:twitterresult")
                .to("file:target/messages/twitter");
    }
}
