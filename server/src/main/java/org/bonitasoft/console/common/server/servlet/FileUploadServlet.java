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
    private static final long serialVersionUID = -948661031179067420L;

    private static final Logger LOGGER = Logger.getLogger(FileUploadServlet.class.getName());

    private String uploadDirectoryPath = null;

    private static final String RESPONSE_SEPARATOR = ":";

    private static final String SUPPORTED_EXTENSIONS_PARAM = "SupportedExtensions";

    private static final String SUPPORTED_EXTENSIONS_SEPARATOR = ",";

    private static final String RETURN_FULL_SERVER_PATH_PARAM = "ReturnFullPath";

    private static final String RETURN_ORIGINAL_FILENAME_PARAM = "ReturnOriginalFilename";

    private String[] supportedExtensionsList = new String[0];

    protected boolean returnFullPathInResponse = true;

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

    public void setUploadDirectoryPath(final String uploadDirectoryPath) {
        this.uploadDirectoryPath = uploadDirectoryPath;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        defineUploadDirectoryPath(request);
        response.setContentType("text/plain;charset=UTF-8");
        final PrintWriter responsePW = response.getWriter();
        try {

            if (!ServletFileUpload.isMultipartContent(request)) {
                return;
            }

            final File targetDirectory = new File(uploadDirectoryPath);
            if (!targetDirectory.exists()) {
                targetDirectory.mkdirs();
            }

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

                // Upload file
                item.write(uploadedFile);

                // Response
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "File uploaded : " + uploadedFile.getPath());
                }

                String responseString;
                if (returnFullPathInResponse) {
                    responseString = uploadedFile.getPath();
                } else {
                    responseString = uploadedFile.getName();
                }
                if (alsoReturnOriginalFilename) {
                    responseString = responseString + RESPONSE_SEPARATOR + fileName;
                }
                responsePW.print(responseString);
                responsePW.flush();

                // TODO add break
            }
        } catch (final Exception e) {
            final String theErrorMessage = "Exception while uploading file.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, theErrorMessage, e);
            }
            throw new ServletException(theErrorMessage, e);
        }
    }

    /**
     * @param response
     * @param responsePW
     */
    private void outputMediaTypeError(final HttpServletResponse response, final PrintWriter responsePW) {
        response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        responsePW.print("Exstention not supported.");
        responsePW.flush();
        return;
    }

    private boolean isSupportedExtention(final String fileName) {
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
