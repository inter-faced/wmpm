package at.tuwien.wmpm15.group8.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.json.simple.JSONObject;


public class AggregatorAggregationStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        //oldBody from Facebook
        JSONObject oldBody = oldExchange.getIn().getBody(JSONObject.class);
        //newBody from Twitter
        JSONObject newBody = newExchange.getIn().getBody(JSONObject.class);

        JSONObject oldsocialnetworks = (JSONObject) oldBody.get("socialnetworks");
        JSONObject newsocialnetworks = (JSONObject) newBody.get("socialnetworks");

        oldsocialnetworks.remove("twitter");

        //get new Twitter Object from newBody and place it into oldsocialnetworks
        JSONObject newtwitter = (JSONObject) newsocialnetworks.get("twitter");
        oldsocialnetworks.put("twitter", newtwitter);

        return oldExchange;
    }
}