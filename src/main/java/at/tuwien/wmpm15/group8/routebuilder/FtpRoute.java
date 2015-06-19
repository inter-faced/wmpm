package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

public class FtpRoute extends RouteBuilder {

    public void configure() throws Exception {

        // configure properties component for credentials.properties
        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");

        /*
        * Upload files from src/data/attachments to FTP server specified in credentials.properties
        * ?noop just for testing
        */
            from("file:src/data/attachments?noop=true")
                    .log("Uploading file ${file:name}")
                    .to("{{ftp.server}}&binary=true&disconnect=true")
                    .log("Uploaded file ${file:name} complete.");

    }


}
