package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
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
                    .process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            Message msg = exchange.getIn();

                            String bodystring = exchange.getIn().getBody().toString();
                            String headerstring = exchange.getIn().getHeaders().toString();

                            System.out.println("FTPRouteTest \n body: " + bodystring + "\nheader= " + headerstring);
                           /* JSONObject idObj = (JSONObject) jsonObject.get("_id");

                            String id = idObj.get("$oid").toString();

                            msg.setHeader("oid", id);*/

                        }
                    })
                            //  .log("Uploading file ${file:name} ${header.oid}")
                    .to("{{ftp.server}}&binary=true")
                    .log("Uploaded file ${file:name} complete.");

        /*
        * Download files from FTP server and move them to hidden directory .done
        */
        /*from("{{ftp.server}}&delay=10s&move=.done&binary=true")

                    .to("file:target/messages/attachments")
                    .log("Downloaded file ${file:name} complete.");
                */
       /* 
         * Download files from FTP server 
         
             from("{{ftp.serverDL}}")
		             .log("-->>>Body from FTP: " + "${body}")
                     .to("jms:queue:incomingDocuments")
                     .log("-->>>Body in qu FTP: " + "${body}");
                     //.log("Downloaded file ${file:name} complete.")
*/    }


}
