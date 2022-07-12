/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.auth;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.BooleanUtils;
import org.bonitasoft.console.common.server.preferences.properties.ConfigurationFile;

/**
 * Utility class for Session Manager access (read in a properties file)
 *
 * @author Ruiheng Fan
 */
public class AuthenticationManagerProperties extends ConfigurationFile {

    protected static final String AUTHENTICATION_CONFIG_FILE_NAME = "authenticationManager-config.properties";
    /**
     * Logout Hidden constant
     */
    public static final String LOGOUT_DISABLED = "logout.link.hidden";

    /**
     * Logout Visible constant
     */
    public static final String LOGOUT_ENABLED = "logout.link.visible";

    /**
     * Configuration of authentication manager implementation
     */
    protected static final String AUTHENTICATION_MANAGER = "auth.AuthenticationManager";

    /**
     * Configuration of OAuth service provider name
     */
    protected static final String OAUTH_SERVICE_PROVIDER = "OAuth.serviceProvider";

    /**
     * Configuration of OAuth consumer key
     */
    protected static final String OAUTH_CONSUMER_KEY = "OAuth.consumerKey";

    /**
     * Configuration of OAuth consumer secret
     */
    protected static final String OAUTH_CONSUMER_SECRET = "OAuth.consumerSecret";

    /**
     * Configuration of OAuth callback URL
     */
    protected static final String OAUTH_CALLBACK_URL = "OAuth.callbackURL";

    /**
     * Configuration of CAS Server URL
     */
    protected static final String CAS_SERVER_URL = "Cas.serverUrlPrefix";

    /**
     * Configuration of CAS Bonita Service URL
     */
    protected static final String CAS_BONITA_SERVICE_URL = "Cas.bonitaServiceURL";

    private static Map<String, Optional<String>> authenticationProperties = new ConcurrentHashMap<String, Optional<String>>();
    
    /**
     * properties
     */
    public AuthenticationManagerProperties() {
        super(AUTHENTICATION_CONFIG_FILE_NAME);
    }

    public static AuthenticationManagerProperties getProperties() {
        return new AuthenticationManagerProperties();
    }

    /**
     * @return get login manager implementation
     */
    public String getAuthenticationManagerImpl() {
        return getTenantProperty(AUTHENTICATION_MANAGER);
    }

    /**
     * @return get OAuth service provider name
     */
    public String getOAuthServiceProviderName() {
        return getTenantProperty(OAUTH_SERVICE_PROVIDER);
    }

    /**
     * @return get OAuth consumer key
     */
    public String getOAuthConsumerKey() {
        return getTenantProperty(OAUTH_CONSUMER_KEY);
    }

    /**
     * @return get OAuth consumer secret
     */
    public String getOAuthConsumerSecret() {
        return getTenantProperty(OAUTH_CONSUMER_SECRET);
    }

    /**
     * @return get OAuth callback URL
     */
    public String getOAuthCallbackURL() {
        return getTenantProperty(OAUTH_CALLBACK_URL);
    }

    /**
     * @return get OAuth callback URL
     */
    public String getCasServerURL() {
        return getTenantProperty(CAS_SERVER_URL);
    }

    /**
     * @return get OAuth callback URL
     */
    public String getCasBonitaServiceUrl() {
        return getTenantProperty(CAS_BONITA_SERVICE_URL);
    }

    /**
     * @return if properties are set up to display the logout button
     */
    public boolean isLogoutDisabled() {
        return BooleanUtils.toBoolean(getTenantProperty(LOGOUT_DISABLED));
    }
    
    @Override
    public String getTenantProperty(String propertyName) {
        Optional<String> propertyValue = authenticationProperties.get(propertyName);
        if (propertyValue == null) {
            propertyValue = Optional.ofNullable(super.getTenantProperty(propertyName));
            authenticationProperties.put(propertyName, propertyValue);
        }
        return propertyValue.orElse(null);
    }
}
