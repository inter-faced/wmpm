package at.tuwien.wmpm15.group8.routebuilder;

import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import at.tuwien.wmpm15.group8.utils.CredentialsReader;



public class FacebookRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		 PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
	        pc.setLocation("classpath:credentials.properties");
	        
	        Properties prop= CredentialsReader.read();
	        
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
                   
                    if (facebookLink.trim().isEmpty()) {
                        facebook.put("error", "applicant did not insert facebook link");
                        exchange.getIn().setHeader("facebookempty", true);
                    } else {
                    	
                    //	DynamicRouteBuilderFacebook dynamicRouteBuilderFacebook = new DynamicRouteBuilderFacebook("facebook://" + facebookLink + "facebook:"+facebookLink+"/?access_token={{facebook.accessToken}}",
                    	DynamicRouteBuilderFacebook dynamicRouteBuilderFacebook = new DynamicRouteBuilderFacebook("facebook://" + facebookLink + "?query=cheese&reading.limit=10&reading.locale=en.US&reading.since=" + 1439903593 + "&consumer.initialDelay=1000&" + "?&oAuthAccessToken={{facebook.accessToken}}&oAuthAppId=={{facebook.appId}}&oAuthAppSecret=={{facebook.appSecret}}",
                    			"direct:facebookresult",
                                "eventFilePoolerfacebook"); //dynamic route name
                    
                        exchange.getContext().addRoutes(dynamicRouteBuilderFacebook);
                        exchange.getIn().setHeader("facebookempty", false);
                    	
                    }
                }
            })
             .log("facebook ${{facebook.accessToken}}")    
            .log("-->>Facebook Filtered Body: ${body}")
           /* .transform(body().convertToString())
            .to("mock:facebookLink")*/
            //.log("-->>Facebook output: ${body}")
            
             .choice()
                .when(header("facebookempty").isEqualTo(true))
                .to("direct:facebookresult")
                .otherwise()
                .to("direct:eventFilePoolerfacebook");
	        
    from("direct:facebookresult")
            .to("file:target/messages/facebook");
	}
	        	

}
