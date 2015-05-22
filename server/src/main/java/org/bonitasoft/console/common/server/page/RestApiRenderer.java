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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import groovy.lang.GroovyClassLoader;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.api.extension.ResourceExtensionResolver;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * Class used by servlets to display a custom rest api
 * Since each instance of the servlet carry an instance of this class, it should have absolutely no instance attribute
 *
 * @author Laurent Leseigneur
 */
public class RestApiRenderer {

    private static final Logger LOGGER = Logger.getLogger(RestApiRenderer.class.getName());

    private final CustomPageService customPageService = new CustomPageService();

    public RestApiResponse handleRestApiCall(final HttpServletRequest request, ResourceExtensionResolver resourceExtensionResolver)
            throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException, BonitaException {
        final PageContextHelper pageContextHelper = new PageContextHelper(request);
        final APISession apiSession = pageContextHelper.getApiSession();
        Long pageId = resourceExtensionResolver.resolvePageId(apiSession);
        final Page page = customPageService.getPage(apiSession, pageId);
        final PageResourceProvider pageResourceProvider = new PageResourceProvider(page, apiSession.getTenantId());
        customPageService.ensurePageFolderIsUpToDate(apiSession, pageResourceProvider);
        String classFileName = resourceExtensionResolver.resolveClassFileName(pageResourceProvider);
        final String mappingKey = resourceExtensionResolver.generateMappingKey();
        if (isFileGroovyPage(pageResourceProvider, classFileName)) {
            return renderResponse(request, apiSession, pageContextHelper, pageResourceProvider, classFileName, mappingKey);
        }
        LOGGER.log(Level.SEVERE, "resource does not exists:" + mappingKey);
        throw new BonitaException("unable to handle rest api call to " + mappingKey);
    }

    private boolean isFileGroovyPage(final PageResourceProvider pageResourceProvider, String classFileName) {
        final File pageFolder = pageResourceProvider.getPageDirectory();
        final File indexGroovy = customPageService.getPageFile(pageFolder, classFileName);
        return indexGroovy.exists();
    }

    private RestApiResponse renderResponse(final HttpServletRequest request, final APISession apiSession, final PageContextHelper pageContextHelper,
                                           final PageResourceProvider pageResourceProvider, String classFileName, String mappingKey)
            throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException, BonitaException {
        final ClassLoader originalClassloader = Thread.currentThread().getContextClassLoader();
        final GroovyClassLoader pageClassloader = customPageService.getPageClassloader(apiSession, pageResourceProvider);
        try {
            Thread.currentThread().setContextClassLoader(pageClassloader);
            final Class<RestApiController> restApiControllerClass = customPageService.registerRestApiPage(pageClassloader, pageResourceProvider, classFileName);
            final RestApiController restApiController = customPageService.loadRestApiPage(restApiControllerClass);
            pageResourceProvider.setResourceClassLoader(pageClassloader);
            try {
                return restApiController.doHandle(request, pageResourceProvider,
                        new PageContext(apiSession, pageContextHelper.getCurrentLocale(), pageContextHelper.getCurrentProfile()), new RestApiResponseBuilder(), new RestApiUtilImpl());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "error when executing rest api call to " + mappingKey, e);
                throw e;
            }
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassloader);
        }
    }

}
