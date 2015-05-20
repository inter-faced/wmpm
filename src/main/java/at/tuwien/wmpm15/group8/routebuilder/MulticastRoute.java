package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.builder.RouteBuilder;


public class MulticastRoute extends RouteBuilder {

    public void configure() {

        from("direct:multicast")
                .multicast()
                .parallelProcessing()
                //TODO add other social network separated by comma
                //.to("direct:twitterpreproc", "direct:contentproc");//, "direct:startAggregator");
        		.to("direct:twitterpreproc", "direct:startAggregator");
        
    }
}
