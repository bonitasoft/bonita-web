package org.bonitasoft.console.client.angular;

/**
 * @author Vincent Elcrin
 */
public class AngularResourceRoot {

    public String contextualize(String url) {
        return "../portal.js/" + url;
    }
}
