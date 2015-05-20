package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hawtdb.HawtDBAggregationRepository;
import org.apache.camel.spi.AggregationRepository;

import at.tuwien.wmpm15.group8.beans.MyAggregationStrategy;

public class AggregatorRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		//set up HawtDB
		//AggregationRepository myRepo = new
			//	HawtDBAggregationRepository("myrepo", "src/data/myrepo.dat");
		
		from("direct:start")
		.log("Sending ${body} with correlation key ${header.id}")
		.aggregate(header("id"), new MyAggregationStrategy())
		.completionSize(3)
		//.aggregationRepository(myRepo)
		.log("Sending out ${body}")
		.to("mock:result");
		
	}

}
