/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login;

import org.bonitasoft.engine.session.APISession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Vincent Elcrin
 * 
 */
public class HttpServletRequestAccessor {

    protected static final String REDIRECT_URL = LoginManager.REDIRECT_URL;

    public static final String USERNAME_PARAM = "username";

    public static final String PASSWORD_PARAM = "password";

    public static final String AUTO_LOGIN_PARAM = "autologin";

    public static final String PROCESS_DEFINITION_ID = "process";

    private final static String OAUTH_VERIFIER = "oauth_verifier";

    private final static String OAUTH_TOKEN = "oauth_token";

    private final static String TENANT_ID = LoginManager.TENANT;

    private final HttpServletRequest httpServletRequest;

    public HttpServletRequestAccessor(final HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getUsername() {
        return httpServletRequest.getParameter(USERNAME_PARAM);
    }

    public String getPassword() {
        return httpServletRequest.getParameter(PASSWORD_PARAM);
    }

    public String getTenantId() {
        return httpServletRequest.getParameter(TENANT_ID);
    }

    public HttpSession getHttpSession() {
        return httpServletRequest.getSession();
    }

    public String getAutoLoginScope() {
        return httpServletRequest.getParameter(AUTO_LOGIN_PARAM);
    }

    public boolean isAutoLoginRequested() {
        return httpServletRequest.getParameter(AUTO_LOGIN_PARAM) != null;
    }

    public String getRedirectUrl() {
        return httpServletRequest.getParameter(REDIRECT_URL);
    }

    public String getOAuthToken() {
        return httpServletRequest.getParameter(OAUTH_TOKEN);
    }

    public String getOAuthVerifier() {
        return httpServletRequest.getParameter(OAUTH_VERIFIER);
    }

    public APISession getApiSession() {
        return (APISession) getHttpSession().getAttribute(LoginManager.API_SESSION_PARAM_KEY);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String[]> getParameterMap() {
        return httpServletRequest.getParameterMap();
    }

    public String getRequestedUrl() {
        return httpServletRequest.getRequestURL().toString();
    }

    public String getRequestedUri() {
        return httpServletRequest.getRequestURI();
    }

    public HttpServletRequest asHttpServletRequest() {
        return httpServletRequest;
    }
}
