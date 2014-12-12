package org.bonitasoft.console.common.server.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

public class TenantFolder {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(TenantFolder.class.getName());

    public TenantFolder() {
    }

    public boolean isInTempFolder(final File file, final WebBonitaConstantsUtils webBonitaConstantsUtils) {
        return isInFolder(file, webBonitaConstantsUtils.getTempFolder());
    }

    public boolean isInFolder(final File file, final File parentFolder) {
        try {
            if (!file.getCanonicalPath().startsWith(parentFolder.getCanonicalPath())) {
                throw new IOException();
            }
        } catch (final IOException e) {
            final String errorMessage = "Error when verifying the target path of the file " + file.getAbsolutePath()
                    + ". For security reasons, access to paths other than "
                    + parentFolder.getAbsolutePath()
                    + " is restricted.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            return false;
        }

        return true;
    }

}
