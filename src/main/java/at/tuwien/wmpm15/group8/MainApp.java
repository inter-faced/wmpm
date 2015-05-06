package at.tuwien.wmpm15.group8;

import at.tuwien.wmpm15.group8.routebuilder.AnExampleRoute;
import at.tuwien.wmpm15.group8.routebuilder.TwitterRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        //add all the Routes here
        context.addRoutes(new AnExampleRoute());
        context.addRoutes(new TwitterRoute());

        context.start();
    }

}

