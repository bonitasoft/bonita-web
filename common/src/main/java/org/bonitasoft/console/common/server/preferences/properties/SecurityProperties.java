/**
 * Copyright (C) 2009 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.preferences.properties;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for security properties access
 *
 * @author Anthony Birembaut
 */
public class SecurityProperties {

    /**
     * Default name of the form definition file
     */
    public static final String SECURITY_DEFAULT_CONFIG_FILE_NAME = "security-config.properties";

    /**
     * property for the robustness of the password
     */
    public static final String PASSWORD_VALIDATOR_CLASSNAME = "security.password.validator";

    /**
     * property for the CSRF protection activation
     */
    public static final String CSRF_PROTECTION = "security.csrf.enabled";
    
    /**
     * property for the CSRF token cookie to have the secure flag (HTTPS only)
     */
    public static final String SECURE_TOKEN_COOKIE = "security.csrf.cookie.secure";

    /**
     * property for the REST API Authorization checks activation
     */
    public static final String API_AUTHORIZATIONS_CHECK = "security.rest.api.authorizations.check.enabled";

    /**
     * Custom page debug mode
     */
    public static final String API_AUTHORIZATIONS_CHECK_DEBUG = "security.rest.api.authorizations.check.debug";

    private static Map<Long, Map<String, Optional<String>>> securityProperties = new ConcurrentHashMap<Long, Map<String, Optional<String>>>();
    
    private final long tenantId;


    public SecurityProperties() {
        tenantId = -1;
    }

    public SecurityProperties(long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return the password validator property
     */
    public String getPasswordValidator() {
        return getProperty(PASSWORD_VALIDATOR_CLASSNAME);
    }

    /**
     * @return the value to allow or not API authorization checks
     */
    public boolean isAPIAuthorizationsCheckEnabled() {
        final String res = getProperty(API_AUTHORIZATIONS_CHECK);
        return res != null && res.equals("true");
    }

    /**
     * @return the value allow permission properties file debug
     */
    public boolean isAPIAuthorizationsCheckInDebugMode() {
        final String debugMode = getProperty(API_AUTHORIZATIONS_CHECK_DEBUG);
        return Boolean.parseBoolean(debugMode);
    }

    /**
     * @return the value to allow or not CSRF protection
     */
    public boolean isCSRFProtectionEnabled() {
        final String res = getProperty(CSRF_PROTECTION);
        return res != null && res.equals("true");
    }
    
    /**
     * @return the value to add or not secure flag to the cookies for CSRF token
     */
    public boolean isCSRFTokenCookieSecure() {
        final String res = getProperty(SECURE_TOKEN_COOKIE);
        return res != null && res.equals("true");
    }

    Properties getProperties() {
        if (tenantId > 0) {
            return getConfigurationFilesManager().getTenantProperties(SECURITY_DEFAULT_CONFIG_FILE_NAME, tenantId);
        }
        return getConfigurationFilesManager().getPlatformProperties(SECURITY_DEFAULT_CONFIG_FILE_NAME);
    }

    protected ConfigurationFilesManager getConfigurationFilesManager() {
        return ConfigurationFilesManager.getInstance();
    }
    
    public String getProperty(String propertyName) {
        Map<String, Optional<String>> tenantSecurityProperties = securityProperties.get(tenantId);
        if (tenantSecurityProperties == null) {
            tenantSecurityProperties = new ConcurrentHashMap<String, Optional<String>>();
            securityProperties.put(tenantId, tenantSecurityProperties);
        }
        Optional<String> propertyValue = tenantSecurityProperties.get(propertyName);
        if (propertyValue == null) {
            propertyValue = Optional.ofNullable(getProperties().getProperty(propertyName));
            tenantSecurityProperties.put(propertyName, propertyValue);
        }
        return propertyValue.orElse(null);
    }

}
