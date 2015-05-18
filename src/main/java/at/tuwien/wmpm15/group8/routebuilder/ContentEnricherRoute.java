package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class ContentEnricherRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		 from("file:target/messages/twitter")
		 .log("-->>>Body before file fetch: " + "${body}")
		// .to("file:target/messages/intermediateProfile")
		.log("-->>>Starting to build enriched message: " + "${body}")
		
		.pollEnrich("ftp://workflow2015.square7.ch:21/applications?username=workflow2015&password=group8", new AggregationStrategy() {
			 	public Exchange aggregate(Exchange oldExchange,
					Exchange newExchange) {
			 		System.err.println("--------<<<<Sysout>>>------ enricher: ");
					
					if (newExchange == null) {
					return oldExchange;
					}
					String old = oldExchange.getIn()
					.getBody(String.class);
					String ftp = newExchange.getIn()
					.getBody(String.class);
					String body = old + "\n" + ftp;
					oldExchange.getIn().setBody(body);
					return oldExchange;
					}
					})
					.log("-->>>Body after file fetch: " + "${body}")
					 .to("file:target/messages/finalProfile");
	}

}
