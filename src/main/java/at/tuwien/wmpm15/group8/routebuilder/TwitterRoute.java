package at.tuwien.wmpm15.group8.routebuilder;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;

public class TwitterRoute extends RouteBuilder {

    public void configure() {

        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");

        from("direct:twitterpreproc")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        Message msg = exchange.getIn();

                        JSONObject jsonObject = exchange.getIn().getBody(JSONObject.class);

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
                .to("direct:eventFilePooler");

        from("direct:twitterresult")
                .transform(body().convertToString())
                .to("file:target/messages/twitter");
    }
}
