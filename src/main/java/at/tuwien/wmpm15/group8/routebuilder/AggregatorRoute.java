package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hawtdb.HawtDBAggregationRepository;
import org.apache.camel.spi.AggregationRepository;

import at.tuwien.wmpm15.group8.beans.AggregatorAggregationStrategy;

public class AggregatorRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		//set up HawtDB
		//AggregationRepository myRepo = new
			//	HawtDBAggregationRepository("myrepo", "src/data/myrepo.dat");
		
		/*from("direct:startAggregator")
		.log("Sending ${body} with correlation key ${header.id}")
		//.aggregate(header("id"), new MyAggregationStrategy())
		.aggregate(new MyAggregationStrategy()).header("id")
		.completionSize(2)
		//.aggregationRepository(myRepo)
		.log("Sending out ${body}")
		.to("file:target/messages/intermediateProfile");*/
		
		//aggregate message from db with twitter message
		//from("direct:startAggregator")
		from("direct:startAggregator")
		.log("To aggregate from Twitter: ${body} with correlation key ${header.id}")
		.aggregate(header("_id"), new AggregatorAggregationStrategy())
		//.aggregate(header("id"))//.to("mock:result")
		.completionSize(2)
		//.aggregationRepository(myRepo)
		.log("Aggregation Result Database and Twitter ${body}")
		.to("file:target/messages/intermediateProfile");
	}

}
