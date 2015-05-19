package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

public class SendToDepartment extends RouteBuilder {

    public void configure() throws Exception {

        from("file:src/data/attachments?noop=true&fileName=LOR.jpg")
                .log("Composing Mail")
                .setHeader("subject", simple("New application"))
                .log("Sending Mail")
                .to("smtps://smtp.gmail.com?username=user@gmail.com&password=pw&to=recipient@gmail.com&debugMode=true")
                .log("Mail sent");
    }


}
