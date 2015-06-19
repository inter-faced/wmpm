package at.tuwien.wmpm15.group8.routebuilder;

import at.tuwien.wmpm15.group8.beans.AggregatorAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;

public class AggregatorRoute extends RouteBuilder{

    @Override
    public void configure() throws Exception {

        //TODO: not needed because of missing facebook,
        from("direct:aggregatorstart")
                .aggregate(header("id"), new AggregatorAggregationStrategy())
                .completionSize(2)
                .to("direct:aggregatorend");

    }

}