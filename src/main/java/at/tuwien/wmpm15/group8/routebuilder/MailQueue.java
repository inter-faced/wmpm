package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by alex on 15.06.15.
 */
public class MailQueue extends RouteBuilder {
    public void configure() throws Exception {
        from("direct:queue")
                // send it to the seda queue that is async
                .to("seda:next")
                        // return a constant response
                .transform(constant("OK"));

        from("seda:next").to("mock:result");
    }
}
