package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

public class SendToDepartment extends RouteBuilder {

    public void configure() throws Exception {

        // important note: log in with the credentials provided on Google Drive in order to be able to use this route

        // configure properties component for credentials.properties
        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");

        from("direct:mailqueue")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message msg = exchange.getIn();
                        Object msgBody = exchange.getIn().getBody();

                        JSONParser jsonParser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(msgBody.toString());
                        JSONObject idObj = (JSONObject) jsonObject.get("_id");

                        String firstName = (String) jsonObject.get("firstName");
                        String lastName = (String) jsonObject.get("lastName");
                        String id = idObj.get("$oid").toString();

                        msg.setHeader("subject", "ID: " + id + ", " + firstName + " " + lastName);

                        msg.addAttachment("LOR.jpg", new DataHandler(new FileDataSource("src/data/attachments/LOR.jpg")));




                    }
                })
                .to("{{smtps.outserver}}?username={{smtps.username}}&password={{smtps.password}}&to={{smtps.to}}&debugMode=false");


    }


}
