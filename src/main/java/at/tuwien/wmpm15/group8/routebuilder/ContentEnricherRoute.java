package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import at.tuwien.wmpm15.group8.beans.AggregatorAggregationStrategy;

public class ContentEnricherRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		 from("direct:startContentEnricher")
		 .log("CONTENTENR-->>>Body before file fetch: " + "${body}")
		 				
		.pollEnrich("file:src/data/attachments?fileName=Transcript.jpg")//, new MyAggregationStrategy())
					.log("CONTENTENR-->>>Body after file fetch: " + "${body}")
					 .to("file:target/messages/finalProfile");
	}

}
