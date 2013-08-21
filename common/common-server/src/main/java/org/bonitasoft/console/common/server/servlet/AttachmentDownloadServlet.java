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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet allowing to download process instances attachments
 * 
 * @author Julien Mege
 */
public class AttachmentDownloadServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 5209516978177786895L;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(AttachmentDownloadServlet.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
    }

    protected byte[] getFileContent(final File file, final int fileLength, final String attachmentPath) throws ServletException, FileNotFoundException {
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
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, errorMessage, e);
                }
                throw new ServletException(errorMessage, e);
            } finally {
                fileInput.close();
            }
        } catch (final FileNotFoundException e) {
            final String errorMessage = "Attachment : No such file\n\t> " + attachmentPath;
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage);
            }
            throw e;
        } catch (final IOException e) {
            final String errorMessage = "Attachment : Error while reading file " + attachmentPath;
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage + "\n\t> " + e.getMessage());
            }
            throw new ServletException(errorMessage, e);
        }
        return content;
    }
}
