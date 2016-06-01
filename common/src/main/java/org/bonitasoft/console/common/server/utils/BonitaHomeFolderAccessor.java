package org.bonitasoft.console.common.server.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;

public class BonitaHomeFolderAccessor {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(BonitaHomeFolderAccessor.class.getName());

    public BonitaHomeFolderAccessor() {
    }

    public File getTempFile(final String filePath, final Long tenantId) throws IOException {
        return new File(getCompleteTempFilePath(filePath, tenantId));
    }

    public String getCompleteTempFilePath(final String filePath) throws IOException {
        return getCompleteTempFilePath(filePath, null);
    }

    public String getCompleteTempFilePath(final String filePath, final Long tenantId) throws IOException {
        String tempFilePath = filePath;
        final File tempFolder = getBonitaConstantUtil(tenantId).getTempFolder();

        if (!tempFilePath.contains(File.separator)) {
            tempFilePath = tempFolder.getAbsolutePath() + File.separator + tempFilePath;
        } else {
            verifyFolderAuthorization(new File(filePath), tempFolder);
        }

        return tempFilePath;
    }

    public WebBonitaConstantsUtils getBonitaConstantUtil(final Long tenantId) {
        if (tenantId != null) {
            return WebBonitaConstantsUtils.getInstance(tenantId);
        } else {
            return WebBonitaConstantsUtils.getInstance();
        }
    }

    public boolean isInTempFolder(final File file, final WebBonitaConstantsUtils webBonitaConstantsUtils) throws IOException {
        return isInFolder(file, webBonitaConstantsUtils.getTempFolder());
    }

    public boolean isInFolder(final File file, final File parentFolder) throws IOException {
        try {
            verifyFolderAuthorization(file, parentFolder);
        } catch (final UnauthorizedFolderException e) {
            return false;
        }
        return true;
    }

    private void verifyFolderAuthorization(final File file, final File parentFolder) throws IOException {
        try {
            if (!file.getCanonicalPath().startsWith(parentFolder.getCanonicalPath())) {
                throw new UnauthorizedFolderException("Unauthorized access to the file " + file.getPath());
            }
        } catch (final UnauthorizedFolderException e) {
            final String errorMessage = "Unauthorized access to the file " + file.getAbsolutePath()
                    + ". For security reasons, access to paths other than "
                    + parentFolder.getAbsolutePath()
                    + " is restricted.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw e;
        }
    }

    public IconDescriptor getIconFromFileSystem(String iconPath, long tenantId) {
        try {
            String completeTempFilePath = getCompleteTempFilePath(iconPath, tenantId);
            File tempFile = new File(completeTempFilePath);
            return new IconDescriptor(tempFile.getName(), FileUtils.readFileToByteArray(tempFile));
        } catch (IOException e) {
            throw new APIForbiddenException("Forbidden access to " + iconPath, e);
        }
    }
}
