package org.bonitasoft.web.toolkit.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestException;


public class RequestBuilder extends com.google.gwt.http.client.RequestBuilder {

    public RequestBuilder(final Method httpMethod, final String url) {
        super(httpMethod, url);
    }

    @Override
    public Request send() throws RequestException {
        String apiToken = getAPIToken();
        if (apiToken != null) {
            setHeader("X-Bonita-API-Token", apiToken);
        }
        return super.send();
    }

    protected String getAPIToken() {
        return UserSessionVariables.getUserVariable(UserSessionVariables.API_TOKEN);
    }

}
