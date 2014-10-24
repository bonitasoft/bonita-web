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

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.web.rest.server.framework.utils.RestRequestParser;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Zhiheng Yang, Chong Zhao
 * @author Baptiste Mesta
 */
public class RestAPIAuthorizationFilter extends AbstractAuthorizationFilter {

    private static final String PLATFORM_API_URI = "API/platform/";

    protected static final String PLATFORM_SESSION_PARAM_KEY = "platformSession";

    @Override
    boolean checkValidCondition(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException {
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

    boolean checkPermissions(HttpServletRequest request) {
        RestRequestParser restRequestParser = new RestRequestParser(request).invoke();

        return checkPermissions(request, restRequestParser.getApiName(), restRequestParser.getResourceName(), restRequestParser.getId());
    }

    boolean checkPermissions(HttpServletRequest request, String apiName, String resourceName, APIID id) {
        String method = request.getMethod();
        HttpSession session = request.getSession();
        Set<String> permissions = (Set<String>) session.getAttribute(LoginManager.PERMISSIONS_SESSION_PARAM_KEY);
        APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        Long tenantId = apiSession.getTenantId();
        boolean apiAuthorizationsCheckEnabled = isApiAuthorizationsCheckEnabled(tenantId);
        if (!apiAuthorizationsCheckEnabled || apiSession.isTechnicalUser()) {
            return true;
        }
        ResourcesPermissionsMapping resourcesPermissionsMapping = getResourcesPermissionsMapping(tenantId);
        String resourceId = id != null ? id.getPart(0) : null;

        return staticCheck(method, apiName, resourceName, resourceId, permissions, resourcesPermissionsMapping, apiSession.getUserName());
    }

    boolean isApiAuthorizationsCheckEnabled(Long tenantId) {
        return PropertiesFactory.getSecurityProperties(tenantId).isAPIAuthorizationsCheckEnabled();
    }

    ResourcesPermissionsMapping getResourcesPermissionsMapping(long tenantId) {
        return PropertiesFactory.getResourcesPermissionsMapping(tenantId);
    }

    boolean staticCheck(String method, String apiName, String resourceName, String resourceId, Set<String> permissionsOfUser,
            ResourcesPermissionsMapping resourcesPermissionsMapping, String username) {
        List<String> resourcePermissions = resourcesPermissionsMapping.getResourcePermissions(method, apiName, resourceName, resourceId);
        if (resourcePermissions.isEmpty()) {
            resourcePermissions = resourcesPermissionsMapping.getResourcePermissions(method, apiName, resourceName, null);
        }
        //get the resource permission mapping
        for (String resourcePermission : resourcePermissions) {
            if (permissionsOfUser.contains(resourcePermission)) {
                return true;
            }
        }
        LOGGER.log(Level.WARNING, "Unauthorized access to " + method + " " + apiName + "/" + resourceName + (resourceId != null ? "/" + resourceId : "")
                + " attempted by " + username + " required permissions:" + resourcePermissions);
        return false;
    }

}
