package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import at.tuwien.wmpm15.group8.beans.MyAggregationStrategy;

public class ContentEnricherRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		 from("file:target/messages/twitter")
		 .log("-->>>Body before file fetch: " + "${body}")
		 				
		.pollEnrich("{{ftp.serverDL}}", new MyAggregationStrategy())
					.log("-->>>Body after file fetch: " + "${body}")
					 .to("file:target/messages/finalProfile");
	}

}
