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

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerProperties;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * @author Ruiheng.Fan
 */
public abstract class OAuthConsumer implements Serializable {

    protected String redirectUrl;

    protected long tenantId;

    protected Class<? extends Api> viewType;

    protected OAuthService service;

    private static final long serialVersionUID = 1L;

    protected OAuthConsumer(final Class<? extends Api> viewType, final long tenantId, final String redirctURL) {
        this.viewType = viewType;
        redirectUrl = redirctURL;
        this.tenantId = tenantId;
        generateOAuthService();
    }

    protected void generateOAuthService() {
        final AuthenticationManagerProperties properties = AuthenticationManagerProperties.getProperties(tenantId);
        final String consumerKey = properties.getOAuthConsumerKey();
        final String consumerSecret = properties.getOAuthConsumerSecret();
        final String callbackURL = properties.getOAuthCallbackURL();

        service = new ServiceBuilder()
                .provider(viewType)
                .apiKey(consumerKey)
                .apiSecret(consumerSecret)
                .callback(callbackURL + "?" + AuthenticationManager.TENANT + "=" + tenantId + "&" + AuthenticationManager.REDIRECT_URL + "=" + redirectUrl)
                .build();
    }

    public Token getRequestToken() {
        return service.getRequestToken();
    }

    public String getAuthorizationUrl(final Token requestToken) {
        return service.getAuthorizationUrl(requestToken);
    }

    public Token getAccessToken(final Token requestToken, final String verifier) {
        final Token accessToken = service.getAccessToken(requestToken, new Verifier(verifier));
        return accessToken;
    }

    public abstract String getUserJSONString(final Token accessToken) throws AuthenticationFailedException;

}
