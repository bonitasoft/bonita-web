/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.login.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerFactory;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerNotFoundException;
import org.bonitasoft.console.common.server.auth.ConsumerNotFoundException;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.utils.LoginUrl;
import org.bonitasoft.console.common.server.login.utils.RedirectUrlBuilder;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.platform.LogoutException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.SessionNotFoundException;

/**
 * Servlet used to logout from the applications
 *
 * @author Zhiheng Yang, Chong Zhao
 *
 */
public class LogoutServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 739607235407639011L;

    /**
     * the URL of the login page
     */
    protected static final String LOGIN_PAGE = "login.jsp";

    /**
     * the URL param for the login page
     */
    protected static final String LOGIN_URL_PARAM_NAME = "loginUrl";

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        logout(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        logout(request, response);
    }

    /**
     * Console logout
     */
    protected void logout(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request);
        final HttpSession session = requestAccessor.getHttpSession();
        final APISession apiSession = requestAccessor.getApiSession();
        long tenantId = -1L;
        if (apiSession != null) {
            tenantId = apiSession.getTenantId();
        }
        try {
            engineLogout(apiSession);
            SessionUtil.sessionLogout(session);

            boolean redirectAfterLogin = true;
            final String redirectAfterLoginStr = request.getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
            // Do not modify this condition: the redirection should happen unless there is redirect=false in the URL
            if (Boolean.FALSE.toString().equals(redirectAfterLoginStr)) {
                redirectAfterLogin = false;
            }
            if (redirectAfterLogin) {
                final String loginPage = getURLToRedirectTo(requestAccessor, tenantId);
                response.sendRedirect(loginPage);
            }
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while performing the logout", e);
            }
            throw new ServletException(e);
        }
    }
    
    // protected for test stubbing
    protected AuthenticationManager getAuthenticationManager(final long tenantId) throws ServletException {
        try {
            return AuthenticationManagerFactory.getAuthenticationManager(tenantId);
        } catch (final AuthenticationManagerNotFoundException e) {
            throw new ServletException(e);
        }
    }

    protected String getURLToRedirectTo(final HttpServletRequestAccessor requestAccessor, final long tenantId)
            throws AuthenticationManagerNotFoundException, UnsupportedEncodingException, ConsumerNotFoundException, ServletException {
        final AuthenticationManager authenticationManager = getAuthenticationManager(tenantId);
        final HttpServletRequest request = requestAccessor.asHttpServletRequest();

        final String redirectURL = createRedirectUrl(requestAccessor, tenantId);

        final String logoutPage = authenticationManager.getLogoutPageURL(requestAccessor, redirectURL);
        String redirectionPage = null;
        if (logoutPage != null) {
            redirectionPage = logoutPage;
        } else {
            final String loginPageURLFromRequest = request.getParameter(LOGIN_URL_PARAM_NAME);
            if (loginPageURLFromRequest != null) {
                redirectionPage = sanitizeLoginPageUrl(loginPageURLFromRequest);
            } else {
                LoginUrl loginPageURL = new LoginUrl(authenticationManager, redirectURL, requestAccessor);
                redirectionPage = loginPageURL.getLocation();
            }
        }
        return redirectionPage;
    }
    
    protected String sanitizeLoginPageUrl(final String loginURL) {
        return new RedirectUrlBuilder(new URLProtector().protectRedirectUrl(loginURL)).build().getUrl();
    }

    protected String createRedirectUrl(final HttpServletRequestAccessor request, final long tenantId) throws ServletException {
        final String redirectUrlFromRequest = request.getRedirectUrl();
        String redirectUrl = redirectUrlFromRequest != null ? redirectUrlFromRequest : getDefaultRedirectUrl(tenantId);
        return new RedirectUrlBuilder(redirectUrl).build().getUrl();
    }

    protected String getDefaultRedirectUrl(final long tenantId) throws ServletException {
        StringBuilder defaultRedirectUrl = new StringBuilder(AuthenticationManager.DEFAULT_DIRECT_URL);
        if (tenantId != getDefaultTenantId()) {
            defaultRedirectUrl.append("?").append(AuthenticationManager.TENANT).append("=").append(tenantId);
        }
        return defaultRedirectUrl.toString();
    }

    protected long getDefaultTenantId() {
        return TenantsManagementUtils.getDefaultTenantId();
    }

    protected void engineLogout(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException,
            SessionNotFoundException, LogoutException {
        if (apiSession != null) {
            final LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
            loginAPI.logout(apiSession);
        }
    }
}
