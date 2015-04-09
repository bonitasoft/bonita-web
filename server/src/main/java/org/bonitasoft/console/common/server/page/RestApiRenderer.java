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

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import groovy.lang.GroovyClassLoader;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * Class used by servlets to display a custom rest api
 * Since each instance of the servlet carry an instance of this class, it should have absolutely no instance attribute
 *
 * @author Laurent Leseigneur
 */
public class RestApiRenderer {

    public static final String PROFILE_PARAM = "profile";

    public static final String LOCALE_PARAM = "locale";

    public static final String DEFAULT_LOCALE = "en";

    private CustomPageService customPageService = new CustomPageService();

    public Object handleCustomRestApiCall(final HttpServletRequest request, final APISession apiSession, final String pageName)
            throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException, BonitaException {

        PageResourceProvider pageResourceProvider = new PageResourceProvider(pageName, apiSession.getTenantId());
        customPageService.ensurePageFolderIsUpToDate(apiSession, pageName, pageResourceProvider);
        if (isGroovyPage(pageResourceProvider)) {
            return displayGroovyPage(request, apiSession, pageName, pageResourceProvider);
        }
        throw new BonitaException("unable to handle custom rest api call to " + pageName);
    }

    private boolean isGroovyPage(PageResourceProvider pageResourceProvider) {
        File pageFolder = pageResourceProvider.getPageDirectory();
        File indexGroovy = customPageService.getGroovyPageFile(pageFolder);
        return indexGroovy.exists();
    }

    private Object displayGroovyPage(final HttpServletRequest request, final APISession apiSession, final String pageName, final PageResourceProvider pageResourceProvider)
            throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException, BonitaException {
        final ClassLoader originalClassloader = Thread.currentThread().getContextClassLoader();
        final GroovyClassLoader pageClassloader = customPageService.getPageClassloader(apiSession, pageName, pageResourceProvider);
        try {
            Thread.currentThread().setContextClassLoader(pageClassloader);
            final Class<RestApiController> pageClass = customPageService.registerRestApiPage(pageClassloader, pageResourceProvider);
            final RestApiController restApiController = customPageService.loadRestApiPage(pageClass);
            pageResourceProvider.setResourceClassLoader(pageClassloader);
            return restApiController.doHandle(request, pageResourceProvider,
                    new PageContext(apiSession, getCurrentLocale(request), getCurrentProfile(request)));
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassloader);
        }
    }

    public String getCurrentProfile(final HttpServletRequest request) {
        return request.getParameter(PROFILE_PARAM);
    }

    public Locale getCurrentLocale(final HttpServletRequest request) {
        final String locale = request.getParameter(LOCALE_PARAM);
        if (locale == null) {
            for (final Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("BOS_Locale")) {
                    return new Locale(cookie.getValue());
                }
            }
            return new Locale(DEFAULT_LOCALE);
        }
        return new Locale(locale);
    }


}
