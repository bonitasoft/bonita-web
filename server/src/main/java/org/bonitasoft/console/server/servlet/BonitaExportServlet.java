/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.server.servlet;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ExecutionException;
import org.bonitasoft.engine.exception.ExportException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.InvalidSessionException;

/**
 * Export Resources as XML file
 *
 * @author Cuisha Gai, Anthony Birembaut
 */
abstract class BonitaExportServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 1800666571090128789L;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {

        OutputStream out = null;
        try {
            final byte[] resourceBytes = exportResources(request);

            // Set response headers
            setResponseHeaders(request, response);
            out = response.getOutputStream();
            out.write(resourceBytes);
            out.flush();

        } catch (final InvalidSessionException e) {
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, _("Session expired. Please log in again."), e);
            }
            throw new ServletException(e.getMessage(), e);
        } catch (final FileNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            if (getLogger().isLoggable(Level.WARNING)) {
                getLogger().log(Level.WARNING, "Resource not found");
            }
            throw new ServletException(e.getMessage(), e);
        } catch (final Exception e) {
            if (getLogger().isLoggable(Level.SEVERE)) {
                getLogger().log(Level.SEVERE, e.getMessage(), e);
            }
            throw new ServletException(e.getMessage(), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (final Exception e) {
                getLogger().log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    protected void setResponseHeaders(final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        final String encodedfileName = URLEncoder.encode(getFileExportName(), "UTF-8");
        final String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.contains("Firefox")) {
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedfileName);
        } else {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName.replaceAll("\\_", " ") + "\"; filename*=UTF-8''"
                    + encodedfileName);
        }
    }

    protected abstract String getFileExportName();

    protected abstract byte[] exportResources(final HttpServletRequest request) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, ExportException, FileNotFoundException, ExecutionException;

    protected abstract Logger getLogger();

}
