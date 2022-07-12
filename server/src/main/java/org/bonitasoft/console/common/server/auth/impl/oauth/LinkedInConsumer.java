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

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

/**
 * @author Ruiheng Fan, Chong Zhao
 *
 */
public class LinkedInConsumer extends OAuthConsumer {

    private static final long serialVersionUID = 1L;

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkedInConsumer.class.getName());

    private static final String LINKEDIN_PREFIX = "LinkedIn_";

    protected LinkedInConsumer(final String redirctURL) {
        super(LinkedInApi.class, redirctURL);
    }

    private static final String PROTECTED_RESOURCE_URL = "http://api.linkedin.com/v1/people/~:(id,first-name,last-name)?format=json";

    @Override
    public String getUserJSONString(final Token accessToken) throws AuthenticationFailedException {
        try {
            final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
            service.signRequest(accessToken, request);
            final Response response = request.send();
            final JSONObject jsonObj = new JSONObject(response.getBody());

            return LINKEDIN_PREFIX + jsonObj.getString("id");

        } catch (final JSONException e) {
            final String message = "Return values from LinkedIn cannot be resolved.";
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn( e.getMessage());
            }
            throw new AuthenticationFailedException(message, e);
        }

    }
}
