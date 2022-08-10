package org.bonitasoft.console.common.server.utils;

import java.io.IOException;

/**
 * Technical exception thrown when we try to read/write a file out of the authorized file system path.
 *
 * @author Julien Mege
 */
public class UnauthorizedFolderException extends IOException {

    private static final long serialVersionUID = 1071342750973031637L;

    public UnauthorizedFolderException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public UnauthorizedFolderException(final String message) {
        super(message);
    }

}
