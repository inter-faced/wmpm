package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.util.Random;

/**
 * Created by alex on 10.06.15.
 * Simulation of department process
 */
public class DepartmentAnswerRoute extends RouteBuilder {
    public void configure() throws Exception {

        // important note: log in with the credentials provided on Google Drive in order to be able to use this route

        // configure properties component for credentials.properties
        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");

        from("direct:department")
                .delay(getRandomTime(5000, 0)) //simulating answer after random amount of time
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message msg = exchange.getIn();

                        JSONObject jsonObject = exchange.getIn().getBody(JSONObject.class);
                        JSONObject idObj = (JSONObject) jsonObject.get("_id");

                        String firstName = (String) jsonObject.get("firstName");
                        String lastName = (String) jsonObject.get("lastName");
                        String id = idObj.get("$oid").toString();
                        String statusString = "";
                        Boolean status = getRandomBoolean();
                        if (status) {
                            statusString = "accepted";
                        } else {
                            statusString = "rejected";
                        }

                        jsonObject.put("Status", statusString);

                        msg.setHeader("subject", "ID: " + id + ", " + firstName + " " + lastName + ", STATUS: " + statusString);

                        msg.addAttachment("LOR.jpg", new DataHandler(new FileDataSource("src/data/attachments/LOR.jpg")));

                    }
                })
                .to("{{dep.outserver}}?username={{dep.username}}&password={{dep.password}}&to={{dep.to}}&debugMode=false", "direct:consumer");


    }


    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public int getRandomTime (int max, int min){
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

}
