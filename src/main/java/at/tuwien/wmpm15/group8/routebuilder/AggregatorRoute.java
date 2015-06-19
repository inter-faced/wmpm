package at.tuwien.wmpm15.group8.routebuilder;

import at.tuwien.wmpm15.group8.beans.AggregatorAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;

//import org.apache.camel.component.hawtdb.HawtDBAggregationRepository;

public class AggregatorRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		
		//aggregate message from db with twitter message
		//from("direct:startAggregator")
		from("direct:startAggregator")
		.log("To aggregate from Twitter: ${body} with correlation key ${header.id}" + body().getClass())
		.aggregate(header("id"), new AggregatorAggregationStrategy())
		//.aggregate(header("id"))//.to("mock:result")
		.completionSize(2)
		//.aggregationRepository(myRepo)
		.log("Aggregation Result Database and Twitter ${body}")
				.transform(body().convertToString())
				.to("file:target/messages/intermediateProfile");
		
	}

}
