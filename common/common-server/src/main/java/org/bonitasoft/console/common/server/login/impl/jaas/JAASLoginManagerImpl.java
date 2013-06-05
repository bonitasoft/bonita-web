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
package org.bonitasoft.console.common.server.login.impl.jaas;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.bonitasoft.console.common.client.user.User;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.datastore.Credentials;
import org.bonitasoft.console.common.server.login.datastore.UserLogger;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;

/**
 * 
 * Login manager imlement by JAAS
 * 
 * @author Vincent Elcrin
 * 
 */
public class JAASLoginManagerImpl implements LoginManager {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(JAASLoginManagerImpl.class.getName());

    /**
     * JAAS Auth login context
     */
    public static final String JAAS_AUTH_LOGIN_CONTEXT = "BonitaAuth";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLoginpageURL(final long tenantId, final String redirectURL) {
        final StringBuffer url = new StringBuffer();
        url.append("..").append(LoginManager.LOGIN_PAGE).append("?");
        if (tenantId != -1L) {
            url.append(LoginManager.TENANT).append("=").append(tenantId).append("&");
        }
        url.append(LoginManager.REDIRECT_URL).append("=").append(redirectURL);
        return url.toString();
    }

    @Override
    public void login(final HttpServletRequestAccessor request, final Credentials credentials) throws LoginFailedException {
        long tenantId = credentials.getTenantId();
        final CallbackHandler handler = createConsoleCallbackHandler(request, String.valueOf(tenantId));
        try {
            String loginContextName = getLoginContextName(tenantId);
            LoginContext loginContext = new LoginContext(loginContextName, handler);
            loginContext.login();
            loginContext.logout();
        } catch (final LoginException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
            throw new LoginFailedException(e.getMessage(), e);
        }

        final User user = new User(request.getUsername(), "en");
        final APISession apiSession = getUserLogger().doLogin(credentials);
        user.setUseCredentialTransmission(useCredentialsTransmission(apiSession));
        SessionUtil.sessionLogin(user, apiSession, request.getHttpSession());
    }

    private ConsoleCallbackHandler createConsoleCallbackHandler(final HttpServletRequestAccessor request, String tenantId) {
        return new ConsoleCallbackHandler(request.getUsername(), request.getPassword(), tenantId);
    }

    /**
     * Overridden in SP
     */
    protected UserLogger getUserLogger() {
        return new UserLogger();
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

    private boolean useCredentialsTransmission(final APISession apiSession) {
        return PropertiesFactory.getSecurityProperties(apiSession.getTenantId()).useCredentialsTransmission();
    }

}
