/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Anthony Birembaut
 */
public abstract class ResourceServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = -2103038794535737881L;

    /**
     * Logger
     */
    private final static Logger LOGGER = Logger.getLogger(ResourceServlet.class.getName());

    /**
     * tenant request parameter
     */
    private final static String TENANT_PARAM = "tenant";

    /**
     * The engine API session param key name
     */
    public static final String API_SESSION_PARAM_KEY = "apiSession";

    protected abstract String getResourceParameterName();

    protected abstract String getDefaultResourceName();

    protected abstract File getResourcesParentFolder(long tenantId);

    /**
     * Return null if there is no subfolder
     */
    protected abstract String getSubFolderName();

    protected ResourceLocationReader resourceLocationReader = new ResourceLocationReader();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String fileName = resourceLocationReader.getResourceLocationFromRequest(request);
        String resourceName = request.getParameter(getResourceParameterName());
        if (resourceName == null) {
            resourceName = getDefaultResourceName();
        }
        try {
            getResourceFile(request, response, resourceName, fileName);
        } catch (final UnsupportedEncodingException e) {
            final String errorMessage = "UnsupportedEncodingException :" + e;
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
    }

    /**
     * Get resource file.
     *
     * @throws ServletException
     * @throws UnsupportedEncodingException
     */
    protected void getResourceFile(final HttpServletRequest request, final HttpServletResponse response, String resourceName, String fileName)
            throws ServletException, IOException {
        if (resourceName == null) {
            final String errorMessage = "Error while using the servlet to get a resource: the parameter " + getResourceParameterName() + " is null.";
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
        if (fileName == null) {
            final String errorMessage = "Error while using the servlet to get a resource: the parameter " + ResourceLocationReader.LOCATION_PARAM + " is null.";
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
        resourceName = URLDecoder.decode(resourceName, "UTF-8");
        fileName = URLDecoder.decode(fileName, "UTF-8");
        response.setCharacterEncoding("UTF-8");

        final File resourcesParentFolder = getResourcesParentFolder(request);
        final String subFolderName = getSubFolderName();
        String subFolderSuffix;
        if (subFolderName != null) {
            subFolderSuffix = File.separator + subFolderName;
        } else {
            subFolderSuffix = "";
        }
        try {
            final File resourceFolder = new File(resourcesParentFolder, resourceName + subFolderSuffix);
            final File file = new File(resourceFolder, fileName);
            final BonitaHomeFolderAccessor tenantFolder = new BonitaHomeFolderAccessor();
            if (!tenantFolder.isInFolder(resourceFolder, resourcesParentFolder)) {
                throw new ServletException("For security reasons, access to this file paths" + resourceFolder.getAbsolutePath() + " is restricted.");
            }
            if (!tenantFolder.isInFolder(file, resourceFolder)) {
                throw new ServletException("For security reasons, access to this file paths" + file.getAbsolutePath() + " is restricted.");
            }

            byte[] content;
            String contentType;

            final String lowerCaseFileName = fileName.toLowerCase();
            if (lowerCaseFileName.endsWith(".jpg")) {
                contentType = "image/jpeg";
            } else if (lowerCaseFileName.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (lowerCaseFileName.endsWith(".gif")) {
                contentType = "image/gif";
            } else if (lowerCaseFileName.endsWith(".png")) {
                contentType = "image/png";
            } else if (lowerCaseFileName.endsWith(".css") || lowerCaseFileName.endsWith(".less")) {
                contentType = "text/css";
            } else if (lowerCaseFileName.endsWith(".js")) {
                contentType = "application/x-javascript";
            } else if (lowerCaseFileName.endsWith(".html")) {
                contentType = "text/html; charset=UTF-8";
            } else if (lowerCaseFileName.endsWith(".htc")) {
                contentType = "text/x-component";
            } else if (lowerCaseFileName.endsWith(".svg")) {
                contentType = "image/svg+xml";
            } else if (lowerCaseFileName.endsWith(".eot")) {
                contentType = "application/vnd.ms-fontobject";
            } else if (lowerCaseFileName.endsWith(".woff")) {
                contentType = "application/x-font-woff";
            } else if (lowerCaseFileName.endsWith(".ttf")) {
                contentType = "application/x-font-ttf";
            } else if (lowerCaseFileName.endsWith(".otf")) {
                contentType = "application/x-font-opentype";
            } else {
                final FileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
                contentType = mimetypesFileTypeMap.getContentType(file);
            }
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            content = FileUtils.readFileToByteArray(file);
            response.setContentType(contentType);
            response.setContentLength(content.length);
            response.setBufferSize(content.length);
            final OutputStream out = response.getOutputStream();
            out.write(content, 0, content.length);
            response.flushBuffer();
            out.close();
        } catch (FileNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getMessage());
            }
            response.sendError(404);
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while generating the response.", e);
            }
            throw new ServletException(e.getMessage(), e);
        }
    }

    protected File getResourcesParentFolder(final HttpServletRequest request) throws ServletException {
        final HttpSession session = request.getSession();
        long tenantId = 1;
        final String tenantFromRequest = request.getParameter(TENANT_PARAM);
        if (tenantFromRequest != null) {
            tenantId = Long.parseLong(tenantFromRequest);
        } else {
            final APISession apiSession = (APISession) session.getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
            if (apiSession != null) {
                tenantId = apiSession.getTenantId();
            }
        }
        try {
            return getResourcesParentFolder(tenantId);
        } catch (final RuntimeException e) {
            final String errorMessage = "Error while using the servlet to get themes parent folder.";
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
    }

}
