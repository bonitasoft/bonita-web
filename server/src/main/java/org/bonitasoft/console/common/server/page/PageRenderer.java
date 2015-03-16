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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * Class used by servlets to display a custom page
 * Since each instance of the servlet carry an instance of this class, it should have absolutely no instance attribute
 *
 * @author Anthony Birembaut
 */
public class PageRenderer {

    public static final String PROFILE_PARAM = "profile";

    public static final String LOCALE_PARAM = "locale";

    public static final String DEFAULT_LOCALE = "en";

    public void displayCustomPage(final HttpServletRequest request, final HttpServletResponse response, final APISession apiSession, final String pageName)
            throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException, BonitaException {

        final CustomPageService customPageService = new CustomPageService();
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
