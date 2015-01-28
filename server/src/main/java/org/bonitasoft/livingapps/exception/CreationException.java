package org.bonitasoft.livingapps.exception;

public class CreationException extends Exception {

    public CreationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CreationException(String message) {
        super(message);
    }
}
