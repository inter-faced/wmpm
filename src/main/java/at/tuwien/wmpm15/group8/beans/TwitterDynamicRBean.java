package at.tuwien.wmpm15.group8.beans;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;

/**
 * Created by Interfaced on 17.05.15.
 */
public class TwitterDynamicRBean {

    public String route(@Body String body,
                        @Header(Exchange.SLIP_ENDPOINT) String previous) {
        return whereToGo(body, previous);
    }

    private String whereToGo(String body, String previous) {
        if (previous == null) {
            return "twitter://timeline/user?type=direct&user=" + body +"&consumerKey={{twitter.consumerKey}}&consumerSecret={{twitter.consumerSecret}}&accessToken={{twitter.accessToken}}&accessTokenSecret={{twitter.accessTokenSecret}}";
        } else if ("mock://a".equals(previous)) {
            return "language://simple:Bye ${body}";
        } else {
            return null;
        }
    }
}