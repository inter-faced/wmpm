package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by alex on 15.06.15.
 */
public class MailQueue extends RouteBuilder {
    public void configure() throws Exception {
        from("jms:queue:email.queue")
                .to("direct:reject");

        //to do: dynamic address in to block
        from("direct:reject").to("{{smtps.outserver}}?username={{smtps.username}}&password={{smtps.password}}&to=pinoccio69@gmx.at&debugMode=false");

    }
}
