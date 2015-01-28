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
 * @author Fabio Lombardi
 *
 */
public class InvalidPageZipContentException extends Exception {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;

    public InvalidPageZipContentException() {
        super();
    }

    public InvalidPageZipContentException(final String message) {
        super(message);
    }

    public InvalidPageZipContentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidPageZipContentException(final Throwable cause) {
        super(cause);
    }
}
