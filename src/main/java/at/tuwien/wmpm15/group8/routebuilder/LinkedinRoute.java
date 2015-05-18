package at.tuwien.wmpm15.group8.routebuilder;

import org.apache.camel.Component;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.linkedin.LinkedInComponent;
import org.apache.camel.component.linkedin.LinkedInConfiguration;
import org.apache.camel.component.linkedin.LinkedInEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;

public class LinkedinRoute extends RouteBuilder{
	

	@Override
	public void configure() throws Exception {
		
		LinkedInComponent lc = getContext().getComponent("linkedin", LinkedInComponent.class);
    	
     	LinkedInConfiguration configuration = new LinkedInConfiguration();
    	configuration.setClientId("77zu5pjrv3xrip");
    	configuration.setClientSecret("czIgyyo285etXYpV");
    	configuration.setUserName("Alexandra Negoescu");
    	configuration.setUserPassword("password");
    	configuration.setRedirectUri("http://localhost");
    	
    	lc.setConfiguration(configuration);
        		
		
		from("linkedin://people/connections?consumer.timeUnit=SECONDS&consumer.delay=30")
	   .log("-->>>Connections fetched: " + "${body}")
		.to("file:target/messages/linkedin")
		.log("-->>>Final linkedin message: " + "${body}");
		
		/*get profile
		 from("direct:linkedinPreProc")
		.log("-->>>Profile fetched: " + "${body}")
		.to("linkedin://people/people")
		.log("-->>>Final thing: " + "${body}");
		*/
	}

}
