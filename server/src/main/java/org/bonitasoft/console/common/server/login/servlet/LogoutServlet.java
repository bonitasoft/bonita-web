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
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.LoginManagerFactory;
import org.bonitasoft.console.common.server.login.impl.oauth.OAuthLoginManagerImpl;
import org.bonitasoft.console.common.server.login.localization.RedirectUrlBuilder;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.platform.LogoutException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.SessionNotFoundException;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;

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
        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        long tenantId = -1L;
        if (apiSession != null) {
            tenantId = apiSession.getTenantId();
        }
        String loginPage = null;

        try {
            boolean redirectAfterLogin = true;
            final String redirectAfterLoginStr = request.getParameter(LoginManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
            final String localeStr = request.getParameter(UrlOption.LANG);
            // Do not modify this condition: the redirection should happen unless there is redirect=false in the URL
            if (Boolean.FALSE.toString().equals(redirectAfterLoginStr)) {
                redirectAfterLogin = false;
            }
            if (redirectAfterLogin) {
                final LoginManager loginManager = LoginManagerFactory.getLoginManager(tenantId);
                final String encodedRedirectURL = URLEncoder.encode(createRedirectUrl(request), "UTF-8");
                if (loginManager instanceof OAuthLoginManagerImpl) {
                    loginPage = loginManager.getLoginpageURL(request, tenantId, encodedRedirectURL);
                } else {
                    final String loginURL = request.getParameter(LOGIN_URL_PARAM_NAME);
                    if (StringUtils.isNotEmpty(loginURL)) {
                        loginPage = buildLoginPageUrl(loginURL);
                    } else {
                        if (localeStr != null) {
                            // Append tenant parameter in url parameters
                            loginPage = LOGIN_PAGE + "?" + UrlOption.LANG + "=" + localeStr + "&" + LoginManager.REDIRECT_URL + "=" + encodedRedirectURL;
                        } else {
                            loginPage = LOGIN_PAGE + "?" + LoginManager.REDIRECT_URL + "=" + encodedRedirectURL;
                        }
                    }
                }
                engineLogout(apiSession);
                SessionUtil.sessionLogout(session);
                response.sendRedirect(loginPage);
            } else {
                engineLogout(apiSession);
                SessionUtil.sessionLogout(session);
            }
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while performing the logout", e);
            }
            throw new ServletException(e);
        }
    }

    protected String buildLoginPageUrl(final String loginURL) {
        return new RedirectUrlBuilder(new URLProtector().protectRedirectUrl(loginURL)).build().getUrl();
    }

    /**
     * Used in SP
     */
    protected String createRedirectUrl(final HttpServletRequest request) {
        return new RedirectUrlBuilder(getRedirectUrl(request)).build().getUrl();
    }

    private String getRedirectUrl(final HttpServletRequest request) {
        final HttpServletRequestAccessor accessor = new HttpServletRequestAccessor(request);
        final String redirectUrl = accessor.getRedirectUrl();
        return redirectUrl != null ? redirectUrl : getDefaultRedirectUrl();
    }

    /**
     * Overridden in SP
     */
    protected String getDefaultRedirectUrl() {
        return LoginManager.DEFAULT_DIRECT_URL;
    }

    private void engineLogout(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException,
            SessionNotFoundException, LogoutException {
        if (apiSession != null) {
            final LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
            loginAPI.logout(apiSession);
        }
    }
}
