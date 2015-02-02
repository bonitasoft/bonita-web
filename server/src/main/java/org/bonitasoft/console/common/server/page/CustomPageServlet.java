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

import groovy.lang.GroovyClassLoader;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;

public class CustomPageServlet extends HttpServlet {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(CustomPageServlet.class.getName());

    /**
     * uuid
     */
    private static final long serialVersionUID = -5410859017103815654L;

    private static final String PAGE_NAME_PARAM = "page";

    private static final String APP_ID_PARAM = "applicationId";

    private static final String PROFILE_PARAM = "profile";

    private static final String LOCALE_PARAM = "locale";

    private static final String DEFAULT_LOCALE = "en";

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String appId = request.getParameter(APP_ID_PARAM);
        final String pageName = request.getParameter(PAGE_NAME_PARAM);
        final CustomPageService customPageService = new CustomPageService();
        final APISession apiSession = getEngineSession(request);

        try {

            if (!getCustomPageAuthorizationsHelper(apiSession).isPageAuthorized(appId, pageName)) {
                throw new IllegalAccessException("User not Authorized");
            }

            final PageResourceProvider pageResourceProvider = new PageResourceProvider(pageName, apiSession.getTenantId());
            final ClassLoader originalClassloader = Thread.currentThread().getContextClassLoader();
            final GroovyClassLoader pageClassloader = customPageService.getPageClassloader(apiSession, pageName, pageResourceProvider);
            try {
                Thread.currentThread().setContextClassLoader(pageClassloader);
                final Class<PageController> pageClass = customPageService.registerPage(pageClassloader, pageResourceProvider);
                final PageController pageController = customPageService.loadPage(pageClass);
                pageResourceProvider.setResourceClassLoader(pageClassloader);
                pageController.doGet(request, response, pageResourceProvider,
                        new PageContext(apiSession, getCurrentLocale(request), getCurrentProfile(request)));
            } finally {
                Thread.currentThread().setContextClassLoader(originalClassloader);
            }
        } catch (final Exception e) {
            handleException(pageName, e);
        }
    }

    protected CustomPageAuthorizationsHelper getCustomPageAuthorizationsHelper(final APISession apiSession) throws BonitaHomeNotSetException,
            ServerAPIException, UnknownAPITypeException {
        return new CustomPageAuthorizationsHelper(new GetUserRightsHelper(apiSession),
                TenantAPIAccessor.getLivingApplicationAPI(apiSession), TenantAPIAccessor.getCustomPageAPI(apiSession));
    }

    protected String getCurrentProfile(final HttpServletRequest request) {
        return request.getParameter(PROFILE_PARAM);
    }

    protected Locale getCurrentLocale(final HttpServletRequest request) {
        final String locale = request.getParameter(LOCALE_PARAM);
        if (locale == null) {
            for(final Cookie cookie: request.getCookies()) {
                if (cookie.getName().equals("BOS_Locale")) {
                    return new Locale(cookie.getValue());
                }
            }
            return new Locale(DEFAULT_LOCALE);
        }
        return new Locale(locale);
    }

    protected void handleException(final String pageName, final Exception e) throws ServletException {
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Error while trying to render the custom page " + pageName, e);
        }
        throw new ServletException(e.getMessage());
    }

    protected static APISession getEngineSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        return (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
    }

}
