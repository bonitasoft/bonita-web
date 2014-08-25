package org.bonitasoft.web.server.rest.exception;

import javax.ws.rs.core.Response.Status;

public class ErrorMessage {

    private int status;
    private String type;
    private String message;
    
    public ErrorMessage(Exception exception) {
        this.type = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
    }

    public String getType() {
        return type;
    }
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
    
    public ErrorMessage withStatus(Status status) {
        this.status = status.getStatusCode();
        return this;
    }
}
