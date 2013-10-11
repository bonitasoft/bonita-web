/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.forms.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.ArchivedDocument;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.server.accessor.impl.util.ApplicationResourcesUtils;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormWorkflowAPI;
import org.bonitasoft.forms.server.api.impl.util.BPMEngineAPIUtil;

/**
 * Servlet allowing to download process instances attachments
 * 
 * @author Anthony Birembaut
 */
public class DocumentDownloadServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 5209516978177786895L;

    /**
     * process ID
     */
    protected static final String PROCESS_ID_PARAM = "process";

    /**
     * instance ID
     */
    protected static final String INSTANCE_ID_PARAM = "instance";

    /**
     * task ID
     */
    protected static final String TASK_ID_PARAM = "task";

    /**
     * document id of the document to download
     */
    protected static final String DOCUMENT_ID_PARAM = "document";

    /**
     * attachment : indicate the path of the process attachment
     */
    protected static final String FILE_PATH_PARAM = "filePath";

    /**
     * attachment : indicate the file name of the process attachment
     */
    protected static final String FILE_NAME_PARAM = "fileName";

    /**
     * resource : indicate the file name of the process resource
     */
    protected static final String RESOURCE_FILE_NAME_PARAM = "resourceFileName";

    /**
     * The engine API session param key name
     */
    protected static final String API_SESSION_PARAM_KEY = "apiSession";

    /**
     * the name of the directory in which the resources are stored in the business archive (in /resources/forms)
     */
    protected static final String BUSINESS_ARCHIVE_RESOURCES_DIRECTORY = "documents";

    /**
     * Util class allowing to work with the BPM engine API
     */
    protected BPMEngineAPIUtil bpmEngineAPIUtil = new BPMEngineAPIUtil();

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(DocumentDownloadServlet.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {

        final String filePath = request.getParameter(FILE_PATH_PARAM);
        String fileName = request.getParameter(FILE_NAME_PARAM);
        final String resourcePath = request.getParameter(RESOURCE_FILE_NAME_PARAM);
        final String documentId = request.getParameter(DOCUMENT_ID_PARAM);
        final APISession apiSession = (APISession) request.getSession().getAttribute(API_SESSION_PARAM_KEY);
        byte[] fileContent = null;
        if (filePath != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "attachmentPath: " + filePath);
            }
            final File file = new File(filePath);
            try {
                final File tmpDir = WebBonitaConstantsUtils.getInstance(apiSession.getTenantId()).getTempFolder();
                if (!file.getCanonicalPath().startsWith(tmpDir.getCanonicalPath())) {
                    throw new IOException();
                }
            } catch (final IOException e) {
                final String errorMessage = "Error while getting the file " + filePath + " For security reasons, access to paths other than "
                        + WebBonitaConstants.BONITA_HOME + "/" + WebBonitaConstants.clientFolderPath + "/" + WebBonitaConstants.tmpFolderName
                        + " is restricted";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new ServletException(errorMessage);
            }
            if (fileName == null) {
                fileName = file.getName();
            }
            fileContent = getFileContent(file, filePath);
        } else if (documentId != null) {
            try {
                final ProcessAPI processAPI = bpmEngineAPIUtil.getProcessAPI(apiSession);
                String contentStorageId;
                try {
                    final Document document = processAPI.getDocument(Long.valueOf(documentId));
                    fileName = document.getContentFileName();
                    contentStorageId = document.getContentStorageId();
                } catch (final DocumentNotFoundException dnfe) {
                    final ArchivedDocument archivedDocument = processAPI.getArchivedProcessDocument(Long.valueOf(documentId));
                    fileName = archivedDocument.getDocumentContentFileName();
                    contentStorageId = archivedDocument.getContentStorageId();
                }
                if (contentStorageId != null && !contentStorageId.isEmpty()) {
                    fileContent = processAPI.getDocumentContent(contentStorageId);
                }
            } catch (final Exception e) {
                final String errorMessage = "Error while retrieving the document  with ID " + documentId + " from the engine.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new ServletException(errorMessage, e);
            }
        } else if (resourcePath != null) {
            final String processIDStr = request.getParameter(PROCESS_ID_PARAM);
            final String instanceIDStr = request.getParameter(INSTANCE_ID_PARAM);
            final String taskIdStr = request.getParameter(TASK_ID_PARAM);
            final IFormWorkflowAPI workflowAPI = FormAPIFactory.getFormWorkflowAPI();
            long processDefinitionID = -1;
            try {
                if (processIDStr != null) {
                    processDefinitionID = Long.parseLong(processIDStr);
                } else if (taskIdStr != null) {
                    processDefinitionID = workflowAPI.getProcessDefinitionIDFromActivityInstanceID(apiSession, Long.parseLong(taskIdStr));
                } else if (instanceIDStr != null) {
                    processDefinitionID = workflowAPI.getProcessDefinitionIDFromProcessInstanceID(apiSession, Long.parseLong(instanceIDStr));
                } else {
                    final String errorMessage = "Error while retrieving the resource " + resourcePath
                            + " : Either a process, instance or task is required in the URL";
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, errorMessage);
                    }
                    throw new ServletException(errorMessage);
                }
                Date processDeployementDate = workflowAPI.getMigrationDate(apiSession, processDefinitionID);
                if (processDeployementDate == null) {
                    processDeployementDate = workflowAPI.getProcessDefinitionDate(apiSession, processDefinitionID);
                }
                final File processDir = ApplicationResourcesUtils.getApplicationResourceDir(apiSession, processDefinitionID, processDeployementDate);
                final File resource = new File(processDir, BUSINESS_ARCHIVE_RESOURCES_DIRECTORY + File.separator + resourcePath);
                if (resource.exists()) {
                    fileName = resource.getName();
                    fileContent = getFileContent(resource, filePath);
                } else {
                    final String errorMessage = "The target resource does not exist " + resource.getAbsolutePath();
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, errorMessage);
                    }
                    throw new IOException(errorMessage);
                }
            } catch (final Exception e) {
                final String errorMessage = "Error while retrieving the resource " + resourcePath;
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new ServletException(errorMessage, e);
            }
        } else {
            final String errorMessage = "Error while getting the file. either a document, a filePath or a resourcePath parameter is required.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        try {
            final String encodedfileName = URLEncoder.encode(fileName, "UTF-8");
            final String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.contains("Firefox")) {
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedfileName.replace("+", "%20"));
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName.replaceAll("\\+", " ") + "\"; filename*=UTF-8''"
                        + encodedfileName.replace("+", "%20"));
            }
            final OutputStream out = response.getOutputStream();
            if (fileContent == null) {
                response.setContentLength(0);
            } else {
                response.setContentLength(fileContent.length);
                out.write(fileContent);
            }
            out.close();
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while generating the response.", e);
            }
            throw new ServletException(e);
        }
    }

    protected byte[] getFileContent(final File file, final String filePath) throws ServletException {

        int fileLength = 0;
        if (file.length() > Integer.MAX_VALUE) {
            throw new ServletException("file " + filePath + " too big !");
        } else {
            fileLength = (int) file.length();
        }

        byte[] content;
        try {
            final InputStream fileInput = new FileInputStream(file);
            final byte[] fileContent = new byte[fileLength];
            try {
                int offset = 0;
                int length = fileLength;
                while (length > 0) {
                    final int read = fileInput.read(fileContent, offset, length);
                    if (read <= 0) {
                        break;
                    }
                    length -= read;
                    offset += read;
                }
                content = fileContent;
            } catch (final FileNotFoundException e) {
                final String errorMessage = "Error while getting the attachment. The file " + filePath + " does not exist.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new ServletException(errorMessage, e);
            } finally {
                fileInput.close();
            }
        } catch (final IOException e) {
            final String errorMessage = "Error while reading attachment (file  : " + filePath;
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new ServletException(errorMessage, e);
        }
        return content;
    }
}
