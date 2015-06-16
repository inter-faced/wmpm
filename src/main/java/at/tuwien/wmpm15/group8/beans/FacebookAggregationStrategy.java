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

           
        	 
        	 //Status status = newExchange.getIn().getBody(Status.class);

             // enriching the oldBody with facebook data
            // facebook.put("interests",  getInterests());
           /*  twitter.put("favouritesCount", status.getUser().getFavouritesCount());
             twitter.put("followersCount", status.getUser().getFollowersCount());
             twitter.put("tweetCount", status.getUser().getStatusesCount());
*/
             return oldExchange;
         }

	}

}
