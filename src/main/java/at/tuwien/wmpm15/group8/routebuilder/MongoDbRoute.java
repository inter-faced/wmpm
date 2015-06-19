package at.tuwien.wmpm15.group8.routebuilder;

import at.tuwien.wmpm15.group8.beans.ProcessCriteria;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MongoDbRoute  extends RouteBuilder {

	public void configure() {

		PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:credentials.properties");


		from("mongodb:myDb?database={{mongodb.webdbName}}&collection={{mongodb.webdbApplicantsCollectionCapped}}&tailTrackIncreasingField=increasing&cursorRegenerationDelay=60000ms")
		.id("tailableCursorConsumer")
		.autoStartup(false)
		.to("direct:inputApplicants" );//TODO remove multicast & savetodb from here

		from("direct:inputApplicants")
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
	
				msg.setBody(jsonObject);
				msg.setHeader("id", id);

			}
		})

		.log("Start processing the application ${header.id} ...")
		.bean(ProcessCriteria.class)
		.choice()
		.when(header("status").isEqualTo("qualified"))
		//.to("jms:queue:applicant.queue")
		.to("direct:multicast")
		.log("Application ${header.id} passed the primary qualification process")

		.otherwise()
		.to("direct:consumer")
		.log("Application ${header.id} did not pass the primary qualification process");



	}

}
