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
package org.bonitasoft.console.common.server.login.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.preferences.properties.DynamicPermissionsChecks;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.console.common.server.utils.PermissionsBuilder;
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

    public static final String SCRIPT_TYPE_AUTHORIZATION_PREFIX = "check";

    private static final String PLATFORM_API_URI = "API/platform/";

    protected static final String PLATFORM_SESSION_PARAM_KEY = "platformSession";

    @Override
    protected HttpServletRequest getRequest(final ServletRequest request) {
        //we need to use a MultiReadHttpServletRequest wrapper in order to be able to get the inputstream twice (in the filter and in the API servlet)
        return new MultiReadHttpServletRequest((HttpServletRequest) request);
    }

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
        final Set<String> userPermissions = (Set<String>) session.getAttribute(LoginManager.PERMISSIONS_SESSION_PARAM_KEY);
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        final Long tenantId = apiSession.getTenantId();
        final boolean apiAuthorizationsCheckEnabled = isApiAuthorizationsCheckEnabled(tenantId);
        if (!apiAuthorizationsCheckEnabled || apiSession.isTechnicalUser()) {
            return true;
        }
        final ResourcesPermissionsMapping resourcesPermissionsMapping = getResourcesPermissionsMapping(tenantId);
        final String resourceId = id != null ? id.getPart(0) : null;

        final DynamicPermissionsChecks dynamicPermissionsChecks = getDynamicPermissionsChecks(tenantId);

        final Set<String> resourceAuthorizations = getDynamicAuthorizations(apiName, resourceName, method, resourceId, dynamicPermissionsChecks);
        if (!resourceAuthorizations.isEmpty()) {
            //if there is a dynamic rule, use it to check the permissions
            final String requestBody = getRequestBody(request);
            final APICallContext apiCallContext = new APICallContext(method, apiName, resourceName, resourceId, request.getQueryString(), requestBody);
            return dynamicCheck(apiCallContext, userPermissions, resourceAuthorizations, apiSession);
        } else {
            //if there is no dynamic rule, use the static permissions
            final APICallContext apiCallContext = new APICallContext(method, apiName, resourceName, resourceId);
            return staticCheck(apiCallContext, userPermissions, resourcesPermissionsMapping, apiSession.getUserName());
        }
    }

    protected Set<String> getDynamicAuthorizations(final String apiName, final String resourceName, final String method, final String resourceId,
            final DynamicPermissionsChecks dynamicPermissionsChecks) {
        Set<String> resourcePermissions = dynamicPermissionsChecks.getResourcePermissions(method, apiName, resourceName, resourceId);
        if (resourcePermissions.isEmpty()) {
            resourcePermissions = dynamicPermissionsChecks.getResourcePermissions(method, apiName, resourceName);
        }
        return resourcePermissions;
    }

    protected String getRequestBody(final HttpServletRequest request) throws ServletException {
        try {
            final ServletInputStream inputStream = request.getInputStream();
            return IOUtils.toString(inputStream, request.getCharacterEncoding());
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

    protected boolean staticCheck(final APICallContext apiCallContext, final Set<String> permissionsOfUser,
            final ResourcesPermissionsMapping resourcesPermissionsMapping, final String username) {
        Set<String> resourcePermissions = resourcesPermissionsMapping.getResourcePermissions(apiCallContext.getMethod(), apiCallContext.getApiName(),
                apiCallContext.getResourceName(), apiCallContext.getResourceId());
        if (resourcePermissions.isEmpty()) {
            resourcePermissions = resourcesPermissionsMapping.getResourcePermissions(apiCallContext.getMethod(), apiCallContext.getApiName(),
                    apiCallContext.getResourceName());
        }
        for (final String resourcePermission : resourcePermissions) {
            if (permissionsOfUser.contains(resourcePermission)) {
                return true;
            }
        }
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST,
                    "Unauthorized access to " + apiCallContext.getMethod() + " " + apiCallContext.getApiName() + "/" + apiCallContext.getResourceName()
                            + (apiCallContext.getResourceId() != null ? "/" + apiCallContext.getResourceId() : "") + " attempted by " + username
                            + " required permissions: " + resourcePermissions);
        }
        return false;
    }

    protected boolean dynamicCheck(final APICallContext apiCallContext, final Set<String> userPermissions, final Set<String> resourceAuthorizations,
            final APISession apiSession) throws ServletException {
        checkResourceAuthorizationsSyntax(resourceAuthorizations);
        if (resourceAuthorizations.contains(PermissionsBuilder.USER_TYPE_AUTHORIZATION_PREFIX + "|" + apiSession.getUserName())) {
            return true;
        }
        final Set<String> profileAuthorizations = getResourceProfileAuthorizations(resourceAuthorizations);
        for (final String profileAuthorization : profileAuthorizations) {
            if (userPermissions.contains(profileAuthorization)) {
                return true;
            }
        }
        final String resourceClassname = getResourceClassname(resourceAuthorizations);
        if (resourceClassname != null) {
            try {
                return checkWithScript(apiSession, resourceClassname, apiCallContext);
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
        return false;
    }

    protected boolean checkResourceAuthorizationsSyntax(final Set<String> resourceAuthorizations) {
        boolean valid = true;
        for (final String resourceAuthorization : resourceAuthorizations) {
            if (!resourceAuthorization.matches("(" + PermissionsBuilder.USER_TYPE_AUTHORIZATION_PREFIX + "|"
                    + PermissionsBuilder.PROFILE_TYPE_AUTHORIZATION_PREFIX + "|" + SCRIPT_TYPE_AUTHORIZATION_PREFIX + ")\\|.+")) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Error while getting dynamic authoriations. Unknown syntax: " + resourceAuthorization
                            + " defined in dynamic-permissions-checks.properties");
                }
                valid = false;
            }
        }
        return valid;
    }

    protected String getResourceClassname(final Set<String> resourcePermissions) {
        String className = null;
        for (final String resourcePermission : resourcePermissions) {
            if (resourcePermission.startsWith(SCRIPT_TYPE_AUTHORIZATION_PREFIX + "|")) {
                className = resourcePermission.substring((SCRIPT_TYPE_AUTHORIZATION_PREFIX + "|").length());
            }
        }
        return className;
    }

    protected Set<String> getResourceProfileAuthorizations(final Set<String> resourcePermissions) {
        final Set<String> profileAuthorizations = new HashSet<String>();
        for (final String athorizedItem : resourcePermissions) {
            if (athorizedItem.startsWith(PermissionsBuilder.PROFILE_TYPE_AUTHORIZATION_PREFIX + "|")) {
                profileAuthorizations.add(athorizedItem);
            }
        }
        return profileAuthorizations;
    }

    protected boolean checkWithScript(final APISession apiSession, final String resourceClassname, final APICallContext apiCallContext)
            throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException,
            ExecutionException, NotFoundException {
        final PermissionAPI permissionAPI = TenantAPIAccessor.getPermissionAPI(apiSession);
        final boolean authorized = permissionAPI.checkAPICallWithScript(resourceClassname, apiCallContext);
        if (!authorized) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(
                        Level.FINEST,
                        "Unauthorized access to " + apiCallContext.getMethod() + " " + apiCallContext.getApiName() + "/" + apiCallContext.getResourceName()
                                + (apiCallContext.getResourceId() != null ? "/" + apiCallContext.getResourceId() : "") + " attempted by "
                                + apiSession.getUserName() + " Permission script: " + resourceClassname);
            }
        }
        return authorized;
    }
}
