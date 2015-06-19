package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.json.simple.JSONObject;

/**
 * Created by samuel on 6/16/15.
 */
public class AnswerConsumer extends RouteBuilder {
    private String status;

    @Override
    public void configure() throws Exception {
        from("direct:consumer")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message msg = exchange.getIn();

                        JSONObject jsonObject = exchange.getIn().getBody(JSONObject.class);
                        JSONObject idObj = (JSONObject) jsonObject.get("_id");
                        JSONObject prefObj = (JSONObject) jsonObject.get("jobpreferences");

                        status = (String) jsonObject.get("Status");
                        String firstName = (String) jsonObject.get("firstName");
                        String lastName = (String) jsonObject.get("lastName");
                        String id = idObj.get("$oid").toString();
                        String pref1 = prefObj.get("preference1").toString();
                        String msgContent = "";

                        String header = "Your application as " + pref1 + ", ID: " + id + " has been " + status;

                        if (status == "accepted") {
                            msgContent = "Dear " + firstName + " " + lastName + ", \n" +
                                    "You are so great we want to meet you!";

                        } else {
                            msgContent = "Dear " + firstName + " " + lastName + ", \n" +
                                    "Your resume sucks so bad, that we regret even reading it!";
                            if (status == null) {
                                header = "Your application as " + pref1 + ", ID: " + id + " has been rejected (unqualified)";
                            }

                        }


                        msg.setHeader("subject", header);
                        msg.setBody(msgContent);
                        msg.removeAttachment("LOR4.jpg");
                    }
                })
                .log(String.valueOf(body()))

                .choice()
                .when(header("subject").contains("accepted"))
                .to("direct:invite")
                .otherwise()
                .to("jms:queue:email.queue");
    }
}
