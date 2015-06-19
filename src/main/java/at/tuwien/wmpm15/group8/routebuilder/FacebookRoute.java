package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.builder.RouteBuilder;


public class FacebookRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        //TODO: not needed because of missing facebook
        from("direct:fromulticast")
                .pollEnrich("something")
                .to("direct:aggregatorstart");
    }


}
