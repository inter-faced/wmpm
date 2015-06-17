package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import at.tuwien.wmpm15.group8.beans.ProcessCriteria;

public class SaveToDbRoute  extends RouteBuilder {

	public void configure() {

		
		PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:credentials.properties");



		from("direct:savetodb")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				Message msg=exchange.getIn();
				Object msgBody =exchange.getIn().getBody();
			
				DBObject dbObject = (DBObject) JSON.parse(msgBody.toString());
				
				//System.out.println(">> Applicant Object to save: " + dbObject);

				msg.setBody(dbObject);


			}
		})
		.to("mongodb:myDb?database={{mongodb.webdbName}}&collection={{mongodb.webdbApplicantsCollectionEnriched}}&operation=insert");

		
		



	}

}
