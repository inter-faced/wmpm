package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

/**
 * Created by samuel on 6/16/15.
 */
public class InvitationRoute extends RouteBuilder {

    public void configure() throws Exception {

        // important note: log in with the credentials provided on Google Drive in order to be able to use this route

        // configure properties component for credentials.properties
        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:credentials.properties");

        from("direct:invite")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message msg = exchange.getIn();
                    }
                })
                .to("{{smtps.outserver}}?username={{smtps.username}}&password={{smtps.password}}&to={{smtps.to}}&debugMode=false");


    }
}
