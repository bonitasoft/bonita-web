/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login.filter;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;

import static org.apache.commons.lang3.StringUtils.isBlank;


/**
 * @author Paul AMAR
 *
 */
public class TokenValidatorFilter extends AbstractAuthorizationFilter {

    @Override
    boolean checkValidCondition(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        if (isCsrfProtectionEnabled()) {
            String headerFromRequest = getCSRFToken(httpRequest);
            String apiToken = (String) httpRequest.getSession().getAttribute("api_token");

            if (headerFromRequest == null || !headerFromRequest.equals(apiToken)) {
                if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Token Validation failed, expected: " + apiToken + ", received: " + headerFromRequest);    
                }
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }

    // protected for testing
    protected boolean isCsrfProtectionEnabled() {
        return PropertiesFactory.getSecurityProperties().isCSRFProtectionEnabled();
    }

    private String getCSRFToken(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("X-Bonita-API-Token");
        if (isBlank(token)) {
            token = httpRequest.getParameter("CSRFToken");
        }
        return token;
    }


}
