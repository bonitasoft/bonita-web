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
package org.bonitasoft.web.toolkit.server.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet allowing to download process instances attachments
 * 
 * @author Julien Mege
 */
public abstract class AttachmentImageServlet extends AttachmentDownloadServlet {

    private static final long serialVersionUID = 6883709267615643971L;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(AttachmentImageServlet.class.getName());

    public static final String SRC_PARAM = "src";

    private String directoryPath = null;

    protected abstract void defineDirectoryPath(final HttpServletRequest request);

    /**
     * @param directoryPath
     *            the directoryPath to set
     */
    public void setDirectoryPath(final String directoryPath) {
        this.directoryPath = directoryPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        defineDirectoryPath(request);
        final String srcStr = request.getParameter(SRC_PARAM);
        String fileName = null;
        String contentType = null;
        byte[] attachment = null;

        if (srcStr != null) {
            final File file = new File(this.directoryPath + srcStr);

            int fileLength = 0;
            if (file.length() > Integer.MAX_VALUE) {
                throw new ServletException("file " + srcStr + " too big !");
            } else {
                fileLength = (int) file.length();
            }
            if (fileName == null) {
                fileName = file.getName();
            }
            final FileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
            contentType = mimetypesFileTypeMap.getContentType(file);
            try {
                attachment = getFileContent(file, fileLength, srcStr);
            } catch (final FileNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
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
                if (attachment != null) {
                    response.setContentLength(attachment.length);
                    OutputStream out = null;
                    try {
                        out = response.getOutputStream();
                        out.write(attachment);
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
