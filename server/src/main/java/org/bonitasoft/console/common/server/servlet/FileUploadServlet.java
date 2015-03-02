/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet allowing to upload a File.
 *
 * @author Julien Mege
 */
public abstract class FileUploadServlet extends HttpServlet {

    /**
     * UID
     */
    protected static final long serialVersionUID = -948661031179067420L;

    protected static final Logger LOGGER = Logger.getLogger(FileUploadServlet.class.getName());

    private String uploadDirectoryPath = null;

    public static final String RESPONSE_SEPARATOR = "::";

    protected static final String SUPPORTED_EXTENSIONS_PARAM = "SupportedExtensions";

    protected static final String SUPPORTED_EXTENSIONS_SEPARATOR = ",";

    protected static final String RETURN_FULL_SERVER_PATH_PARAM = "ReturnFullPath";

    protected static final String RETURN_ORIGINAL_FILENAME_PARAM = "ReturnOriginalFilename";

    protected String[] supportedExtensionsList = new String[0];

    protected boolean returnFullPathInResponse = false;

    protected boolean alsoReturnOriginalFilename = false;

    @Override
    public void init() throws ServletException {

        final String supportedExtensionsParam = getInitParameter(SUPPORTED_EXTENSIONS_PARAM);
        if (supportedExtensionsParam != null) {
            supportedExtensionsList = supportedExtensionsParam.split(SUPPORTED_EXTENSIONS_SEPARATOR);
        }
        final String alsoReturnOriginalFilenameParam = getInitParameter(RETURN_ORIGINAL_FILENAME_PARAM);
        if (alsoReturnOriginalFilenameParam != null) {
            alsoReturnOriginalFilename = Boolean.parseBoolean(alsoReturnOriginalFilenameParam);
        }
        final String returnFullPathInResponseParam = getInitParameter(RETURN_FULL_SERVER_PATH_PARAM);
        if (returnFullPathInResponseParam != null) {
            returnFullPathInResponse = Boolean.parseBoolean(returnFullPathInResponseParam);
        }
    }

    protected abstract void defineUploadDirectoryPath(final HttpServletRequest request);

    protected void setUploadDirectoryPath(final String uploadDirectoryPath) {
        this.uploadDirectoryPath = uploadDirectoryPath;
    }

    protected String getUploadDirectoryPath() {
        return uploadDirectoryPath;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        defineUploadDirectoryPath(request);
        response.setContentType("text/plain;charset=UTF-8");
        try {
            if (!ServletFileUpload.isMultipartContent(request)) {
                return;
            }

            final File targetDirectory = new File(uploadDirectoryPath);
            if (!targetDirectory.exists()) {
                targetDirectory.mkdirs();
            }

            final PrintWriter responsePW = response.getWriter();

            final FileItemFactory fileItemFactory = new DiskFileItemFactory();
            final ServletFileUpload serviceFileUpload = new ServletFileUpload(fileItemFactory);
            final List<FileItem> items = serviceFileUpload.parseRequest(request);

            for (final FileItem item : items) {
                if (item.isFormField()) {
                    continue;
                }

                final String fileName = item.getName();

                // Check if extension is allowed
                if (!isSupportedExtention(fileName)) {
                    outputMediaTypeError(response, responsePW);
                    return;
                }

                // Make unique file name
                final File uploadedFile = makeUniqueFilename(targetDirectory, fileName);

                // Upload file
                item.write(uploadedFile);
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "File uploaded : " + uploadedFile.getPath());
                }

                // Response
                final String responseString = generateResponseString(request, fileName, uploadedFile);
                responsePW.print(responseString);
                responsePW.flush();
            }
        } catch (final Exception e) {
            final String theErrorMessage = "Exception while uploading file.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, theErrorMessage, e);
            }
            throw new ServletException(theErrorMessage, e);
        }
    }

    protected String generateResponseString(final HttpServletRequest request, final String fileName, final File uploadedFile) throws Exception {
        String responseString;
        if (returnFullPathInResponse) {
            responseString = uploadedFile.getPath();
        } else {
            responseString = uploadedFile.getName();
        }
        if (alsoReturnOriginalFilename) {
            responseString = responseString + RESPONSE_SEPARATOR + fileName;
        }
        return responseString;
    }

    protected File makeUniqueFilename(final File targetDirectory, final String fileName) throws IOException {
        String extension = "";
        int slashPos = fileName.lastIndexOf("/");
        if (slashPos == -1) {
            slashPos = fileName.lastIndexOf("\\");
        }
        final int dotPos = fileName.lastIndexOf('.');
        if (dotPos > slashPos && dotPos > -1) {
            extension = fileName.substring(dotPos);
        }
        final File uploadedFile = File.createTempFile("tmp_", extension, targetDirectory);
        uploadedFile.deleteOnExit();
        return uploadedFile;
    }

    protected void outputMediaTypeError(final HttpServletResponse response, final PrintWriter responsePW) {
        response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        responsePW.print("Exstention not supported.");
        responsePW.flush();
        return;
    }

    protected boolean isSupportedExtention(final String fileName) {
        if (fileName == null) {
            return false;
        }

        // if no extension specified, all extensions are allowed
        if (supportedExtensionsList.length < 1) {
            return true;
        }

        for (final String extention : supportedExtensionsList) {
            if (fileName.toLowerCase().endsWith("." + extention.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
