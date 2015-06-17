package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class FacebookRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		 PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
	        pc.setLocation("classpath:credentials.properties");
	        
	       	        

	        from("direct:facebookpreproc")
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                	
                    Message msg = exchange.getIn();

                    String msgbody = exchange.getIn().getBody(String.class);

                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(msgbody.toString());
                    

                    JSONObject socialnetworks = (JSONObject) jsonObject.get("socialnetworks");
            
                    //TODO exception handling
                    JSONObject facebook = (JSONObject) socialnetworks.get("facebook");
            
                    String facebookLink = (String) facebook.get("directlink");
                    System.out.println("------>>>>>>> get data from facebookLINK:" + facebookLink);
                    JSONObject idObj = (JSONObject) jsonObject.get("_id");
                    String id =   idObj.get("$oid").toString();
                    
                    msg.setBody(facebook);
    				msg.setHeader("id", id);
                    
                }
            })
            .log("-->>Facebook Filtered Body: ${body}")
           // .transform(body().convertToString())
            .to("mock:facebookLink")
            .log("-->>Facebook output: ${body}");

    from("direct:facebookresult")
            .to("file:target/messages/facebook");
	}
	        	

}
