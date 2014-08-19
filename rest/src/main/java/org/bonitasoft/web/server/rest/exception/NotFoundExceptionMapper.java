package org.bonitasoft.web.server.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.bonitasoft.engine.exception.NotFoundException;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        ErrorMessage error = new ErrorMessage(Status.NOT_FOUND.getStatusCode(), exception.getClass().getSimpleName(), exception.getMessage());
        return Response.status(Status.NOT_FOUND).entity(error).build();
    }

    
}
