package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

public class FtpRoute extends RouteBuilder {

    public void configure() throws Exception {

        // configure properties component for ftp.properties
        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:ftp.properties");

        /*
        * Upload files from src/data/attachments to FTP server specified in ftp.properties
        * ?noop just for testing
        */
            from("file:src/data/attachments?noop=true")
                    .log("Uploading file ${file:name}")
                    .to("{{ftp.serverUL}}")
                    .log("Uploaded file ${file:name} complete.");

        /*
        * Download files from FTP server and move them to hidden directory .done
        */
            from("{{ftp.serverDL}}")
                    .to("file:target/attachments")
                    .log("Downloaded file ${file:name} complete.");
    }


}
