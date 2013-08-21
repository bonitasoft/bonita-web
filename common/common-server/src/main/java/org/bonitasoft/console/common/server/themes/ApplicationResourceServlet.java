/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.themes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * JAAS Store login context
     */
    public static final String JAAS_STORE_LOGIN_CONTEXT = "BonitaStore";

    /**
     * user credentials param key inside the session
     */
    public static final String USER_CREDENTIALS_SESSION_PARAM_KEY = "userCredentials";

    /**
     * process id : indicate the process for witch the form has to be displayed
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
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(ApplicationResourceServlet.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {

        // final String processUUIDStr = request.getParameter(PROCESS_ID_PARAM);
        // final String resourcePath = request.getParameter(RESOURCE_PATH_PARAM);
        // String resourceFileName = null;
        // byte[] content = null;
        // String contentType = null;
        // if (resourcePath == null) {
        // final String errorMessage = "Error while using the servlet ApplicationResourceServlet to get a resource: the parameter " + RESOURCE_PATH_PARAM +
        // " is undefined.";
        // if (LOGGER.isLoggable(Level.SEVERE)) {
        // LOGGER.log(Level.SEVERE, errorMessage);
        // }
        // throw new ServletException(errorMessage);
        // }
        // if (processUUIDStr == null) {
        // final String errorMessage = "Error while using the servlet ApplicationResourceServlet to get a resource: the parameter " + PROCESS_ID_PARAM +
        // " is undefined.";
        // if (LOGGER.isLoggable(Level.SEVERE)) {
        // LOGGER.log(Level.SEVERE, errorMessage);
        // }
        // throw new ServletException(errorMessage);
        // }
        // try {
        // final File processDir = new File(PropertiesFactory.getTenancyProperties().getDomainFormsWorkFolder(), processUUIDStr);
        // if (processDir.exists()) {
        // final File[] directories = processDir.listFiles(new FileFilter() {
        // public boolean accept(final File pathname) {
        // return pathname.isDirectory();
        // }
        // });
        // long lastDeployementDate = 0L;
        // for (final File directory : directories) {
        // try {
        // final long deployementDate = Long.parseLong(directory.getName());
        // if (deployementDate > lastDeployementDate) {
        // lastDeployementDate = deployementDate;
        // }
        // } catch (final Exception e) {
        // if (LOGGER.isLoggable(Level.WARNING)) {
        // LOGGER.log(Level.WARNING,
        // "Process application resources deployement folder contains a directory that does not match a process deployement timestamp: " + directory.getName(),
        // e);
        // }
        // }
        // }
        // if (lastDeployementDate == 0L) {
        // if (LOGGER.isLoggable(Level.WARNING)) {
        // LOGGER.log(Level.WARNING, "Process application resources deployement folder contains no directory that match a process deployement timestamp.");
        // }
        // } else {
        // final File file = new File(processDir, lastDeployementDate + File.separator + WEB_RESOURCES_DIR + File.separator + resourcePath);
        // try {
        // if (!file.getCanonicalPath().startsWith(processDir.getCanonicalPath())) {
        // throw new IOException();
        // }
        // } catch (final IOException e) {
        // final String errorMessage = "Error while getting the resource " + resourcePath + " For security reasons, access to paths other than " +
        // processDir.getName() + " is restricted";
        // if (LOGGER.isLoggable(Level.SEVERE)) {
        // LOGGER.log(Level.SEVERE, errorMessage, e);
        // }
        // throw new ServletException(errorMessage);
        // }
        //
        // int fileLength = 0;
        // if (file.length() > Integer.MAX_VALUE) {
        // throw new ServletException("file " + resourcePath + " too big !");
        // } else {
        // fileLength = (int) file.length();
        // }
        // resourceFileName = file.getName();
        // content = getFileContent(file, fileLength, resourcePath);
        // if (resourceFileName.endsWith(".css")) {
        // contentType = "text/css";
        // }else if(resourceFileName.endsWith(".js")){
        // contentType = "application/x-javascript";
        // } else {
        // final FileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        // contentType = mimetypesFileTypeMap.getContentType(file);
        // }
        // }
        // } else {
        // if (LOGGER.isLoggable(Level.WARNING)) {
        // LOGGER.log(Level.WARNING, "The Process application resources deployement directory does not exist.");
        // }
        // }
        // } catch (final Exception e) {
        // final String errorMessage = "Error while getting the resource " + resourcePath;
        // if (LOGGER.isLoggable(Level.SEVERE)) {
        // LOGGER.log(Level.SEVERE, errorMessage, e);
        // }
        // throw new ServletException(errorMessage, e);
        // }
        // if (contentType == null) {
        // contentType = "application/octet-stream";
        // }
        // response.setContentType(contentType);
        // response.setCharacterEncoding("UTF-8");
        // try {
        // final String encodedfileName = URLEncoder.encode(resourceFileName, "UTF-8");
        // final String userAgent = request.getHeader("User-Agent");
        // if (userAgent != null && userAgent.contains("Firefox")) {
        // response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedfileName);
        // } else {
        // response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName.replaceAll("\\+", " ") + "\"; filename*=UTF-8''" +
        // encodedfileName);
        // }
        // response.setContentLength(content.length);
        // final OutputStream out = response.getOutputStream();
        // out.write(content);
        // out.close();
        // } catch (final IOException e) {
        // if (LOGGER.isLoggable(Level.SEVERE)) {
        // LOGGER.log(Level.SEVERE, "Error while generating the response.", e);
        // }
        // throw new ServletException(e.getMessage(), e);
        // }
    }

    protected byte[] getFileContent(final File file, final int fileLength, final String filePath) throws ServletException {
        byte[] content = null;
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
                final String errorMessage = "Error while getting the application resource. The file " + filePath + " does not exist.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage);
                }
                throw new ServletException(errorMessage);
            } finally {
                fileInput.close();
            }
        } catch (final IOException e) {
            final String errorMessage = "Error while reading resource: " + filePath;
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new ServletException(errorMessage, e);
        }
        return content;
    }

}
