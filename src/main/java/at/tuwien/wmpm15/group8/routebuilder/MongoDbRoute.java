package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MongoDbRoute  extends RouteBuilder {

	public void configure() {

		PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:credentials.properties");//classpath:mongodb.properties

		from("direct:findAll")
				.to("mongodb:myDb?database={{mongodb.webdbName}}&collection={{mongodb.webdbApplicantsCollection}}&operation=findAll")
				.to("direct:resultFindAll");

		from("direct:resultFindAll")
		.split(body())
		.delay(5000)
		.setExchangePattern(ExchangePattern.InOnly)
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				Message msg=exchange.getIn();
				Object msgBody =exchange.getIn().getBody();

				//System.out.println(">> Applicant Object: " + msgBody);

				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(msgBody.toString());
				JSONObject idObj = (JSONObject) jsonObject.get("_id");

				String id =   idObj.get("$oid").toString();
				//System.out.println(">> Applicant ID: "  + id);

				msg.setHeader("id", id);



			}
		})
		.transform(body().convertToString())

		.to("direct:multicast");

		/*
		// just for testing
		from("jms:queue:multicast.queue")
		.to ("file:target/messages/mongo");
		 */


	}

}
