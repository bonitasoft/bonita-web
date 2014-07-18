package org.bonitasoft.console.client.angular;

/**
 * @author Vincent Elcrin
 */
public class AngularProxiedResourceRoot extends AngularResourceRoot {

    @Override
    public String contextualize(String url) {
        return "http://127.0.0.1:9000/" + url;
    }
}
