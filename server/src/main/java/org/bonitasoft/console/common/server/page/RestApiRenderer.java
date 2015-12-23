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

import org.bonitasoft.console.common.server.page.extension.PageContextImpl;
import org.bonitasoft.console.common.server.page.extension.PageResourceProviderImpl;
import org.bonitasoft.console.common.server.page.extension.RestAPIContextImpl;
import org.bonitasoft.console.common.server.page.extension.RestApiUtilImpl;
import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.api.extension.ResourceExtensionResolver;
import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.GroovyClassLoader;

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
        final Long pageId = resourceExtensionResolver.resolvePageId(apiSession);
        final Page page = customPageService.getPage(apiSession, pageId);
        final PageResourceProviderImpl pageResourceProvider = new PageResourceProviderImpl(page, apiSession.getTenantId());
        customPageService.ensurePageFolderIsUpToDate(apiSession, pageResourceProvider);
        final File restApiControllerFile = resourceExtensionResolver.resolveRestApiControllerFile(pageResourceProvider);
        final String mappingKey = resourceExtensionResolver.generateMappingKey();
        if (restApiControllerFile.exists()) {
            return renderResponse(request, apiSession, pageContextHelper, pageResourceProvider, restApiControllerFile, mappingKey);
        }
        LOGGER.log(Level.SEVERE, "resource does not exists:" + mappingKey);
        throw new BonitaException("unable to handle rest api call to " + mappingKey);
    }

    private RestApiResponse renderResponse(final HttpServletRequest request, final APISession apiSession, final PageContextHelper pageContextHelper,
            final PageResourceProviderImpl pageResourceProvider, File restApiControllerFile, String mappingKey)
                    throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException, BonitaException {
        final ClassLoader originalClassloader = Thread.currentThread().getContextClassLoader();
        final GroovyClassLoader pageClassloader = customPageService.getPageClassloader(apiSession, pageResourceProvider);
        try {
            Thread.currentThread().setContextClassLoader(pageClassloader);
            final Class<?> restApiControllerClass = customPageService.registerRestApiPage(pageClassloader, restApiControllerFile);
            pageResourceProvider.setResourceClassLoader(pageClassloader);
            try {
                return doHandle(request, apiSession, pageContextHelper, pageResourceProvider, restApiControllerClass);
            } catch (Throwable e) {
                LOGGER.log(Level.SEVERE, "Error when executing rest api extension call to " + mappingKey, e);
                throw e;
            }
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassloader);
        }
    }

    protected RestApiResponse doHandle(final HttpServletRequest request,
            final APISession apiSession,
            final PageContextHelper pageContextHelper,
            final PageResourceProviderImpl pageResourceProvider,
            final Class<?> restApiControllerClass) throws InstantiationException, IllegalAccessException {
        if (RestApiController.class.isAssignableFrom(restApiControllerClass)) {//LEGACY MODE
            final RestApiController restApiController = instantiate(restApiControllerClass, RestApiController.class);
            return restApiController.doHandle(request, pageResourceProvider,
                    new PageContextImpl(apiSession, pageContextHelper.getCurrentLocale(), pageContextHelper.getCurrentProfile()),
                    new RestApiResponseBuilder(), new RestApiUtilImpl());
        } else {
            final org.bonitasoft.web.extension.rest.RestApiController restApiController = instantiate(restApiControllerClass,
                    org.bonitasoft.web.extension.rest.RestApiController.class);
            return restApiController.doHandle(request,
                    new org.bonitasoft.web.extension.rest.RestApiResponseBuilder(),
                    new RestAPIContextImpl(apiSession, new APIClient(apiSession), pageContextHelper.getCurrentLocale(), pageResourceProvider));
        }
    }

    protected <T extends Object> T instantiate(Class<?> baseClass, Class<T> toClass) throws InstantiationException, IllegalAccessException {
        return (T) baseClass.newInstance();
    }

}
