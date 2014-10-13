/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.console.common.server.login.impl.oauth;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.datastore.Credentials;
import org.bonitasoft.console.common.server.login.datastore.UserLogger;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;
import org.scribe.model.Token;

/**
 * @author Ruiheng Fan, Chong Zhao
 * 
 */
public class OAuthLoginManagerImpl implements LoginManager {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(OAuthLoginManagerImpl.class.getName());

    @Override
    public String getLoginpageURL(final HttpServletRequest request, final long tenantId, final String redirectURL) throws OAuthConsumerNotFoundException {
        final OAuthConsumer aConsumer = OAuthConsumerFactory.getOAuthConsumer(tenantId, redirectURL);
        final Token requestToken = aConsumer.getRequestToken();
        TokenCacheUtil.addRequestToken(requestToken);
        return aConsumer.getAuthorizationUrl(requestToken);
    }

    @Override
    public void login(final HttpServletRequestAccessor request, final Credentials credentials) throws LoginFailedException {
        if (request.getOAuthVerifier() == null) {
            throw new LoginFailedException();
        }
        final long tenantId = credentials.getTenantId();

        String local = DEFAULT_LOCALE;
        if (request.getParameterMap().get("_l") != null
                && request.getParameterMap().get("_l").length >= 0) {
            local = request.getParameterMap().get("_l")[0];
        }
        final User user = new User(getOAuthUserId(request, tenantId), local);
        final APISession apiSession = getUserLogger().doLogin(credentials);
        SessionUtil.sessionLogin(user, apiSession, request.getHttpSession());
    }

    /**
     * Overridden in SP
     */
    protected UserLogger getUserLogger() {
        return new UserLogger();
    }

    private String getOAuthUserId(final HttpServletRequestAccessor request, final long tenantId)
            throws LoginFailedException {
        try {
            final OAuthConsumer aConsumer = OAuthConsumerFactory.getOAuthConsumer(tenantId, request.getRedirectUrl());
            final String requestTokenStr = request.getOAuthToken();
            final Token requestToken = TokenCacheUtil.getToken(requestTokenStr);
            final Token accessToken = aConsumer.getAccessToken(requestToken, request.getOAuthVerifier());
            final String userId = aConsumer.getUserJSONString(accessToken);
            return userId;
        } catch (final BonitaException e) {
            logSevereException(e);
            throw new LoginFailedException(e.getMessage(), e);
        } catch (final OAuthConsumerNotFoundException e) {
            logSevereException(e);
            throw new LoginFailedException(e.getMessage(), e);
        }
    }

    private void logSevereException(final Exception e) {
        if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }

}
