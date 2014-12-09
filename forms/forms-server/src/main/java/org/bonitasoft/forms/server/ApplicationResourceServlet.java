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
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.server.exception.NoCredentialsInSessionException;

/**
 * Servlet allowing to download process application resources
 *
 * @author Anthony Birembaut
 */
public class ApplicationResourceServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 5209516978177786895L;

    /**
     * process id : indicate the process for which the form has to be displayed
     */
    public static final String PROCESS_ID_PARAM = "process";

    /**
     * the resources directory name
     */
    public static final String WEB_RESOURCES_DIR = "resources";

    /**
     * resource : indicate the path of the resource
     */
    public static final String RESOURCE_PATH_PARAM = "location";

    /**
     * The engine API session param key name
     */
    public static final String API_SESSION_PARAM_KEY = "apiSession";

    /**
     * The user session param key name
     */
    public static final String USER_SESSION_PARAM_KEY = "user";

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(ApplicationResourceServlet.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {

        final String processUUIDStr = request.getParameter(PROCESS_ID_PARAM);
        final String resourcePath = request.getParameter(RESOURCE_PATH_PARAM);
        String resourceFileName = null;
        byte[] content = null;
        String contentType = null;
        if (resourcePath == null) {
            final String errorMessage = "Error while using the servlet ApplicationResourceServlet to get a resource: the parameter " + RESOURCE_PATH_PARAM
                    + " is undefined.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
        if (processUUIDStr == null) {
            final String errorMessage = "Error while using the servlet ApplicationResourceServlet to get a resource: the parameter " + PROCESS_ID_PARAM
                    + " is undefined.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
        try {
            final long tenantID = getTenantID(request);
            final File processDir = new File(WebBonitaConstantsUtils.getInstance(tenantID).getFormsWorkFolder(), processUUIDStr);
            if (processDir.exists()) {
                final File[] directories = processDir.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(final File pathname) {
                        return pathname.isDirectory();
                    }
                });
                long lastDeployementDate = 0L;
                for (final File directory : directories) {
                    try {
                        final long deployementDate = Long.parseLong(directory.getName());
                        if (deployementDate > lastDeployementDate) {
                            lastDeployementDate = deployementDate;
                        }
                    } catch (final Exception e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING,
                                    "Process application resources deployment folder contains a directory that does not match a process deployment timestamp: "
                                            + directory.getName(), e);
                        }
                    }
                }
                if (lastDeployementDate == 0L) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                "Process application resources deployment folder contains no directory that match a process deployment timestamp.");
                    }
                } else {
                    final File file = new File(processDir, lastDeployementDate + File.separator + WEB_RESOURCES_DIR + File.separator + resourcePath);
                    try {
                        if (!file.getCanonicalPath().startsWith(processDir.getCanonicalPath())) {
                            throw new IOException();
                        }
                    } catch (final IOException e) {
                        final String errorMessage = "Error while getting the resource " + resourcePath + " For security reasons, access to paths other than "
                                + processDir.getName() + " is restricted";
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, errorMessage, e);
                        }
                        throw new ServletException(errorMessage);
                    }
                    resourceFileName = file.getName();
                    content = FileUtils.readFileToByteArray(file);
                    if (resourceFileName.endsWith(".css")) {
                        contentType = "text/css";
                    } else if (resourceFileName.endsWith(".js")) {
                        contentType = "application/x-javascript";
                    } else {
                        final FileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
                        contentType = mimetypesFileTypeMap.getContentType(file);
                    }
                }
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "The Process application resources deployment directory does not exist.");
                }
            }
        } catch (final Exception e) {
            final String errorMessage = "Error while getting the resource " + resourcePath;
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new ServletException(errorMessage, e);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
        try {
            final String encodedfileName = URLEncoder.encode(resourceFileName, "UTF-8");
            final String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.contains("Firefox")) {
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedfileName);
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName.replaceAll("\\+", " ") + "\"; filename*=UTF-8''"
                        + encodedfileName);
            }
            response.setContentLength(content.length);
            final OutputStream out = response.getOutputStream();
            out.write(content);
            out.close();
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while generating the response.", e);
            }
            throw new ServletException(e.getMessage(), e);
        }
    }

    /**
     * Retrieve the tenantID from the session
     *
     * @param request
     *            the HTTP request
     * @return the tenantID
     * @throws NoCredentialsInSessionException
     */
    protected long getTenantID(final HttpServletRequest request) throws NoCredentialsInSessionException {
        final HttpSession httpSession = request.getSession();
        final APISession aAPISession = (APISession) httpSession.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        return aAPISession.getTenantId();
    }

}
