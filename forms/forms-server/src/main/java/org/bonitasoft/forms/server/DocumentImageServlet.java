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
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
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

/**
 * Servlet allowing to view process instances attachments as images
 * 
 * TODO refactor to remove duplicate code with {@link DocumentDownloadServlet}
 * 
 * @author Anthony Birembaut
 */
public class DocumentImageServlet extends DocumentDownloadServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = -2397573068771431608L;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(DocumentImageServlet.class.getName());

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
        String contentType = null;
        if (filePath != null) {
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
            final FileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
            contentType = mimetypesFileTypeMap.getContentType(file);
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
        if (contentType != null) {
            response.setContentType(contentType);
        }
        response.setCharacterEncoding("UTF-8");
        if (fileName != null) {
            try {
                final String encodedfileName = URLEncoder.encode(fileName, "UTF-8");
                final String userAgent = request.getHeader("User-Agent");
                if (userAgent != null && userAgent.contains("Firefox")) {
                    response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + encodedfileName);
                } else {
                    response.setHeader("Content-Disposition", "inline; filename=\"" + encodedfileName.replaceAll("\\+", " ") + "\"; filename*=UTF-8''"
                            + encodedfileName);
                }
                if (fileContent != null) {
                    response.setContentLength(fileContent.length);
                    OutputStream out = null;
                    try {
                        out = response.getOutputStream();
                        out.write(fileContent);
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                    }
                }
            } catch (final IOException e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Error while generating the response.", e);
                }
                throw new ServletException(e.getMessage(), e);
            }
        }
    }
}
