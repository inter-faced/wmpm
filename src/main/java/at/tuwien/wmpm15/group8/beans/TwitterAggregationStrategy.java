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


            String oldBody = oldExchange.getIn().getBody(String.class);


            Status status = newExchange.getIn().getBody(Status.class);

            JSONObject obj = new JSONObject();
            obj.put("favouritesCount", status.getUser().getFavouritesCount());
            obj.put("followersCount", status.getUser().getFollowersCount());
            obj.put("tweetCount", status.getUser().getStatusesCount());

            String newBody = obj.toString();


            String body = oldBody + newBody;
            oldExchange.getIn().setBody(body);
            return oldExchange;
        }
    }
