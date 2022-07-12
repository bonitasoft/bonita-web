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

import org.bonitasoft.console.common.server.auth.AuthenticationManagerProperties;
import org.bonitasoft.console.common.server.auth.ConsumerNotFoundException;

/**
 * @author Chong Zhao
 */
public class OAuthConsumerFactory {

    static OAuthConsumer oAuthConsumer;

    private enum OAUTH_CLASS_TYPE {
        LinkedIn
    }

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthConsumerFactory.class.getName());

    public static OAuthConsumer getOAuthConsumer(final String redirectURL) throws ConsumerNotFoundException {
        if (oAuthConsumer == null) {
            String providerName = null;
            try {
                providerName = AuthenticationManagerProperties.getProperties().getOAuthServiceProviderName();
                if (providerName == null) {
                    providerName = OAUTH_CLASS_TYPE.LinkedIn.toString();
                    LOGGER.info( "The OAuth service provider undefined. Using the default implementation : " + providerName);
                }
                oAuthConsumer = getOAuthConsumerClass(providerName, redirectURL);
            } catch (final ClassNotFoundException e) {
                final String message = "The OAuth provider class for " + providerName + " doesn't exist!";
                 if (LOGGER.isErrorEnabled()) {
                    LOGGER.error( e.getMessage());
                }
                throw new ConsumerNotFoundException(message, e);
            }
        }
        return oAuthConsumer;
    }

    private static OAuthConsumer getOAuthConsumerClass(final String providerName, final String redirectURL) throws ClassNotFoundException {
        OAuthConsumer consumerClass;
        switch (OAUTH_CLASS_TYPE.valueOf(providerName)) {
            case LinkedIn:
                consumerClass = new LinkedInConsumer(redirectURL);
                break;
            default:
                throw new ClassNotFoundException();
        }
        return consumerClass;
    }
}
