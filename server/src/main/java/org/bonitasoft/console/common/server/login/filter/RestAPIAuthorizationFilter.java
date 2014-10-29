/*
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
package org.bonitasoft.console.common.server.login.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.preferences.properties.DynamicPermissionsChecks;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.engine.api.PermissionAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.permission.APICallContext;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ExecutionException;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.web.rest.server.framework.utils.RestRequestParser;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Zhiheng Yang, Chong Zhao
 * @author Baptiste Mesta
 * @author Anthony Birembaut
 */
public class RestAPIAuthorizationFilter extends AbstractAuthorizationFilter {

    private static final String PLATFORM_API_URI = "API/platform/";

    protected static final String PLATFORM_SESSION_PARAM_KEY = "platformSession";

    @Override
    protected boolean checkValidCondition(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws ServletException {
        try {
            if (httpRequest.getRequestURI().contains(PLATFORM_API_URI)) {
                final PlatformSession platformSession = (PlatformSession) httpRequest.getSession().getAttribute(PLATFORM_SESSION_PARAM_KEY);
                if (platformSession != null) {
                    return true;
                } else {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                final APISession apiSession = (APISession) httpRequest.getSession().getAttribute(LoginManager.API_SESSION_PARAM_KEY);
                if (apiSession == null) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else if (!checkPermissions(httpRequest)) {
                    httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    return true;
                }
            }
            return false;
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new ServletException(e);
        }
    }

    protected boolean checkPermissions(final HttpServletRequest request) throws ServletException {
        final RestRequestParser restRequestParser = new RestRequestParser(request).invoke();
        return checkPermissions(request, restRequestParser.getApiName(), restRequestParser.getResourceName(), restRequestParser.getId());
    }

    protected boolean checkPermissions(final HttpServletRequest request, final String apiName, final String resourceName, final APIID id)
            throws ServletException {
        final String method = request.getMethod();
        final HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        final Set<String> permissions = (Set<String>) session.getAttribute(LoginManager.PERMISSIONS_SESSION_PARAM_KEY);
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        final Long tenantId = apiSession.getTenantId();
        final boolean apiAuthorizationsCheckEnabled = isApiAuthorizationsCheckEnabled(tenantId);
        if (!apiAuthorizationsCheckEnabled || apiSession.isTechnicalUser()) {
            return true;
        }
        final ResourcesPermissionsMapping resourcesPermissionsMapping = getResourcesPermissionsMapping(tenantId);
        final String resourceId = id != null ? id.getPart(0) : null;

        boolean authorized = staticCheck(method, apiName, resourceName, resourceId, permissions, resourcesPermissionsMapping, apiSession.getUserName());
        if (!authorized) {
            final DynamicPermissionsChecks dynamicPermissionsChecks = getDynamicPermissionsChecks(tenantId);
            final String requestBody = getRequestBody(request);
            authorized = dynamicCheck(method, apiName, resourceName, resourceId, apiSession, dynamicPermissionsChecks, request.getQueryString(), requestBody);
        }
        return authorized;
    }

    protected String getRequestBody(final HttpServletRequest request) throws ServletException {
        ServletInputStream inputStream = null;
        try {
            try {
                inputStream = request.getInputStream();
                return IOUtils.toString(inputStream, request.getCharacterEncoding());
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (final IOException e) {
            throw new ServletException(e);
        }
    }

    protected boolean isApiAuthorizationsCheckEnabled(final Long tenantId) {
        return PropertiesFactory.getSecurityProperties(tenantId).isAPIAuthorizationsCheckEnabled();
    }

    protected ResourcesPermissionsMapping getResourcesPermissionsMapping(final long tenantId) {
        return PropertiesFactory.getResourcesPermissionsMapping(tenantId);
    }

    protected DynamicPermissionsChecks getDynamicPermissionsChecks(final long tenantId) {
        return PropertiesFactory.getDynamicPermissionsChecks(tenantId);
    }

    protected boolean staticCheck(final String method, final String apiName, final String resourceName, final String resourceId,
            final Set<String> permissionsOfUser,
            final ResourcesPermissionsMapping resourcesPermissionsMapping, final String username) {
        List<String> resourcePermissions = resourcesPermissionsMapping.getResourcePermissions(method, apiName, resourceName, resourceId);
        if (resourcePermissions.isEmpty()) {
            resourcePermissions = resourcesPermissionsMapping.getResourcePermissions(method, apiName, resourceName);
        }
        for (final String resourcePermission : resourcePermissions) {
            if (permissionsOfUser.contains(resourcePermission)) {
                return true;
            }
        }
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Unauthorized access to " + method + " " + apiName + "/" + resourceName + (resourceId != null ? "/" + resourceId : "")
                    + " attempted by " + username + " required permissions: " + resourcePermissions);
        }
        return false;
    }

    protected boolean dynamicCheck(final String method, final String apiName, final String resourceName, final String resourceId, final APISession apiSession,
            final DynamicPermissionsChecks dynamicPermissionsChecks, final String queryString, final String body) throws ServletException {
        String resourceClassname = dynamicPermissionsChecks.getResourceScript(method, apiName, resourceName, resourceId);
        if (resourceClassname == null) {
            resourceClassname = dynamicPermissionsChecks.getResourceScript(method, apiName, resourceName);
        }
        if (resourceClassname != null) {
            final APICallContext apiCallContext = new APICallContext(method, apiName, resourceName, resourceId, queryString, body);
            try {
                return checkWithScript(method, apiName, resourceName, resourceId, apiSession, resourceClassname, apiCallContext);
            } catch (final NotFoundException e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Unable to find the dynamic permissions script: " + resourceClassname, e);
                }
                return false;
            } catch (final ExecutionException e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Unable to execute the dynamic permissions script: " + resourceClassname, e);
                }
                return false;
            } catch (final BonitaException e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Unable to retrieve the permissions API", e);
                }
                throw new ServletException(e);
            }
        }
        return true;
    }

    protected boolean checkWithScript(final String method, final String apiName, final String resourceName, final String resourceId,
            final APISession apiSession,
            final String resourceClassname, final APICallContext apiCallContext) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException,
            ExecutionException, NotFoundException {
        final PermissionAPI permissionAPI = TenantAPIAccessor.getPermissionAPI(apiSession);
        final boolean authorized = permissionAPI.checkAPICallWithScript(resourceClassname, apiCallContext);
        if (!authorized) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Unauthorized access to " + method + " " + apiName + "/" + resourceName
                        + (resourceId != null ? "/" + resourceId : "")
                        + " attempted by " + apiSession.getUserName() + " Permission script: " + resourceClassname);
            }
        }
        return authorized;
    }
}
