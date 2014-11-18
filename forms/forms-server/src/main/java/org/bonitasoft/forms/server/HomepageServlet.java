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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.themes.CompilableFile;
import org.bonitasoft.console.common.server.themes.ThemeArchive;
import org.bonitasoft.console.common.server.themes.ThemeResourceServlet;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.ThemeAPI;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.theme.Theme;
import org.bonitasoft.engine.theme.ThemeType;
import org.bonitasoft.forms.server.accessor.impl.util.FormDocumentBuilderFactory;
import org.bonitasoft.forms.server.provider.FormServiceProvider;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderFactory;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;

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

    /**
     * portal UI mode parameter
     */
    public static final String UI_MODE_PARAM = "ui";

    public static final String LASTUPDATE_FILENAME = ".lastupdate";

    public static final String DEFAULT_CONSOLE_HOME_PAGE_FILENAME = "BonitaConsole.html";

    public static final String DEFAULT_FORM_HOME_PAGE_FILENAME = "BonitaForm.html";

    protected static final String HOMEPAGE_SERVLET_ID_IN_PATH = "homepage";

    protected static final String PORTAL_THEME_NAME = "portal";

    public static final String CONTENT_TYPE = "text/html";

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        final boolean isForm = isForm(getUrlPrefix(request), getUiMode(request));
        final String themeName = getThemeName(request);
        if (isForm && themeName != null) {
            // try to retrieve the homepage in the resources of the application
            showApplicationPage(request, response, themeName);
        } else {
            showDefaultPage(request, response, isForm);
        }
    }

    protected void showApplicationPage(final HttpServletRequest request, final HttpServletResponse response, final String themeName) {
        try {
            final APISession apiSession = getEngineSession(request);
            final File applicationDir = getApplicationFolder(apiSession, themeName);
            final File homepageFile = new File(applicationDir, DEFAULT_FORM_HOME_PAGE_FILENAME);
            if (homepageFile.exists()) {
                final byte[] content = FileUtils.readFileToByteArray(homepageFile);
                response.setContentLength(content.length);
                response.setContentType(CONTENT_TYPE);
                response.setCharacterEncoding("UTF-8");
                final OutputStream out = response.getOutputStream();
                out.write(content);
                out.close();
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "No file " + DEFAULT_FORM_HOME_PAGE_FILENAME + " present in the application resources. Using the default version.");
                }
                showDefaultPage(request, response, true);
            }
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error while loading the file " + DEFAULT_FORM_HOME_PAGE_FILENAME + " application " + themeName, e);
            }
        }
    }

    protected String getUiMode(final HttpServletRequest request) {
        return request.getParameter(UI_MODE_PARAM);
    }

    protected String getThemeName(final HttpServletRequest request) {
        return request.getParameter(THEME_PARAM_NAME);
    }

    protected String getUrlPrefix(final HttpServletRequest request) {
        return request.getServletPath().replaceAll(HOMEPAGE_SERVLET_ID_IN_PATH, "");
    }

    protected void showDefaultPage(final HttpServletRequest request, final HttpServletResponse response, final boolean isForm) {
        try {
            // Check if the portal theme directory exists,
            // if it doesn't, retrieve it from the engine and create a timestamp file with the theme last update date,
            // if it does retrieve the last update date from the engine and compare it to the timestamp file,
            // if the last update date is more recent, retrieve the theme again from the engine
            final File themesParentFolder = getResourcesParentFolder(request);
            final File themeFolder = new File(themesParentFolder, PORTAL_THEME_NAME);
            final APISession apiSession = getEngineSession(request);
            if (themeFolder.exists()) {
                final File timestampFile = new File(themeFolder, LASTUPDATE_FILENAME);
                final long lastUpdateTimestamp = getThemeLastUpdateDateFromEngine(apiSession);
                if (timestampFile.exists()) {
                    final String timestampString = FileUtils.readFileToString(timestampFile);
                    final long timestamp = Long.parseLong(timestampString);
                    if (lastUpdateTimestamp != timestamp) {
                        updateThemeFromEngine(apiSession, themeFolder);
                        FileUtils.writeStringToFile(timestampFile, String.valueOf(lastUpdateTimestamp), false);
                    }
                } else {
                    FileUtils.writeStringToFile(timestampFile, String.valueOf(lastUpdateTimestamp), false);
                }
            } else {
                updateThemeFromEngine(apiSession, themeFolder);
            }
            getResourceFile(request, response, PORTAL_THEME_NAME, getFileName(isForm));
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error while loading the file " + getFileName(isForm) + " in theme " + PORTAL_THEME_NAME, e);
            }
        }
    }

    protected long getThemeLastUpdateDateFromEngine(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        final ThemeAPI themeAPI = TenantAPIAccessor.getThemeAPI(apiSession);
        return themeAPI.getLastUpdateDate(ThemeType.PORTAL).getTime();
    }

    protected void updateThemeFromEngine(final APISession apiSession, final File portalThemeDirectory) throws BonitaHomeNotSetException,
            ServerAPIException, UnknownAPITypeException, IOException {
        Theme theme = TenantAPIAccessor.getThemeAPI(apiSession).getCurrentTheme(ThemeType.PORTAL);
        new ThemeArchive(theme.getContent())
                .extract(portalThemeDirectory)
                .compile(CompilableFile.ALWAYS_COMPILED_FILES)
                .add("bonita.css", theme.getCssContent());
    }

    protected String getFileName(final boolean isForm) {
        return isForm ? DEFAULT_FORM_HOME_PAGE_FILENAME : DEFAULT_CONSOLE_HOME_PAGE_FILENAME;
    }

    protected boolean isForm(final String urlPrefix, final String ui) {
        return ui != null && ui.equals("form");
    }

    public static File getApplicationFolder(final APISession apiSession, final String applicationIDString) throws ServletException {
        File myApplicationsFolder = null;
        long applicationID = 0;
        try {
            applicationID = Long.parseLong(applicationIDString);
        } catch (final NumberFormatException e) {
            final String errorMessage = "Cannot retrieve the application directory. Long expected for the theme parameter";
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        urlContext.put(FormServiceProviderUtil.PROCESS_UUID, applicationID);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.API_SESSION, apiSession);
        try {
            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(apiSession.getTenantId());
            final Date deployemenDate = formServiceProvider.getDeployementDate(context);
            // Make sure the application content has already been retrieved
            FormDocumentBuilderFactory.getFormDocumentBuilder(apiSession, applicationID, Locale.ENGLISH.getLanguage(), deployemenDate);
            myApplicationsFolder = formServiceProvider.getApplicationResourceDir(deployemenDate, context);
        } catch (final Throwable e) {
            final String errorMessage = "Error while using the servlet ThemeResourceServlet to get themes parent folder.";
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage);
            }
            throw new ServletException(errorMessage);
        }
        return myApplicationsFolder;
    }

    protected static APISession getEngineSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        return (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
    }
}
