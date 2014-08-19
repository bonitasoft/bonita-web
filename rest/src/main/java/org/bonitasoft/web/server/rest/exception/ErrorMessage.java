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

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
