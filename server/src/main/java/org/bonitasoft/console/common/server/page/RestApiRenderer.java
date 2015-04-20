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

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;
import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.GroovyClassLoader;

/**
 * Class used by servlets to display a custom rest api
 * Since each instance of the servlet carry an instance of this class, it should have absolutely no instance attribute
 *
 * @author Laurent Leseigneur
 */
public class RestApiRenderer {


    private final CustomPageService customPageService = new CustomPageService();

    public Object handleRestApiCall(final HttpServletRequest request, final String pageName)
            throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException, BonitaException {

        final PageContextHelper pageContextHelper = new PageContextHelper(request);
        final APISession apiSession = pageContextHelper.getApiSession();
        final PageResourceProvider pageResourceProvider = new PageResourceProvider(pageName, apiSession.getTenantId());
        customPageService.ensurePageFolderIsUpToDate(apiSession, pageResourceProvider);
        if (isGroovyPage(pageResourceProvider)) {
            return displayGroovyPage(request, apiSession, pageContextHelper, pageResourceProvider);
        }
        throw new BonitaException("unable to handle custom rest api call to " + pageName);
    }

    private boolean isGroovyPage(final PageResourceProvider pageResourceProvider) {
        final File pageFolder = pageResourceProvider.getPageDirectory();
        final File indexGroovy = customPageService.getGroovyPageFile(pageFolder);
        return indexGroovy.exists();
    }

    private Object displayGroovyPage(final HttpServletRequest request, final APISession apiSession, final PageContextHelper pageContextHelper,
            final PageResourceProvider pageResourceProvider)
            throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException, BonitaException {
        final ClassLoader originalClassloader = Thread.currentThread().getContextClassLoader();
        final GroovyClassLoader pageClassloader = customPageService.getPageClassloader(apiSession, pageResourceProvider);
        try {
            Thread.currentThread().setContextClassLoader(pageClassloader);
            final Class<RestApiController> restApiControllerClass = customPageService.registerRestApiPage(pageClassloader, pageResourceProvider);
            final RestApiController restApiController = customPageService.loadRestApiPage(restApiControllerClass);
            pageResourceProvider.setResourceClassLoader(pageClassloader);
            return restApiController.doHandle(request, pageResourceProvider,
                    new PageContext(apiSession, pageContextHelper.getCurrentLocale(), pageContextHelper.getCurrentProfile()));
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassloader);
        }
    }

}
