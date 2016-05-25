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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.identity.Icon;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Anthony Birembaut
 * @author Baptiste Mesta
 */
public class IconServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(IconServlet.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String iconIdPath = request.getPathInfo();
        if (iconIdPath == null || iconIdPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long iconId = parseLong(iconIdPath);
        if (iconId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        IdentityAPI identityAPI = getIdentityApi(request);
        Icon icon;
        try {
            icon = identityAPI.getIcon(iconId);
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(icon.getMimeType());
        response.setCharacterEncoding("UTF-8");
        try {
            setHeaders(request, response, iconId);
        } catch (UnsupportedEncodingException e) {
            logAndThrowException(e, "Error while generating the headers.");
        }
        try (OutputStream out = response.getOutputStream()) {
            response.setContentLength(icon.getContent().length);
            out.write(icon.getContent());
        } catch (final IOException e) {
            logAndThrowException(e, "Error while generating the response.");
        }

    }

    private Long parseLong(String iconIdPath) {
        try {
            return Long.valueOf(iconIdPath.substring(1));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void logAndThrowException(IOException e, String msg) throws ServletException {
        if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, msg, e);
        }
        throw new ServletException(e.getMessage(), e);
    }

    private void setHeaders(HttpServletRequest request, HttpServletResponse response, Long iconId) throws UnsupportedEncodingException {
        final String encodedFileName = URLEncoder.encode(String.valueOf(iconId), "UTF-8");
        final String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.contains("Firefox")) {
            response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + encodedFileName);
        } else {
            response.setHeader("Content-Disposition", "inline; filename=\"" + encodedFileName.replaceAll("\\+", " ") + "\"; filename*=UTF-8''"
                    + encodedFileName);
        }
    }

    IdentityAPI getIdentityApi(HttpServletRequest request) {
        APISession session = (APISession) request.getSession().getAttribute("apiSession");
        return new APIClient(session).getIdentityAPI();
    }

}
