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

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerFactory;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerNotFoundException;
import org.bonitasoft.console.common.server.login.datastore.Credentials;
import org.bonitasoft.console.common.server.login.datastore.UserLogger;
import org.bonitasoft.console.common.server.utils.PermissionsBuilder;
import org.bonitasoft.console.common.server.utils.PermissionsBuilderAccessor;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;

/**
 * This class performs the authentication, the login and initialize the HTTP session
 * 
 * @author Anthony Birembaut
 */
public class LoginManager {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(LoginManager.class.getName());

    /**
     * default locale
     */
    private static final String DEFAULT_LOCALE = "en";

    public void login(final HttpServletRequestAccessor request, final UserLogger userLoger, final Credentials credentials)
            throws AuthenticationFailedException, ServletException, LoginFailedException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "User authentication using the configured AuthenticationManager");
        }
        final Map<String, Serializable> credentialsMap = getAuthenticationManager(credentials.getTenantId()).authenticate(request, credentials);
        APISession apiSession;
        if (credentialsMap == null || credentialsMap.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Engine login using the username and password");
            }
            apiSession = userLoger.doLogin(credentials);
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Engine login using the map of credentials retrieved from the request");
            }
            apiSession = userLoger.doLogin(credentialsMap);
        }
        storeCredentials(request, apiSession);
    }

    protected AuthenticationManager getAuthenticationManager(final long tenantId) throws ServletException {
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
        SessionUtil.sessionLogin(user, session, permissions, request.getHttpSession());
    }

    protected PermissionsBuilder createPermissionsBuilder(final APISession session) throws LoginFailedException {
        return PermissionsBuilderAccessor.createPermissionBuilder(session);
    }
}
