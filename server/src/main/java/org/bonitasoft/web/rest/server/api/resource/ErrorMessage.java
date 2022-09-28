/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.resource;

import static org.bonitasoft.web.toolkit.client.common.json.JSonUtil.escape;

import org.restlet.ext.jackson.JacksonRepresentation;

/**
 * Representation for error entity
 *
 * @author Colin Puy
 */
public class ErrorMessage {

    private String exception; // might be 'type' with simple name of exception
    private String message;

    // DO NOT PUT stacktrace, this is not coherent with old API toolkit but as a client of REST API, I do not need stacktrace.

    public ErrorMessage() {
        // empty constructor for json serialization
    }

    public ErrorMessage(final Throwable t) {
        if (t != null) {
            exception = t.getClass().toString();
            setMessage(t.getMessage());
        }
    }

    public String getException() {
        return exception;
    }

    public void setException(final String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = escape(message);
    }

    public JacksonRepresentation<ErrorMessage> toEntity() {
        return new JacksonRepresentation<>(this);
    }
}
