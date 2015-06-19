package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.json.simple.JSONObject;

import javax.activation.DataHandler;

public class ContentEnricherRoute extends RouteBuilder {

    public ContentEnricherRoute() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void configure() throws Exception {


        from("direct:startContentEnricher"
        )
                .log("CONTENTENR-->>>Body before file fetch: " + "${body}")
                        //Enrich with CV (id_CV.pdf)
                .pollEnrich("{{ftp.server}}&binary=true",//&fileName=LOR3.jpg , new MyAggregationStrategy())
                        new AggregationStrategy() {
                            public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                                if (newExchange == null) {
                                    return oldExchange;
                                }
                                JSONObject http = oldExchange.getIn().getBody(JSONObject.class);
                                oldExchange.getIn().setBody(http);

                                byte[] ftp = newExchange.getIn().getBody(byte[].class);
                                oldExchange.getIn().addAttachment("LOR3.jpg", new DataHandler(ftp, "image/jpeg"));

                                return oldExchange;
                            }
                        })

                .log("CONTENTENR-->>>Body after file fetch: " + "${body}")
                .to("direct:mailqueue");
/*

        from("direct:startContentEnricher2")
                .log("CONTENTENR-->>>Body before file fetch: " + "${body}")
                //Example of Dynamic Filename
                .pollEnrich("{{ftp.server}}&fileName=${header.id}_CL.pdf&binary=true&move=.done&disconnect=true",//&fileName=LOR3.jpg , new MyAggregationStrategy())
                        new AggregationStrategy() {
                            public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                                if (newExchange == null) {
                                    return oldExchange;
                                }
                                JSONObject http = oldExchange.getIn().getBody(JSONObject.class);
                                oldExchange.getIn().setBody(http);

                                byte[] ftp = newExchange.getIn().getBody(byte[].class);
                                oldExchange.getIn().addAttachment("${header.id}_CL.pdf", new DataHandler(ftp, "application/pdf"));

                                return oldExchange;
                            }
                        })

                .log("CONTENTENR-->>>Body after file fetch: " + "${body}")
                .to("direct:startContentEnricher3");

        from("direct:startContentEnricher3")
                .log("CONTENTENR-->>>Body before file fetch: " + "${body}")

                .pollEnrich("{{ftp.server}}&fileName=LOR3.jpg&binary=true&move=.done&disconnect=true",//&fileName=LOR3.jpg , new MyAggregationStrategy())
                        new AggregationStrategy() {
                            public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                                if (newExchange == null) {
                                    return oldExchange;
                                }
                                JSONObject http = oldExchange.getIn().getBody(JSONObject.class);
                                oldExchange.getIn().setBody(http);

                                byte[] ftp = newExchange.getIn().getBody(byte[].class);
                                oldExchange.getIn().addAttachment("${header.id}_RL.jpg", new DataHandler(ftp, "image/jpeg"));

                                return oldExchange;
                            }
                        })
                .log("CONTENTENR-->>>Body after file fetch: " + "${body}")

*/
        //          .to("direct:mailqueue");
    }
}
