/**
 * Copyright (C) 2015 BonitaSoft S.A.
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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.extension.PageResourceProviderImpl;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.exception.UnauthorizedAccessException;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.engine.session.APISession;

/**
 * Servlet allowing to display a page or the resource of a page
 * (it can be a custom page or an external page)
 *
 * @author Anthony Birembaut
 */
public class PageServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 2789496969243916444L;

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(PageServlet.class.getName());

    public static final String RESOURCE_PATH_SEPARATOR = "/content";

    public static final String API_PATH_SEPARATOR = "/API";

    public static final String THEME_PATH_SEPARATOR = "/theme";

    protected CustomPageRequestModifier customPageRequestModifier = new CustomPageRequestModifier();

    protected PageMappingService pageMappingService = new PageMappingService();

    protected BonitaHomeFolderAccessor bonitaHomeFolderAccessor = new BonitaHomeFolderAccessor();

    protected ResourceRenderer resourceRenderer = new ResourceRenderer();

    protected PageRenderer pageRenderer = new PageRenderer(resourceRenderer);

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        if (!pathInfo.contains(RESOURCE_PATH_SEPARATOR + "/") && pathInfo.indexOf(API_PATH_SEPARATOR + "/") > 0) {
            //Support relative calls to the REST API from the forms using ../API/
            final String apiPath = pathInfo.substring(pathInfo.indexOf(API_PATH_SEPARATOR + "/"));
            request.getRequestDispatcher(apiPath).forward(request, response);
        } else if (!pathInfo.contains(RESOURCE_PATH_SEPARATOR + "/") && pathInfo.indexOf(THEME_PATH_SEPARATOR + "/") > 0) {
            //BS-14188 : as theme are only currently managed in living app, we cannot currently serve
            // theme request from Forms. But, it shouldn't produce any 400 request error either
            // thus, we return 200
            return ;
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        final String pathInfo = request.getPathInfo();
        // Check if requested URL is missing final slash (necessary in order to be able to use relative URLs for resources)
        if (pathInfo.endsWith(RESOURCE_PATH_SEPARATOR)) {
            customPageRequestModifier.redirectToValidPageUrl(request, response);
        } else if (pathInfo.indexOf(RESOURCE_PATH_SEPARATOR + "/") > 0) {
            final String[] pathInfoSegments = pathInfo.split(RESOURCE_PATH_SEPARATOR + "/", 2);
            final String mappingKey = pathInfoSegments[0].substring(1);
            String resourcePath = null;
            if (pathInfoSegments.length > 1 && !pathInfoSegments[1].isEmpty()) {
                resourcePath = pathInfoSegments[1];
            }
            try {
                resolveAndDisplayPage(request, response, apiSession, mappingKey, resourcePath);
            } catch (final Exception e) {
                handleException(response, mappingKey, e);
            }
        } else {
            final String message = "/content is expected in the URL after the page mapping key";
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Bad request: " + message);
            }
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
        }
    }

    protected void resolveAndDisplayPage(final HttpServletRequest request, final HttpServletResponse response, final APISession apiSession,
            final String mappingKey, final String resourcePath)
                    throws BonitaException, IOException, InstantiationException, IllegalAccessException {
        try {
            final PageReference pageReference = pageMappingService.getPage(request, apiSession, mappingKey, pageRenderer.getCurrentLocale(request),
                    isNotResourcePath(resourcePath));
            if (pageReference.getURL() != null) {
                displayExternalPage(request, response, pageReference.getURL());
            } else if (pageReference.getPageId() != null) {
                displayPageOrResource(request, response, apiSession, pageReference.getPageId(), resourcePath);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    final String message = "Both URL and pageId are not set in the page mapping for " + mappingKey;
                    LOGGER.log(Level.FINE, message);
                }
                return;
            }
        } catch (final UnauthorizedAccessException e) {
            final String message = "User not Authorized";
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Forbidden: " + message, e);
            }
            response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
        } catch (final NotFoundException e) {

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Not found: Cannot find the form mapping for key " + mappingKey, e);
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Form mapping not found");
        }
    }

    protected void displayPageOrResource(final HttpServletRequest request, final HttpServletResponse response, final APISession apiSession,
            final Long pageId, final String resourcePath)
                    throws InstantiationException, IllegalAccessException, IOException, BonitaException {
        try {
            if (isNotResourcePath(resourcePath)) {
                pageRenderer.displayCustomPage(request, response, apiSession, pageId);
            } else {
                resourceRenderer.renderFile(request, response, getResourceFile(response, apiSession, pageId, resourcePath), apiSession);
            }
        } catch (final PageNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Cannot find the page with ID " + pageId);
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }

    private boolean isNotResourcePath(final String resourcePath) {
        return resourcePath == null || CustomPageService.PAGE_INDEX_FILENAME.equals(resourcePath)
                || CustomPageService.PAGE_CONTROLLER_FILENAME.equals(resourcePath) || CustomPageService.PAGE_INDEX_NAME.equals(resourcePath);
    }

    protected File getResourceFile(final HttpServletResponse response, final APISession apiSession, final Long pageId, final String resourcePath)
            throws IOException, BonitaException {
        final PageResourceProviderImpl pageResourceProvider = pageRenderer.getPageResourceProvider(pageId, apiSession);
        final File resourceFile = pageResourceProvider.getResourceAsFile(CustomPageService.RESOURCES_PROPERTY + File.separator + resourcePath);
        if (!bonitaHomeFolderAccessor.isInFolder(resourceFile, pageResourceProvider.getPageDirectory())) {
            final String message = "For security reasons, access to this file path is forbidden : " + resourcePath;
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Forbidden: " + message);
            }
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        pageRenderer.ensurePageFolderIsPresent(apiSession, pageResourceProvider);
        return resourceFile;
    }

    protected void displayExternalPage(final HttpServletRequest request, final HttpServletResponse response, final String url) throws IOException {
        response.sendRedirect(response.encodeRedirectURL(url));
    }

    protected void handleException(final HttpServletResponse response, final String mappingKey, final Exception e)
            throws ServletException {
        try {
            if (e instanceof IllegalArgumentException) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "The parameters passed to the servlet are invalid.", e);
                }
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    final String message = "Error while trying to display a page for key " + mappingKey;
                    LOGGER.log(Level.WARNING, message, e);
                }
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } catch (final IOException ioe) {
            throw new ServletException(ioe);
        }
    }

}
