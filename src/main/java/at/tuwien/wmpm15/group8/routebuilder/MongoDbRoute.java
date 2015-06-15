package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import at.tuwien.wmpm15.group8.beans.ProcessCriteria;

public class MongoDbRoute  extends RouteBuilder {

	public void configure() {

		
		PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:credentials.properties");



		from("mongodb:myDb?database={{mongodb.webdbName}}&collection={{mongodb.webdbApplicantsCollectionCapped}}&tailTrackIncreasingField=increasing&cursorRegenerationDelay=60000ms")
		.id("tailableCursorConsumer1")
		// .autoStartup(false)
		.to("direct:inputApplicants","direct:savetodb" );//TODO remove savetodb from here
		
		
		from("direct:inputApplicants" )
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				Message msg=exchange.getIn();
				Object msgBody =exchange.getIn().getBody();


				System.out.println(">> Applicant Object: " + msgBody);

				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(msgBody.toString());
				JSONObject idObj = (JSONObject) jsonObject.get("_id");

				String id =   idObj.get("$oid").toString();
				//System.out.println(">> Applicant ID: "  + id);
				msg.setBody(jsonObject);
				msg.setHeader("id", id);

			}
		})
		//.transform(body().convertToString())
		.bean(ProcessCriteria.class)
		.choice()
		.when(header("status").isEqualTo("qualified"))
		//.to("jms:queue:applicant.queue")
		.to("direct:multicast")
		.log("Applicant qualified!")

		.otherwise()
		.to("jms:queue:email.queue")
		.log("Applicant not qualified!");


		// just for testing
/*		from("jms:queue:multicast.queue")
		.to ("file:target/messages/mongo");*/


	}

}
