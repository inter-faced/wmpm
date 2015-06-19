package at.tuwien.wmpm15.group8.beans;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.json.simple.JSONObject;

import twitter4j.Status;

public class AggregatorAggregationStrategy implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		
		if (oldExchange == null){
			return newExchange;
		}
		
		JSONObject oldBody = oldExchange.getIn().getBody(JSONObject.class);
		JSONObject newBody = newExchange.getIn().getBody(JSONObject.class);
       
		if(newExchange==null){
			return oldExchange;
		}
		
		JSONObject socialnetworks = (JSONObject) oldBody.get("socialnetworks");
        JSONObject twitter = (JSONObject) socialnetworks.get("twitter");
        System.out.println("----To aggregate old" + twitter);
        System.out.println("-----New body:" + newBody.toString());
     
        JSONObject newsocialnetworks = (JSONObject) newBody.get("socialnetworks");
		JSONObject newtwitter = (JSONObject) socialnetworks.get("twitter");
		 System.out.println("----To aggregate new" + newtwitter);
		System.out.println("------New twitter " + newtwitter);
		String favouritesCount = (String) newtwitter.get("favouritesCount");
		String followersCount = (String) newtwitter.get("followersCount");
		String tweetCount = (String) newtwitter.get("tweetCount");
		
		twitter.put("favouritesCount", favouritesCount);
		twitter.put("followersCount", followersCount);
		twitter.put("tweetCount", tweetCount);
		System.out.println("FINAL result aggregator:" + twitter);
		
		return oldExchange;
	}
}