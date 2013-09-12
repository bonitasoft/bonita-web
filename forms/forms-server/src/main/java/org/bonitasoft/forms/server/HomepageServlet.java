/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.bonitasoft.forms.server;

import org.bonitasoft.console.common.server.themes.ApplicationResourceServlet;
import org.bonitasoft.console.common.server.themes.ThemeResourceServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for requesting home page
 * 
 * @author Ruiheng Fan
 */
public class HomepageServlet extends ApplicationResourceServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = -2284578402691230994L;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(HomepageServlet.class.getName());

    public static final String CONTENT_TYPE = "text/html";

    /**
     * Theme parameter
     */
    public static final String THEME_PARAM = "theme";

    /**
     * user XP's UI mode parameter
     */
    public static final String UI_MODE_PARAM = "ui";

    public static final String DEFAULT_CONSOLE_HOME_PAGE_FILENAME = "BonitaConsole.html";

    private static final String DEFAULT_FORM_HOME_PAGE_FILENAME = "BonitaForm.html";

    protected static final String HOMEPAGE_SERVLET_ID_IN_PATH = "homepage";

    protected static final String DEFAULT_THEME_NAME = "default";

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        allwaysShowDefaultPage(request, response, isForm(getUrlPrefix(request), getUiMode(request)));
    }

    private String getUiMode(final HttpServletRequest request) {
        return request.getParameter(UI_MODE_PARAM);
    }

    private String getUrlPrefix(final HttpServletRequest request) {
        return request.getServletPath().replaceAll(HOMEPAGE_SERVLET_ID_IN_PATH, "");
    }

    private void allwaysShowDefaultPage(HttpServletRequest request, HttpServletResponse response, boolean isForm) {
        try {
            ThemeResourceServlet.getThemePackageFile(request, response, DEFAULT_THEME_NAME, getFileName(isForm));
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Get error while loading the " + getFileName(isForm) + " in theme " + DEFAULT_THEME_NAME);
            }
        }
    }

    private String getFileName(boolean isForm) {
        return isForm ? DEFAULT_FORM_HOME_PAGE_FILENAME : DEFAULT_CONSOLE_HOME_PAGE_FILENAME;
    }

    protected boolean isForm(final String urlPrefix, final String ui) {
        return ui != null && ui.equals("form");
    }

}
