package at.tuwien.wmpm15.group8.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.json.simple.JSONObject;
import twitter4j.Status;

public class TwitterAggregationStrategy implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

            JSONObject oldBody = oldExchange.getIn().getBody(JSONObject.class);

            JSONObject socialnetworks = (JSONObject) oldBody.get("socialnetworks");
            JSONObject twitter = (JSONObject) socialnetworks.get("twitter");

            if (newExchange == null) {
                //System.out.println("Error retrieving data from twitter");
                twitter.put("error", "twitter-username of applicant is wrong");

                return oldExchange;
            } else {

                Status status = newExchange.getIn().getBody(Status.class);

                // enriching the oldBody with twitter data
                twitter.put("favouritesCount", status.getUser().getFavouritesCount());
                twitter.put("followersCount", status.getUser().getFollowersCount());
                twitter.put("tweetCount", status.getUser().getStatusesCount());

                return oldExchange;
            }
        }
    }
