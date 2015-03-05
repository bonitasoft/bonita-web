/*******************************************************************************
 * Copyright (C) 2015 BonitaSoft S.A.
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
 ******************************************************************************/
package org.bonitasoft.console.common.server.page;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.exception.BonitaException;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * Class used by servlets to display serve a file
 * Since each instance of the servlet carry an instance of this class, it should have absolutely no instance attribute
 *
 * @author Julien Mege
 */
public class ResourceRenderer {

    /**
     * Logger
     */
    private final static Logger LOGGER = Logger.getLogger(ResourceRenderer.class.getName());

    public void renderFile(final HttpServletRequest request, final HttpServletResponse response, File resourceFile)
            throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException, BonitaException {

        byte[] content;
        response.setCharacterEncoding("UTF-8");

        try {
            content = getFileContent(resourceFile);

            response.setContentType(request.getSession().getServletContext()
                    .getMimeType(resourceFile.getName()));
            response.setContentLength(content.length);
            response.setBufferSize(content.length);
            response.setHeader("Cache-Control", "no-cache");

            try (OutputStream out = response.getOutputStream()) {
                out.write(content, 0, content.length);
            }
            response.flushBuffer();
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while generating the response.", e);
            }
            throw new BonitaException(e.getMessage(), e);
        }
    }

    private byte[] getFileContent(File resourceFile) throws IOException, BonitaException {
        if(resourceFile==null){
            String errorMessage = "Resource file must not be null.";
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage);
            }
            throw new BonitaException(errorMessage);
        }
        return Files.readAllBytes(resourceFile.toPath());
    }

    public List<String> getPathSegments(final HttpServletRequest request) throws UnsupportedEncodingException {
        final List<String> segments = new ArrayList<>();
        final String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            for (final String segment : pathInfo.split("/")) {
                if (!segment.isEmpty()) {
                    segments.add(URLDecoder.decode(segment, "UTF-8"));
                }
            }
        }
        return segments;
    }
}
