package org.bonitasoft.web.server.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bonitasoft.engine.exception.BonitaException;

public class BonitaWebApplicationException extends WebApplicationException {

    private static final long serialVersionUID = 6494511446015486224L;

    public BonitaWebApplicationException(Status status, BonitaException exception) {
        this(status, new ErrorMessage(exception).withStatus(status));
    }
    
    public BonitaWebApplicationException(Status status, ErrorMessage message) {
        super(Response.status(status).entity(message.withStatus(status)).build());
    }
}
