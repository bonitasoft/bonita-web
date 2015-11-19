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
package org.bonitasoft.livingapps;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.CustomPageAuthorizationsHelper;
import org.bonitasoft.console.common.server.page.CustomPageRequestModifier;
import org.bonitasoft.console.common.server.page.CustomPageService;
import org.bonitasoft.console.common.server.page.GetUserRightsHelper;
import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.console.common.server.page.ResourceRenderer;
import org.bonitasoft.console.common.server.page.extension.PageResourceProviderImpl;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.business.application.ApplicationPageNotFoundException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.engine.session.APISession;

public class LivingApplicationPageServlet extends HttpServlet {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(LivingApplicationPageServlet.class.getName());

    /**
     * uuid
     */
    private static final long serialVersionUID = -5410859017103815654L;

    public static final String RESOURCE_PATH_SEPARATOR = "/content";

    public static final String API_PATH_SEPARATOR = "/API";

    public static final String THEME_PATH_SEPARATOR = "/theme";

    protected ResourceRenderer resourceRenderer = new ResourceRenderer();

    protected PageRenderer pageRenderer = new PageRenderer(resourceRenderer);

    protected BonitaHomeFolderAccessor bonitaHomeFolderAccessor = new BonitaHomeFolderAccessor();

    protected CustomPageRequestModifier customPageRequestModifier = new CustomPageRequestModifier();

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        final List<String> pathSegments = resourceRenderer.getPathSegments(pathInfo);
        if (isValidPathForToken(API_PATH_SEPARATOR, pathSegments)) {
            //Support relative calls to the REST API from the forms using ../API/
            final String apiPath = pathInfo.substring(pathInfo.indexOf(API_PATH_SEPARATOR + "/"));
            request.getRequestDispatcher(apiPath).forward(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final String pathInfo = request.getPathInfo();
        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(SessionUtil.API_SESSION_PARAM_KEY);

        // Check if requested URL is missing final slash (necessary in order to be able to use relative URLs for resources)
        if (pathInfo.endsWith(RESOURCE_PATH_SEPARATOR)) {
            customPageRequestModifier.redirectToValidPageUrl(request, response);
            return;
        }

        String appToken = null;
        String pageToken = null;
        String customPageName = null;
        String resourcePath = null;

        //Validate mapping key contain "AppToken/PageToken/" and at least one more segment
        final List<String> pathSegments = resourceRenderer.getPathSegments(pathInfo);
        if (pathSegments.size() >= 3) {
            appToken = pathSegments.get(0);
            pageToken = pathSegments.get(1);
            customPageName = getCustomPageName(appToken, pageToken, apiSession, response);

            if (isValidPathForToken(RESOURCE_PATH_SEPARATOR, pathSegments)) {

                final String pageMapping = "/" + appToken + "/" + pageToken + RESOURCE_PATH_SEPARATOR + "/";
                if (pathInfo.length() > pageMapping.length()) {
                    resourcePath = pathInfo.substring(pageMapping.length());
                }

                try {
                    if (resourcePath == null || isNotResourcePath(resourcePath)) {
                        if (!isAuthorized(apiSession, appToken, customPageName)) {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not Authorized");
                            return;
                        }
                        pageRenderer.displayCustomPage(request, response, apiSession, customPageName);
                    } else {
                        final File resourceFile = getResourceFile(resourcePath, customPageName, apiSession);
                        pageRenderer.ensurePageFolderIsPresent(apiSession, pageRenderer.getPageResourceProvider(customPageName, apiSession.getTenantId()));
                        resourceRenderer.renderFile(request, response, resourceFile, apiSession);
                    }
                } catch (final Exception e) {
                    handleException(customPageName, e);
                }
            } else if (isValidPathForToken(THEME_PATH_SEPARATOR, pathSegments)) {
                //Support relative calls to the THEME from the application page using ../theme/
                final String themePath = pathInfo.substring(pathInfo.indexOf(THEME_PATH_SEPARATOR + "/"));
                request.getRequestDispatcher("/apps/" + appToken + themePath).forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "One of the separator '/content', '/theme' or '/API' is expected in the URL after the application token and the page token.");
            }

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "The info path is suppose to contain the application token, the page token and one of the separator '/content', '/theme' or '/API'.");
        }

    }

    private Long getCustomPageId(final String appToken, final String pageToken, final APISession apiSession,  final HttpServletResponse response) throws IOException, ServletException {
        try {
            return getApplicationApi(apiSession).getApplicationPage(appToken, pageToken).getPageId();
        } catch (final ApplicationPageNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error while trying to render the application page " + appToken+ "/" +pageToken, e);
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Cannot found the page" + pageToken + "for the application" + appToken + ".");
        } catch (final Exception e) {
            handleException(appToken + "/" + pageToken, e);
        }
        return null;
    }

    private String getCustomPageName(final String appToken, final String pageToken, final APISession apiSession,  final HttpServletResponse response) throws ServletException, IOException {
        try {
            final Long customPageId = getCustomPageId(appToken, pageToken,apiSession,response);
            return getPageApi(apiSession).getPage(customPageId).getName();
        } catch (final PageNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error while trying to render the application page " + appToken+ "/" +pageToken, e);
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Cannot found the page"+pageToken+"for the application"+appToken+".");
        } catch (final Exception e) {
            handleException(appToken + "/" + pageToken, e);
        }
        return "";
    }

    private boolean isValidPathForToken(final String TokenSeparator, final List<String> pathSegments) {
        return pathSegments.size() > 2 && pathSegments.get(2).equals(TokenSeparator.substring(1));
    }

    private boolean isNotResourcePath(final String resourcePath) {
        return resourcePath == null || CustomPageService.PAGE_INDEX_FILENAME.equals(resourcePath)
                || CustomPageService.PAGE_CONTROLLER_FILENAME.equals(resourcePath) || CustomPageService.PAGE_INDEX_NAME.equals(resourcePath);
    }

    private File getResourceFile(final String resourcePath, final String pageName, final APISession apiSession) throws IOException, BonitaException {
        final PageResourceProviderImpl pageResourceProvider = pageRenderer.getPageResourceProvider(pageName, apiSession.getTenantId());
        final File resourceFile = new File(pageResourceProvider.getPageDirectory(), CustomPageService.RESOURCES_PROPERTY + File.separator
                + resourcePath);

        if (!bonitaHomeFolderAccessor.isInFolder(resourceFile, pageResourceProvider.getPageDirectory())) {
            throw new BonitaException("Unauthorized access to the file " + resourcePath);
        }
        return resourceFile;
    }

    private boolean isAuthorized(final APISession apiSession, final String appToken, final String pageName) throws BonitaException {
        return getCustomPageAuthorizationsHelper(apiSession).isPageAuthorized(appToken, pageName);
    }

    private void handleException(final String pageName, final Exception e) throws ServletException {
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Error while trying to render the application page " + pageName, e);
        }
        throw new ServletException(e.getMessage());
    }

    protected ApplicationAPI getApplicationApi(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getLivingApplicationAPI(apiSession);
    }

    protected PageAPI getPageApi(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getCustomPageAPI(apiSession);
    }

    protected CustomPageAuthorizationsHelper getCustomPageAuthorizationsHelper(final APISession apiSession) throws BonitaHomeNotSetException,
            ServerAPIException, UnknownAPITypeException {
        return new CustomPageAuthorizationsHelper(new GetUserRightsHelper(apiSession),
                TenantAPIAccessor.getLivingApplicationAPI(apiSession), TenantAPIAccessor.getCustomPageAPI(apiSession));
    }
}
