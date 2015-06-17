package at.tuwien.wmpm15.group8.beans;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.json.simple.JSONObject;

public class AggregatorAggregationStrategy implements AggregationStrategy {
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
	
		if (oldExchange == null) {
			return newExchange;
		}
		
		Message msg=oldExchange.getIn();
		Object msgBody =oldExchange.getIn().getBody();

		
		
		
		JSONObject newBody = newExchange.getIn().getBody(JSONObject.class);
		JSONObject socialnetworks = (JSONObject) newBody.get("socialnetworks");
        JSONObject twitter = (JSONObject) socialnetworks.get("twitter");
        String twittername = (String) twitter.get("nickname");
        String favouritesCount = (String) twitter.get("favouritesCount");
        String followersCount = (String) twitter.get("followersCount");
        String tweetCount = (String) twitter.get("followersCount");
        System.out.println("Followers count: $(followersCount)");
                
        
        JSONObject oldBody = oldExchange.getIn().getBody(JSONObject.class);
        JSONObject socialnetworksOld = (JSONObject) oldBody.get("socialnetworks");
        JSONObject twitterOld = (JSONObject) socialnetworks.get("twitter");
        twitterOld.put("favouritesCount", favouritesCount);
        twitterOld.put("followersCount", followersCount);
        twitterOld.put("tweetCount", tweetCount);

       
		
    //    oldExchange.getIn().setBody(body, type);
	//	String oldBody = oldExchange.getIn().getBody(String.class);
	//	String newBody = newExchange.getIn().getBody(String.class);
       // String body = oldBody + newBody;
	//	oldExchange.getIn().setBody();
		return oldExchange;
	}
}
