package at.tuwien.wmpm15.group8.routebuilder;


import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
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

                        JSONObject jsonObject = exchange.getIn().getBody(JSONObject.class);
                        JSONObject socialnetworks = (JSONObject) jsonObject.get("socialnetworks");

                        JSONObject twitter = (JSONObject) socialnetworks.get("twitter");
                        String twittername = (String) twitter.get("nickname");

                        try {

                            DynamicRouteBuilder dynamicRouteBuilder = new DynamicRouteBuilder("twitter://timeline/user?type=polling&user=" + twittername + "&count=1&numberOfPages=1&consumerKey={{twitter.consumerKey}}&consumerSecret={{twitter.consumerSecret}}&accessToken={{twitter.accessToken}}&accessTokenSecret={{twitter.accessTokenSecret}}",
                                    "direct:twitterresult",
                                    "eventFilePooler"); //dynamic route name
                            exchange.getContext().addRoutes(dynamicRouteBuilder);
                        } catch (IllegalArgumentException exept) {
                            System.out.println("ERROR in DynamicRouteBuilder: " + exept);
                            twitter.put("error", "applicant did not insert twitter-username");
                            exchange.getContext().createProducerTemplate().sendBody("direct:twitterresult", jsonObject);

                            PollingConsumer consumer = getContext().getEndpoint("direct:eventFilePooler").createPollingConsumer();
                            consumer.start();
                        }
                    }
                })
                .to("direct:eventFilePooler");

        //TODO instead of twitterresult it should go to the aggregator - change it in the dynamicRouteBuilder and delete the part bellow
        from("direct:twitterresult")
                .transform(body().convertToString())
                //.to("file:target/messages/twitter");
                .to("direct:startAggregator");
        
        
    }
}
