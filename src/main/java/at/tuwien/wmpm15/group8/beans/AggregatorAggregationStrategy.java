package at.tuwien.wmpm15.group8.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.json.simple.JSONObject;

public class AggregatorAggregationStrategy implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

		if (newExchange == null) {
			System.out.println("-------newExchange is null!!!!");
			return oldExchange;
		}

		if (oldExchange == null) {
			System.out.println("-------oldExchange is null!!!!");
			return newExchange;
		}

		//oldBody from Database
		JSONObject newBody = oldExchange.getIn().getBody(JSONObject.class);
		//newBody comes with new Twitter Data
		JSONObject oldBody = newExchange.getIn().getBody(JSONObject.class);
		System.out.println("------oldbody " + oldBody);
		System.out.println("------newbody " + newBody);



		//remove old Twitter Object from the oldBody Json
		JSONObject oldsocialnetworks = (JSONObject) oldBody.get("socialnetworks");
		JSONObject newsocialnetworks = (JSONObject) newBody.get("socialnetworks");

		//System.out.println("------before oldsocialnetworks" + oldsocialnetworks);
		System.out.println("------thats  newsocialnetworks1" + newsocialnetworks);
		//oldsocialnetworks.remove("twitter");

		//get new Twitter Object from newBody and place it into oldsocialnetworks
		System.out.println("------thats  newsocialnetworks2" + newsocialnetworks);
		//JSONObject newtwitter = (JSONObject) newsocialnetworks.get("twitter");
		//System.out.println("------thats  newtwitter" + newtwitter);

		//oldsocialnetworks.put("twitter", newtwitter);

		//System.out.println("------after oldsocialnetworks" + oldsocialnetworks);

		return oldExchange;
	}
}