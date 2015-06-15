package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import at.tuwien.wmpm15.group8.beans.AggregatorAggregationStrategy;

public class ContentEnricherRoute extends RouteBuilder {

	public ContentEnricherRoute() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure() throws Exception {
		 from("direct:contentproc")
		 .log("CONTENTENR-->>>Body before file fetch: " + "${body}")
		 				
		.pollEnrich("file:src/data?fileName=message1.xml")//, new MyAggregationStrategy())
					.log("CONTENTENR-->>>Body after file fetch: " + "${body}")
					 .to("file:target/messages/finalProfile");
	}

}
