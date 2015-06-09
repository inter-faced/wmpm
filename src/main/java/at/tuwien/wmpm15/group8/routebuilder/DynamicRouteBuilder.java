package at.tuwien.wmpm15.group8.routebuilder;

import at.tuwien.wmpm15.group8.beans.TwitterAggregationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;

class DynamicRouteBuilder extends RouteBuilder {

    private final String enrichUri;
    private final String outUri;
    private final String routeId;
    private static final AggregationStrategy TWITTER_AGGREGATION_STRATEGY = new TwitterAggregationStrategy();

    public DynamicRouteBuilder(String enrichUri, String outUri, String routeId) {
        this.enrichUri = enrichUri;
        this.outUri = outUri;
        this.routeId = routeId;
    }

    //Dynamic route creation
    @Override
    public void configure() throws Exception {
        from("direct:"+routeId).id(routeId) //Dynamic entry point
                .pollEnrich(enrichUri, 2000, TWITTER_AGGREGATION_STRATEGY)
                .to(outUri) //Exit point, where we can wait statically to continue processing
                .process(new ShutDownProcessor());
    }

    //Route shutdown it self after completion
    private final class ShutDownProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            CamelContext camelContext = exchange.getContext();
            camelContext.getInflightRepository().remove(exchange);
            camelContext.stopRoute(routeId);
            camelContext.removeRoute(routeId);
        }
    }
}
