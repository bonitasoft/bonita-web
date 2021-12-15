/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.utils;

import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Conditions;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;

public class RequestBuilder {

    private Request request;

    public RequestBuilder(final String uri) {
        request = new Request();
        request.setResourceRef(uri);
    }

    public RequestBuilder setConditions(Conditions conditions) {
        request.setConditions(conditions);
        return this;
    }
    
    public Response get() {
        final Client client = new Client(Protocol.HTTP);
        request.setMethod(Method.GET);
        return client.handle(request);
    }

    public Response post(final String value) {
        final Client client = new Client(Protocol.HTTP);
        request.setMethod(Method.POST);
        request.setEntity(value, MediaType.APPLICATION_JSON);
        return client.handle(request);
    }
    
    public Response post() {
        final Client client = new Client(Protocol.HTTP);
        request.setMethod(Method.POST);
        return client.handle(request);
    }

    public Response put(final String value) {
        final Client client = new Client(Protocol.HTTP);
        request.setMethod(Method.PUT);
        request.setEntity(value, MediaType.APPLICATION_JSON);
        return client.handle(request);
    }

    public Response delete() {
        final Client client = new Client(Protocol.HTTP);
        request.setMethod(Method.DELETE);
        return client.handle(request);
    }


}
