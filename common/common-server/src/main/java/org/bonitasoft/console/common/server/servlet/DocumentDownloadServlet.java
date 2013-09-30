/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yongtao Guo
 */
public class DocumentDownloadServlet extends HttpServlet {

    /**
     * Version UID
     */
    private static final long serialVersionUID = -7164859812550334970L;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(DocumentDownloadServlet.class.getName());

    /**
     * attachment : indicate the path of the process attachment
     */
    public static final String ATTACHMENT_PATH_PARAM = "attachmentPath";

    /**
     * filename of the document to download
     */
    public static final String DOCUMENT_FILENAME_PARAM = "documentFilename";

    public static final String CONTENT_STORAGE_ID = "contentStorageId";

    public static final String DOCUMENT_ID = "documentId";

    public static final String TYPE = "caseType";

    private String fileName = null;

    private byte[] content = null;

    private void initParams(final HttpServletRequest req) {
        final HttpSession session = req.getSession();
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        final String attachmentPath = req.getParameter(ATTACHMENT_PATH_PARAM);
        final String documentId = req.getParameter(DOCUMENT_ID);
        final String contentStorageId = req.getParameter(CONTENT_STORAGE_ID);
        final String caseType = req.getParameter(TYPE);
        try {
            if (attachmentPath != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "attachmentPath: " + attachmentPath);
                }
                final File file = new File(attachmentPath);
                try {
                    final String tempPath = WebBonitaConstantsUtils.getInstance(apiSession.getTenantId()).getTempFolder().getPath();
                    if (!file.getCanonicalPath().startsWith(tempPath)) {
                        throw new IOException();
                    }
                } catch (final IOException e) {
                    final String errorMessage = "Error while getting the file " + attachmentPath + " For security reasons, access to paths other than "
                            + WebBonitaConstants.BONITA_HOME + "/" + WebBonitaConstants.clientFolderPath + "/" + WebBonitaConstants.tmpFolderName
                            + " is restricted";
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, errorMessage, e);
                    }
                    throw new ServletException(errorMessage);
                }
                this.fileName = file.getName();
                int fileLength = 0;
                if (file.length() > Integer.MAX_VALUE) {
                    throw new ServletException("file " + attachmentPath + " too big !");
                } else {
                    fileLength = (int) file.length();
                }
                this.content = getFileContent(file, fileLength, attachmentPath);
            } else {
                if (documentId != null && !documentId.isEmpty()) {
                    final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(apiSession);
                    if ("ARCHIVED".equals(caseType)) {
                        this.fileName = processAPI.getArchivedProcessDocument(Long.valueOf(documentId)).getDocumentContentFileName();
                    } else {
                        this.fileName = processAPI.getDocument(Long.valueOf(documentId)).getContentFileName();
                    }
                    if (contentStorageId != null && !contentStorageId.isEmpty()) {
                        this.content = processAPI.getDocumentContent(contentStorageId);
                    }
                }
            }
        } catch (final Exception ex) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while retrieving the file name and content of attachment.", ex);
            }
        }
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        initParams(request);
        if (this.content != null) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "fileName: " + this.fileName);
            }

            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            final String encodedfileName = URLEncoder.encode(this.fileName, "UTF-8");
            final String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.contains("Firefox")) {
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedfileName.replace("+", "%20"));
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName.replaceAll("\\+", " ") + "\"; filename*=UTF-8''"
                        + encodedfileName.replace("+", "%20"));
            }
            final OutputStream out = response.getOutputStream();
            out.write(this.content);
            out.close();
        }
    }

    protected byte[] getFileContent(final File file, final int fileLength, final String attachmentPath) throws ServletException {
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
                final String errorMessage = "Error while getting the attachment. The file " + attachmentPath + " does not exist.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new ServletException(errorMessage, e);
            } finally {
                fileInput.close();
            }
        } catch (final IOException e) {
            final String errorMessage = "Error while reading attachment (file  : " + attachmentPath;
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new ServletException(errorMessage, e);
        }
        return content;
    }
}
