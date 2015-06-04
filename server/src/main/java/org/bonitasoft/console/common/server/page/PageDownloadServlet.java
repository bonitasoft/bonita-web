/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.page;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;

/**
 * @author Anthony Birembaut
 *
 */
public class PageDownloadServlet extends HttpServlet {

    private static final String ID_PARAM = "id";

//    private static final String EXPORT_FILE_EXTENTION = "page.zip";

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(PageDownloadServlet.class.getName());

    /**
     * UID
     */
    private static final long serialVersionUID = 7203686892997001991L;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {

        final String pageIDStr = request.getParameter(ID_PARAM);
        long pageId = 0L;
        if (pageIDStr != null) {
            pageId = Long.parseLong(pageIDStr);
        } else {
            throw new ServletException("The ID parameter is mandatory.");
        }
        final APISession apiSession = (APISession) request.getSession().getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        OutputStream out = null;
        try {
            final PageAPI pageAPI = getPageAPI(apiSession);
            final Page page = pageAPI.getPage(pageId);
            final byte[] pageContent = pageAPI.getPageContent(pageId);
            final GetUserRightsHelper getUserRightsHelper = new GetUserRightsHelper(apiSession);
            if (!apiSession.isTechnicalUser() && !getUserRightsHelper.getUserRights().contains("pagelisting")) {
                throw new IllegalAccessException("User not Authorized");
            }
            // Set response headers
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/octet-stream");
            final String encodedfileName = URLEncoder.encode(page.getContentName(), "UTF-8");
            final String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.contains("Firefox")) {
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedfileName);
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName.replaceAll("\\_", " ") + "\"; filename*=UTF-8''"
                        + encodedfileName);
            }
            out = response.getOutputStream();

            if (pageContent == null) {
                response.setContentLength(0);
            } else {
                response.setContentLength(pageContent.length);
            }
            out.write(pageContent);
            out.flush();

        } catch (final InvalidSessionException e) {
            final String message = "Session expired. Please login again.";
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, message, e);
            }
            try {
                out.write(message.getBytes());
                out.flush();
            } catch (final IOException e1) {
                throw new ServletException(e1);
            }

        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            try {
                out.write("An exception occured. Please contact an administrator".getBytes());
                out.flush();
            } catch (final IOException e1) {
                throw new ServletException(e1);
            }
        } finally {
            try {
                out.close();
            } catch (final Exception e) {
                out = null;
            }
        }

    }

    private PageAPI getPageAPI(final APISession apiSession) throws InvalidSessionException, BonitaHomeNotSetException, ServerAPIException,
            UnknownAPITypeException {
        return TenantAPIAccessor.getCustomPageAPI(apiSession);
    }

}
