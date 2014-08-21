package org.bonitasoft.web.server.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bonitasoft.engine.exception.BonitaException;

public class BonitaWebApplicationException extends WebApplicationException {

    private static final long serialVersionUID = 6494511446015486224L;

    public BonitaWebApplicationException(Status status, BonitaException exception) {
        super(Response.status(status).entity(errorMessage(status, exception)).build());
    }

    private static ErrorMessage errorMessage(Status status, Exception exception) {
        return new ErrorMessage(status.getStatusCode(), exception.getClass().getSimpleName(), exception.getMessage());
    }
}
