/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.auth.impl.jaas;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.login.datastore.Credentials;
import org.bonitasoft.console.common.server.utils.PermissionsBuilder;
import org.bonitasoft.console.common.server.utils.PermissionsBuilderAccessor;
import org.bonitasoft.engine.session.APISession;

/**
 *
 * Login manager imlement by JAAS
 *
 * @author Vincent Elcrin
 *
 */
public class JAASAuthenticationManagerImpl implements AuthenticationManager {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(JAASAuthenticationManagerImpl.class.getName());

    /**
     * JAAS Auth login context
     */
    public static final String JAAS_AUTH_LOGIN_CONTEXT = "BonitaAuth";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLoginPageURL(final HttpServletRequestAccessor request, final String redirectURL) {
        final StringBuffer url = new StringBuffer();
        final String context = request.asHttpServletRequest().getContextPath();
        url.append(context).append(AuthenticationManager.LOGIN_PAGE).append("?");
        if (request.getTenantId() != null) {
            url.append(AuthenticationManager.TENANT).append("=").append(request.getTenantId()).append("&");
        }
        url.append(AuthenticationManager.REDIRECT_URL).append("=").append(redirectURL);
        return url.toString();
    }

    @Override
    public Map<String, Serializable> authenticate(final HttpServletRequestAccessor request, final Credentials credentials) throws AuthenticationFailedException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "#authenticate (this implementation of " + AuthenticationManager.class.getName()
                    + " performs a login on the login context " + JAAS_AUTH_LOGIN_CONTEXT + "[_<tenantId>])");
        }
        final long tenantId = credentials.getTenantId();
        final CallbackHandler handler = createConsoleCallbackHandler(request, String.valueOf(tenantId));
        try {
            final String loginContextName = getLoginContextName(tenantId);
            final LoginContext loginContext = new LoginContext(loginContextName, handler);
            loginContext.login();
            loginContext.logout();
            return Collections.emptyMap();
        } catch (final LoginException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
            throw new AuthenticationFailedException(e.getMessage(), e);
        }
    }

    private ConsoleCallbackHandler createConsoleCallbackHandler(final HttpServletRequestAccessor request, final String tenantId) {
        return new ConsoleCallbackHandler(request.getUsername(), request.getPassword(), tenantId);
    }

    protected PermissionsBuilder createPermissionsBuilder(final APISession session) throws LoginFailedException {
        return PermissionsBuilderAccessor.createPermissionBuilder(session);
    }

    private String getLoginContextName(final long tenantId) {
        String loginContextName;
        if (tenantId >= 0) {
            loginContextName = JAAS_AUTH_LOGIN_CONTEXT + "_" + tenantId;
        } else {
            loginContextName = JAAS_AUTH_LOGIN_CONTEXT;
        }
        return loginContextName;
    }

    @Override
    public String getLogoutPageURL(final HttpServletRequestAccessor request, final String redirectURL) {
        return null;
    }

}
