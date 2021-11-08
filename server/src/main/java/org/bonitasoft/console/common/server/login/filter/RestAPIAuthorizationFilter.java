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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.api.PermissionAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.permission.APICallContext;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ExecutionException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.web.rest.server.framework.utils.RestRequestParser;
import org.bonitasoft.web.toolkit.client.common.i18n.model.I18nLocaleDefinition;
import org.bonitasoft.web.toolkit.client.common.session.SessionDefinition;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Zhiheng Yang, Chong Zhao
 * @author Baptiste Mesta
 * @author Anthony Birembaut
 */
public class RestAPIAuthorizationFilter extends AbstractAuthorizationFilter {

    private static final String PLATFORM_API_URI_REGEXP = ".*(API|APIToolkit)/platform/.*";

    protected static final String PLATFORM_SESSION_PARAM_KEY = "platformSession";

    /**
     * Logger
     */
    protected static final Logger LOGGER = Logger.getLogger(RestAPIAuthorizationFilter.class.getName());

    private final Boolean reload;

    public RestAPIAuthorizationFilter(final boolean reload) {
        this.reload = reload;
    }

    public RestAPIAuthorizationFilter() {
        reload = null;//will check property from security-config
    }

    @Override
    protected boolean checkValidCondition(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws ServletException {
        try {
            if (httpRequest.getRequestURI().matches(PLATFORM_API_URI_REGEXP)) {
                return platformAPIsCheck(httpRequest, httpResponse);
            } else {
                return tenantAPIsCheck(httpRequest, httpResponse);
            }
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new ServletException(e);
        }
    }

    protected boolean tenantAPIsCheck(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws ServletException {
        final APISession apiSession = (APISession) httpRequest.getSession().getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        try {
            if (apiSession == null) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            } else if (!checkPermissions(httpRequest)) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            } else {
                return true;
            }
        } catch (InvalidSessionException e) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, "Invalid Bonita engine session.", e);
            }
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            SessionUtil.sessionLogout(httpRequest.getSession());
            return false;
        }
    }

    protected boolean platformAPIsCheck(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) {
        final PlatformSession platformSession = (PlatformSession) httpRequest.getSession().getAttribute(PLATFORM_SESSION_PARAM_KEY);
        if (platformSession != null) {
            return true;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    protected boolean checkPermissions(final HttpServletRequest request) throws ServletException {
        final RestRequestParser restRequestParser = new RestRequestParser(request).invoke();
        return checkPermissions(request, restRequestParser.getApiName(), restRequestParser.getResourceName(), restRequestParser.getResourceQualifiers());
    }

    protected boolean checkPermissions(final HttpServletRequest request, final String apiName, final String resourceName, final APIID resourceQualifiers)
            throws ServletException {
        final String method = request.getMethod();
        final HttpSession session = request.getSession();
        // userPermissions are of type: "organization_visualization"
        @SuppressWarnings("unchecked") final Set<String> userPermissions = (Set<String>) session.getAttribute(SessionUtil.PERMISSIONS_SESSION_PARAM_KEY);
        final APISession apiSession = (APISession) session.getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        final Long tenantId = apiSession.getTenantId();

        final boolean apiAuthorizationsCheckEnabled = isApiAuthorizationsCheckEnabled(tenantId);
        if (!apiAuthorizationsCheckEnabled || apiSession.isTechnicalUser()) {
            return true;
        }
        final String resourceQualifiersAsString = resourceQualifiers != null ? resourceQualifiers.toString() : null;
        final String body = getRequestBody(request);
        final APICallContext apiCallContext = new APICallContext(method, apiName, resourceName, resourceQualifiersAsString, request.getQueryString(), body);
        if (isAlwaysAuthorizedResource(apiCallContext)) {
            return true;
        }
        try {
            return enginePermissionsCheck(apiCallContext, userPermissions, apiSession);
        } catch (BonitaException e) {
            throw new ServletException(e);
        }
    }

    protected boolean isAlwaysAuthorizedResource(final APICallContext apiCallContext) {
        return apiCallContext.isGET()
                && (isSingleResourceCall(apiCallContext, SessionDefinition.TOKEN)
                || isSingleResourceCall(apiCallContext, I18nLocaleDefinition.TOKEN));
    }

    private boolean isSingleResourceCall(final APICallContext apiCallContext, final String authorizedResourceName) {
        return authorizedResourceName.equals(apiCallContext.getResourceName()) && "system".equals(apiCallContext.getApiName());
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

    protected boolean enginePermissionsCheck(final APICallContext apiCallContext, final Set<String> userPermissions, final APISession apiSession)
            throws ServerAPIException, BonitaHomeNotSetException, UnknownAPITypeException, ExecutionException {
        final PermissionAPI permissionAPI = TenantAPIAccessor.getPermissionAPI(apiSession);
        return permissionAPI.isAuthorized(apiCallContext, shouldReload(apiSession), userPermissions);
    }

    private boolean shouldReload(final APISession apiSession) {
        return reload == null ? PropertiesFactory.getSecurityProperties(apiSession.getTenantId()).isAPIAuthorizationsCheckInDebugMode() : reload;
    }
}
