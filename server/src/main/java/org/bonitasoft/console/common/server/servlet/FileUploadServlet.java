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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.codehaus.jettison.json.JSONObject;

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

    protected static final String RESPONSE_CONTENT_TYPE_PARAM = "ContentType";

    protected static final String TEXT_CONTENT_TYPE = "text";

    protected static final String JSON_CONTENT_TYPE = "json";

    protected static final String TEMP_PATH_RESPONSE_ATTRIBUTE = "tempPath";

    protected static final String FILE_NAME_RESPONSE_ATTRIBUTE = "filename";

    protected String[] supportedExtensionsList = new String[0];

    protected boolean returnFullPathInResponse = false;

    protected boolean alsoReturnOriginalFilename = false;

    protected String responseContentType = TEXT_CONTENT_TYPE;

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
        final String responseContentTypeParam = getInitParameter(RESPONSE_CONTENT_TYPE_PARAM);
        if (responseContentTypeParam != null) {
            responseContentType = responseContentTypeParam;
        }
    }

    protected abstract void defineUploadDirectoryPath(final HttpServletRequest request);

    protected abstract void checkUploadSize(final HttpServletRequest request, FileItem item) throws FileTooBigException;

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

                checkUploadSize(request, item);

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
                uploadedFile.deleteOnExit();

                // Response
                final String responseString;
                if (JSON_CONTENT_TYPE.equals(responseContentType)) {
                    responseString = generateResponseJson(request, fileName, uploadedFile);
                } else if (TEXT_CONTENT_TYPE.equals(responseContentType)) {
                    responseString = generateResponseString(request, fileName, uploadedFile);
                } else {
                    throw new ServletException("Unsupported content type in servlet configuration : " + responseContentType);
                }
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
            responseString = responseString + RESPONSE_SEPARATOR + getFilenameLastSegment(fileName);
        }
        return responseString;
    }

    protected String generateResponseJson(final HttpServletRequest request, final String fileName, final File uploadedFile) throws Exception {
        final Map<String, String> responseMap = new HashMap<String, String>();
        if (alsoReturnOriginalFilename) {
            responseMap.put(FILE_NAME_RESPONSE_ATTRIBUTE, getFilenameLastSegment(fileName));
        }
        if (returnFullPathInResponse) {
            responseMap.put(TEMP_PATH_RESPONSE_ATTRIBUTE, uploadedFile.getPath());
        } else {
            responseMap.put(TEMP_PATH_RESPONSE_ATTRIBUTE, uploadedFile.getName());
        }
        return new JSONObject(responseMap).toString();
    }

    protected File makeUniqueFilename(final File targetDirectory, final String fileName) throws IOException {
        final File uploadedFile = File.createTempFile("tmp_", getExtension(fileName), targetDirectory);
        uploadedFile.deleteOnExit();
        return uploadedFile;
    }

    protected String getExtension(final String fileName) {
        String extension = "";
        final String filenameLastSegment = getFilenameLastSegment(fileName);
        final int dotPos = filenameLastSegment.lastIndexOf('.');
        if (dotPos > -1) {
            extension = filenameLastSegment.substring(dotPos);
        }
        return extension;
    }

    protected String getFilenameLastSegment(final String fileName) {
        int slashPos = fileName.lastIndexOf("/");
        if (slashPos == -1) {
            slashPos = fileName.lastIndexOf("\\");
        }
        return fileName.substring(slashPos+1);
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
