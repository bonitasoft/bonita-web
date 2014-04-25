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
package org.bonitasoft.console.common.server.login.filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.PlatformSession;

/**
 * @author Zhiheng Yang, Chong Zhao
 */
public class RestAPIAuthorizationFilter extends AbstractAuthorizationFilter {

    private static final String PLATFORM_API_URI = "API/platform/";

    protected static final String PLATFORM_SESSION_PARAM_KEY = "platformSession";
    
    @Override
    boolean checkValidCondition(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException {
        try {
            if (httpRequest.getRequestURI().contains(PLATFORM_API_URI)) {
                final PlatformSession platformSession = (PlatformSession) httpRequest.getSession().getAttribute(PLATFORM_SESSION_PARAM_KEY);
                if (platformSession != null) {
                    return true;
                } else {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                final APISession apiSession = (APISession) httpRequest.getSession().getAttribute(LoginManager.API_SESSION_PARAM_KEY);
                if (apiSession != null) {
                    return true;
                } else {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
            return false;
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new ServletException(e);
        }
    }

}
