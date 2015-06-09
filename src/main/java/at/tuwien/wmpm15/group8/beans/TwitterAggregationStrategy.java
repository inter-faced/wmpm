package at.tuwien.wmpm15.group8.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.json.simple.JSONObject;
import twitter4j.Status;

public class TwitterAggregationStrategy implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                System.out.println("------old exchange is null: " + oldExchange.toString());
                return newExchange;
            }
            if (newExchange == null) {
                System.out.println("------new exchange is null: " + oldExchange.toString());
                return oldExchange;
            }

            System.out.println("------new exchange is null: " + oldExchange.toString());

            Status status = newExchange.getIn().getBody(Status.class);
            JSONObject oldBody = oldExchange.getIn().getBody(JSONObject.class);

            // enriching the oldBody with twitter data
            JSONObject socialnetworks = (JSONObject) oldBody.get("socialnetworks");
            //TODO exception handling if no twitter data there
            JSONObject twitter = (JSONObject) socialnetworks.get("twitter");
            twitter.put("favouritesCount", status.getUser().getFavouritesCount());
            twitter.put("followersCount", status.getUser().getFollowersCount());
            twitter.put("tweetCount", status.getUser().getStatusesCount());

            // oldBody is now enriched with the twitter data
            oldExchange.getIn().setBody(oldBody);
            return oldExchange;
        }
    }
