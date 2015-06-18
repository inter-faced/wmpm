package at.tuwien.wmpm15.group8.routebuilder;


import org.apache.camel.Exchange;
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

                        if (twittername.trim().isEmpty()) {
                            twitter.put("error", "applicant did not insert twitter-username");
                            exchange.getIn().setHeader("twitterempty", true);
                        } else {
                                DynamicRouteBuilder dynamicRouteBuilder = new DynamicRouteBuilder("twitter://timeline/user?type=polling&numberOfPages=1&count=1&user=" + twittername + "&consumerKey={{twitter.consumerKey}}&consumerSecret={{twitter.consumerSecret}}&accessToken={{twitter.accessToken}}&accessTokenSecret={{twitter.accessTokenSecret}}",
                                        "direct:twitterresult",
                                        "eventFilePooler"); //dynamic route name
                                exchange.getContext().addRoutes(dynamicRouteBuilder);
                                exchange.getIn().setHeader("twitterempty", false);
                        }
                    }
                })
                //.log("headerid:   ${header.id} twitterempty: ${header.twitterempty}")
                .choice()
                .when(header("twitterempty").isEqualTo(true))
                .to("direct:twitterresult")
                .otherwise()
                .to("direct:eventFilePooler");

        //TODO instead of twitterresult it should go to the aggregator - change it in the dynamicRouteBuilder and delete the part bellow
        from("direct:twitterresult")
                .transform(body().convertToString())
                //.log("Bodyfilesave: ${body}")
                .to("file:target/messages/twitter");
    }
}
