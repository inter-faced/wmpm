package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class ContentEnricherRoute extends RouteBuilder {


	public ContentEnricherRoute() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure() throws Exception {


		// configure properties component for credentials.properties
		PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:credentials.properties");

		 from("direct:startContentEnricher")
		 .log("CONTENTENR-->>>Body before file fetch: " + "${body}")

				 .pollEnrich("{{ftp.server}}&fileName=LOR3.jpg",//, new MyAggregationStrategy())

						 new AggregationStrategy() {
							 public Exchange aggregate(Exchange oldExchange,
													   Exchange newExchange) {
								 if (newExchange == null) {
									 return oldExchange;
								 }
								 String oldmessage = oldExchange.getIn()
										 .getBody(String.class);
								 String ftp = newExchange.getIn()
										 .getBody(String.class);
								 String body = oldmessage + "\n" + ftp;
								 oldExchange.getIn().setBody(body);
								 System.out.println("ContentEnricherTest");
								 return oldExchange;
							 }
						 })





					.log("CONTENTENR-->>>Body after file fetch: " + "${body}")
					 .to("file:target/messages/finalProfile");
	}

}
