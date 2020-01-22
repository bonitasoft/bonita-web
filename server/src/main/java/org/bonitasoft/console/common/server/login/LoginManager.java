/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerFactory;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerNotFoundException;
import org.bonitasoft.console.common.server.login.credentials.Credentials;
import org.bonitasoft.console.common.server.login.credentials.StandardCredentials;
import org.bonitasoft.console.common.server.login.credentials.UserLogger;
import org.bonitasoft.console.common.server.login.credentials.UserLoggerFactory;
import org.bonitasoft.console.common.server.login.filter.TokenGenerator;
import org.bonitasoft.console.common.server.utils.PermissionsBuilder;
import org.bonitasoft.console.common.server.utils.PermissionsBuilderAccessor;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.exception.TenantStatusException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;

/**
 * This class performs the authentication, the login and initializes the HTTP session
 *
 * @author Anthony Birembaut
 */
public class LoginManager {

    private static final Logger LOGGER = Logger.getLogger(LoginManager.class.getName());
    private static final String DEFAULT_LOCALE = "en";
    public static final String TENANT_COOKIE_NAME = "bonita.tenant";
    
    protected TokenGenerator tokenGenerator = new TokenGenerator();
    protected PortalCookies portalCookies = new PortalCookies();
    
    
    /**
     * Performs the login using the appropriate AuthenticationManager implementation for authentication, logging in on the engine, initializing the HTTP session, and creating the cookies (tanat and CSRF)
     */
    public void login(final HttpServletRequest request, final HttpServletResponse response)
        throws AuthenticationManagerNotFoundException, LoginFailedException, TenantStatusException, AuthenticationFailedException, ServletException {
        final long tenantId = getTenantId(request);
        final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request);
        final StandardCredentials userCredentials = createUserCredentials(tenantId, requestAccessor);
        loginInternal(requestAccessor, response, getUserLogger(), userCredentials);
    }
    
    /**
     * @deprecated use {@link LoginManager#login(HttpServletRequest, HttpServletResponse)} instead. It also deals with the cookies creation (CSRF and tenant)
     */
    @Deprecated
    public void login(HttpServletRequestAccessor request, HttpServletResponse response, UserLogger userLoger, Credentials credentials)
            throws AuthenticationFailedException, ServletException, LoginFailedException {
        loginInternal(request, response, userLoger, credentials);
    }
    
    public void loginInternal(HttpServletRequestAccessor request, HttpServletResponse response, UserLogger userLoger, Credentials credentials)
            throws AuthenticationFailedException, ServletException, LoginFailedException {
        AuthenticationManager authenticationManager = getAuthenticationManager(credentials.getTenantId());
        Map<String, Serializable> credentialsMap = authenticationManager.authenticate(request, credentials);
        if(credentialsMap.isEmpty() 
                && (credentials.getName() == null || credentials.getName().isEmpty())) {
            LOGGER.log(Level.FINE, "There are no credentials in the request");
            throw new AuthenticationFailedException("No credentials in request");
        }
        APISession apiSession = loginWithAppropriateCredentials(userLoger, credentials, credentialsMap);
        portalCookies.addTenantCookieToResponse(response, apiSession.getTenantId());
        storeCredentials(request, apiSession);
        portalCookies.addCSRFTokenCookieToResponse(request.asHttpServletRequest(), response, tokenGenerator.createOrLoadToken(request.getHttpSession()));
    }
    
    protected long getTenantId(final HttpServletRequest request) throws ServletException {
        final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request);
        TenantIdAccessor tenantIdAccessor = TenantIdAccessorFactory.getTenantIdAccessor(requestAccessor);
        return tenantIdAccessor.ensureTenantId();
    }
    
    protected StandardCredentials createUserCredentials(final long tenantId, final HttpServletRequestAccessor requestAccessor) {
        return new StandardCredentials(requestAccessor.getUsername(), requestAccessor.getPassword(), tenantId);
    }

    protected UserLogger getUserLogger() {
        return UserLoggerFactory.getUserLogger();
    }

    public AuthenticationManager getAuthenticationManager(final long tenantId) throws ServletException {
        try {
            final AuthenticationManager authenticationManager = AuthenticationManagerFactory.getAuthenticationManager(tenantId);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Using the AuthenticationManager implementation: " + authenticationManager.getClass().getName());
            }
            return authenticationManager;
        } catch (final AuthenticationManagerNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private APISession loginWithAppropriateCredentials(UserLogger userLoger, Credentials credentials, Map<String, Serializable> credentialsMap)
            throws LoginFailedException {
        if (MapUtils.isEmpty(credentialsMap)) {
            LOGGER.log(Level.FINE, "Engine login using the username and password");
            return userLoger.doLogin(credentials);
        } else {
            LOGGER.log(Level.FINE, "Engine login using the map of credentials retrieved from the request");
            return userLoger.doLogin(credentialsMap);
        }
    }
    protected void storeCredentials(final HttpServletRequestAccessor request, final APISession session) throws LoginFailedException {
        String local = DEFAULT_LOCALE;
        if (request.getParameterMap().get("_l") != null
                && request.getParameterMap().get("_l").length >= 0) {
            local = request.getParameterMap().get("_l")[0];
        }
        final User user = new User(request.getUsername(), local);
        final PermissionsBuilder permissionsBuilder = createPermissionsBuilder(session);
        final Set<String> permissions = permissionsBuilder.getPermissions();
        initSession(request, session, user, permissions);
    }

    protected void initSession(final HttpServletRequestAccessor request, final APISession session, final User user, final Set<String> permissions) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "HTTP session initialization");
        }
        //invalidating session allows to fix session fixation security issue
        request.getHttpSession().invalidate();
        //calling request.getSession() creates a new Session if no any valid exists
        SessionUtil.sessionLogin(user, session, permissions, request.getHttpSession());
    }

    protected PermissionsBuilder createPermissionsBuilder(final APISession session) throws LoginFailedException {
        return PermissionsBuilderAccessor.createPermissionBuilder(session);
    }
}
