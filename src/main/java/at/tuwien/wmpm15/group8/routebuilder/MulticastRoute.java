package at.tuwien.wmpm15.group8.routebuilder;


import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MulticastRoute extends RouteBuilder {

    public void configure() {

        from("jms:queue:applicant.queue")
                .process(new Processor() { // set message header ID
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        Message msg = exchange.getIn();
                        String msgbody = exchange.getIn().getBody(String.class);

                        JSONParser jsonParser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(msgbody.toString());
                        JSONArray socialnetworks = (JSONArray) jsonObject.get("socialnetworks");


                        //JSONObject twitter = (JSONObject) socialnetworks.get(1);
                        //System.out.println("--------" + msgbody.toString());
                        //String nickname =   twitter.get("nickname").toString();
                        //System.out.println(">> Applicant Twitter nickname: "  + nickname);
                        //msg.setHeader("id", id);


                    }
                })
                .multicast()
                .parallelProcessing()
                //.log("-->>testqueue: ${body}")
                .to("jms:queue:twitter.queue");
    }
}
