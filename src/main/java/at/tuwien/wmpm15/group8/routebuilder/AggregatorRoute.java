package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hawtdb.HawtDBAggregationRepository;
import org.apache.camel.spi.AggregationRepository;
import org.json.simple.JSONObject;

import at.tuwien.wmpm15.group8.beans.AggregatorAggregationStrategy;

public class AggregatorRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		
		//aggregate message from db with twitter message
		//from("direct:startAggregator")
		from("direct:startAggregator")
		.log("To aggregate from Twitter: ${body} with correlation key ${header.id}")
		.aggregate(header("id"), new AggregatorAggregationStrategy())
		//.aggregate(header("id"))//.to("mock:result")
		.completionSize(2)
		//.aggregationRepository(myRepo)
		.log("Aggregation Result Database and Twitter ${body}")
		.to("file:target/messages/intermediateProfile");
		
	}

}
