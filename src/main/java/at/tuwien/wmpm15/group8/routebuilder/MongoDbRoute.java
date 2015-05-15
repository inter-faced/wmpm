package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

public class MongoDbRoute  extends RouteBuilder {
	public void configure() {


		PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:credentials.properties");//classpath:mongodb.properties

		from("direct:findAll")
		.to("mongodb:myDb?database={{mongodb.dbName}}&collection={{mongodb.ApplicantCollectionName}}&operation=findAll")
		.to("direct:resultFindAll");

		from("direct:resultFindAll")
		.split(body())
		.delay(5000)
		.setExchangePattern(ExchangePattern.InOnly)
		.process(new Processor() { 
			@Override
			public void process(Exchange exchange) throws Exception {
				Object msg = exchange.getIn().getBody();

				System.out.println(">> Applicant Object: " + msg);
			}
		})
		.transform(body().convertToString())

		.to("jms:queue:test.queue");


		// just for testing
		from("jms:queue:test.queue")
		.to ("file:target/messages/mongo");

	}

}
