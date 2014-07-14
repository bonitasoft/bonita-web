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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.login.LoginManagerPropertiesFactory;

/**
 * @author Chong Zhao
 * 
 */
public class OAuthConsumerFactory {

    static Map<Long, OAuthConsumer> map = new HashMap<Long, OAuthConsumer>();
    private static LoginManagerPropertiesFactory loginManagerPropertiesFactory = new LoginManagerPropertiesFactory();
    
    private enum OAUTH_CLASS_TYPE {
        LinkedIn
    }

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(OAuthConsumerFactory.class.getName());

    public static OAuthConsumer getOAuthConsumer(final long tenantId, final String redirectURL) throws OAuthConsumerNotFoundException {
        if (!map.containsKey(tenantId)) {
            String providerName = null;
            try {
                providerName = loginManagerPropertiesFactory.getProperties(tenantId).getOAuthServiceProviderName();
                if (providerName == null) {
                    providerName = OAUTH_CLASS_TYPE.LinkedIn.toString();
                    LOGGER.log(Level.INFO, "The OAuth service provider undefined. Using the default implementation : " + providerName);
                }
                map.put(tenantId, getOAuthConsumerClass(providerName, tenantId, redirectURL));
            } catch (final ClassNotFoundException e) {
                final String message = "The OAuth provider class for " + providerName + " doesn't exist!";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
                throw new OAuthConsumerNotFoundException(message, e);
            }
        }
        return map.get(tenantId);
    }

    private static OAuthConsumer getOAuthConsumerClass(final String providerName, final long tenantId, final String redirectURL) throws ClassNotFoundException {
        OAuthConsumer consumerClass;
        switch (OAUTH_CLASS_TYPE.valueOf(providerName)) {
            case LinkedIn:
                consumerClass = new LinkedInConsumer(tenantId, redirectURL);
                break;
            default:
                throw new ClassNotFoundException();
        }
        return consumerClass;
    }
}
