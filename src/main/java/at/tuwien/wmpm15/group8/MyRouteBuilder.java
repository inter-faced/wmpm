package at.tuwien.wmpm15.group8;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

/**
 * A Camel Java DSL Router
 */

public class MyRouteBuilder extends RouteBuilder {

    public void configure() {



        // here is a sample which processes the input files
        // (leaving them in place - see the 'noop' flag)
        // then performs content based routing on the message using XPath
        from("file:src/data?noop=true")
            .choice()
                .when(xpath("/person/city = 'London'"))
                    .log("UK message")
                    .to("file:target/messages/uk")
                .otherwise()
                    .log("Other message")
                    .to("file:target/messages/others");



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
