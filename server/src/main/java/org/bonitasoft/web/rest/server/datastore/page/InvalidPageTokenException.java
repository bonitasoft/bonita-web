/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.page;

/**
 * @author Anthony Birembaut
 *
 */
public class InvalidPageTokenException extends Exception {

    /**
     * UID
     */
    private static final long serialVersionUID = -1593131519230620935L;

    public InvalidPageTokenException() {
        super();
    }

    public InvalidPageTokenException(final String message) {
        super(message);
    }

    public InvalidPageTokenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidPageTokenException(final Throwable cause) {
        super(cause);
    }
}
