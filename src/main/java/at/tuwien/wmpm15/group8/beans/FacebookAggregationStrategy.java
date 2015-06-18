package at.tuwien.wmpm15.group8.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.json.simple.JSONObject;


public class FacebookAggregationStrategy implements AggregationStrategy {
	public FacebookAggregationStrategy() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
	     JSONObject oldBody = oldExchange.getIn().getBody(JSONObject.class);

         JSONObject socialnetworks = (JSONObject) oldBody.get("socialnetworks");
         JSONObject facebook = (JSONObject) socialnetworks.get("facebook");

         if (newExchange == null) {
             //System.out.println("Error retrieving data from twitter");
             facebook.put("error", "facebook-link of applicant is wrong");

             return oldExchange;
         } else {

           
        	  JSONObject newBody = newExchange.getIn().getBody(JSONObject.class);
        	  facebook.put("name", newBody.get("name"));
              
         return oldExchange;
         }

	}

}
