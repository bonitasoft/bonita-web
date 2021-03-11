/**
 * Copyright (C) 2011 BonitaSoft S.A.
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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.themes.ThemeExtractor;
import org.bonitasoft.console.common.server.themes.ThemeResourceServlet;
import org.bonitasoft.console.common.server.utils.LocaleUtils;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.theme.ThemeType;

/**
 * Servlet for requesting home page
 *
 * @author Ruiheng Fan, Vincent Elcrin, Anthony Birembaut
 */
public class HomepageServlet extends ThemeResourceServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = -2284578402691230994L;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(HomepageServlet.class.getName());

    public static final String LASTUPDATE_FILENAME = ".lastupdate";

    public static final String DEFAULT_CONSOLE_HOME_PAGE_FILENAME = "BonitaConsole.html";

    protected static final String HOMEPAGE_SERVLET_ID_IN_PATH = "homepage";

    public static final String PORTAL_THEME_NAME = "portal";

    public static final String CONTENT_TYPE = "text/html";

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        enforceLocaleCookieIfPresentInURLOrBrowser(request, response);
        showDefaultPage(request, response);
    }

    protected void enforceLocaleCookieIfPresentInURLOrBrowser(final HttpServletRequest request, final HttpServletResponse response) {
        final String localeFromCookie = LocaleUtils.getLocaleFromCookies(request);
        final String localeFromURL = LocaleUtils.getLocaleFromRequestURL(request);

        if (localeFromCookie != null && !LocaleUtils.isLocaleSupportedInPortal(localeFromCookie)) {
            LocaleUtils.addOrReplaceLocaleCookieResponse(response, localeFromCookie);
        }

        if (localeFromURL != null && !localeFromURL.equals(localeFromCookie)) {
            //Set the cookie if the locale is in the URL and different from the existing cookie value or the cookie does not exist yet
            LocaleUtils.addOrReplaceLocaleCookieResponse(response, localeFromURL);
        } else if (localeFromCookie == null) {
            //Set the cookie with the browser locale if there is no cookie
            final String localeFromBrowser = LocaleUtils.getLocaleFromBrowser(request);
            if (localeFromBrowser != null) {
                LocaleUtils.addOrReplaceLocaleCookieResponse(response, localeFromBrowser);
            }
        }
    }

    protected String getThemeName(final HttpServletRequest request) {
        return request.getParameter(THEME_PARAM_NAME);
    }

    protected String getUrlPrefix(final HttpServletRequest request) {
        return request.getServletPath().replaceAll(HOMEPAGE_SERVLET_ID_IN_PATH, "");
    }

    protected void showDefaultPage(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            // Check if the portal theme directory exists,
            // if it doesn't, retrieve it from the engine and create a timestamp file with the theme last update date,
            // if it does retrieve the last update date from the engine and compare it to the timestamp file,
            // if the last update date is more recent, retrieve the theme again from the engine
            final APISession apiSession = getEngineSession(request);
            final File themeFolder = getResourcesParentFolder(request);
            new ThemeExtractor().retrieveAndExtractCurrentTheme(themeFolder, apiSession, ThemeType.PORTAL);
            getResourceFile(request, response, PORTAL_THEME_NAME, DEFAULT_CONSOLE_HOME_PAGE_FILENAME);
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error while loading the file " + DEFAULT_CONSOLE_HOME_PAGE_FILENAME + " in theme " + PORTAL_THEME_NAME, e);
            }
        }
    }

    protected static APISession getEngineSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        return (APISession) session.getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
    }
}
