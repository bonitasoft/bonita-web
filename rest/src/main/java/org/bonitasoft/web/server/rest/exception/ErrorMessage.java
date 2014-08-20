package org.bonitasoft.web.server.rest.exception;

public class ErrorMessage {

    private int status;
    private String type;
    private String message;
    
    public ErrorMessage(int status, String type, String message) {
        this.type = type;
        this.message = message;
        this.status = status;
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
}
