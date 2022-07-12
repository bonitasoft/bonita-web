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
package org.bonitasoft.console.common.server.auth.impl.oauth;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.servlet.ServletException;

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.ConsumerNotFoundException;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.credentials.Credentials;
import org.scribe.model.Token;

/**
 * @author Ruiheng Fan, Chong Zhao
 *
 */
public class OAuthAuthenticationManagerImpl implements AuthenticationManager {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthAuthenticationManagerImpl.class.getName());

    @Override
    public String getLoginPageURL(final HttpServletRequestAccessor request, final String redirectURL) throws ConsumerNotFoundException, ServletException {
        final OAuthConsumer aConsumer = OAuthConsumerFactory.getOAuthConsumer(redirectURL);
        final Token requestToken = aConsumer.getRequestToken();
        TokenCacheUtil.addRequestToken(requestToken);
        return aConsumer.getAuthorizationUrl(requestToken);
    }

    @Override
    public Map<String, Serializable> authenticate(final HttpServletRequestAccessor request, final Credentials credentials)
            throws AuthenticationFailedException, ServletException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("#authenticate (this implementation of " + AuthenticationManager.class.getName()
                    + " is an example of Oauth authentication working with Linkedin service provider)");
        }
        if (request.getOAuthVerifier() == null) {
            throw new AuthenticationFailedException();
        }
        getOAuthUserId(request);
        return Collections.emptyMap();
    }

    private String getOAuthUserId(final HttpServletRequestAccessor request)
            throws AuthenticationFailedException {
        try {
            final OAuthConsumer aConsumer = OAuthConsumerFactory.getOAuthConsumer(request.getRedirectUrl());
            final String requestTokenStr = request.getOAuthToken();
            final Token requestToken = TokenCacheUtil.getToken(requestTokenStr);
            final Token accessToken = aConsumer.getAccessToken(requestToken, request.getOAuthVerifier());
            return aConsumer.getUserJSONString(accessToken);
        } catch (final ConsumerNotFoundException e) {
            logSevereException(e);
            throw new AuthenticationFailedException(e.getMessage(), e);
        }
    }

    private void logSevereException(final Exception e) {
         if (LOGGER.isErrorEnabled()) {
            LOGGER.error( e.getMessage());
        }

    }

    @Override
    public String getLogoutPageURL(final HttpServletRequestAccessor request, final String redirectURL) throws ConsumerNotFoundException, ServletException {
        return getLoginPageURL(request, redirectURL);
    }

}
