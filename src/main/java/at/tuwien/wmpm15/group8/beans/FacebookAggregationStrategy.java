package at.tuwien.wmpm15.group8.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.json.simple.JSONObject;


public class FacebookAggregationStrategy implements AggregationStrategy {
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		return oldExchange;
	     

	}

}
