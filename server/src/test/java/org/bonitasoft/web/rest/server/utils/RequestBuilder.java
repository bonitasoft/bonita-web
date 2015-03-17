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
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;

public class RequestBuilder {

    private final String uri;

    public RequestBuilder(final String uri) {
        this.uri = uri;
    }

    public Response get() {
        final Client client = new Client(Protocol.HTTP);
        return client.handle(new Request(Method.GET, uri));
    }

    public Response post(final String value) {
        final Client client = new Client(Protocol.HTTP);
        final Request request = new Request(Method.POST, uri);
        request.setEntity(value, MediaType.APPLICATION_JSON);
        return client.handle(request);
    }

    public Response put(final String value) {
        final Client client = new Client(Protocol.HTTP);
        final Request request = new Request(Method.PUT, uri);
        request.setEntity(value, MediaType.APPLICATION_JSON);
        return client.handle(request);
    }
}
