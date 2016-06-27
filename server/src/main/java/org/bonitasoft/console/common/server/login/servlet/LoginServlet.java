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
package org.bonitasoft.console.common.server.login.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.CharEncoding;
import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerNotFoundException;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.credentials.StandardCredentials;
import org.bonitasoft.console.common.server.login.credentials.UserLogger;
import org.bonitasoft.console.common.server.login.localization.RedirectUrlBuilder;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.TenantStatusException;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Anthony Birembaut, Ruiheng Fan, Chong Zhao, Haojie Yuan
 *
 */
public class LoginServlet extends HttpServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5326931127638029215L;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    /**
     * login fail message
     */
    protected static final String LOGIN_FAIL_MESSAGE = "loginFailMessage";


        /**
     * the Tenant In maintenance message key
     */
    protected static final String TENANT_IN_MAINTENACE_MESSAGE = "tenantInMaintenanceMessage";

    /**
     * the URL param for the login page
     */
    protected static final String LOGIN_URL_PARAM_NAME = "loginUrl";

    /**
     * Necessary studio integration (username and password are passed in the URL in development mode)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "query string : " + dropPassword(req.getQueryString()));
        }
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {

        // force post request body to UTF-8
        try {
            request.setCharacterEncoding(CharEncoding.UTF_8);
        } catch (final UnsupportedEncodingException e) {
            // should never appear
            throw new ServletException(e);
        }

        final boolean redirectAfterLogin = hasRedirection(request);
        final String redirectURL = getRedirectUrl(request, redirectAfterLogin);
        try {
            doLogin(request, response);
            final APISession apiSession = (APISession) request.getSession().getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
            // if there a redirect=false attribute in the request do nothing (API login), otherwise, redirect (Portal login)
            if (redirectAfterLogin) {
                if (apiSession.isTechnicalUser() || TenantsManagementUtils.hasProfileForUser(apiSession)) {
                    response.sendRedirect(createRedirectUrl(request, redirectURL));
                } else {
                    request.setAttribute(LOGIN_FAIL_MESSAGE, "noProfileForUser");
                    getServletContext().getRequestDispatcher(AuthenticationManager.LOGIN_PAGE).forward(request, response);
                }
            }
        } catch (final AuthenticationManagerNotFoundException e) {
            final String message = "Can't get login manager";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new ServletException(e);
        } catch (final LoginFailedException e) {
            handleLoginFailedException(request, response, redirectAfterLogin, e);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Error while trying to log in", e);
            throw new ServletException(e);
        }
    }

    private void handleLoginFailedException(final HttpServletRequest request, final HttpServletResponse response, final boolean redirectAfterLogin,
            final LoginFailedException e) throws ServletException {
        // if there a redirect=false attribute in the request do nothing (API login), otherwise, redirect (Portal login)
        if (redirectAfterLogin) {
            try {
                request.setAttribute(LOGIN_FAIL_MESSAGE, LOGIN_FAIL_MESSAGE);
                String loginURL = request.getParameter(LOGIN_URL_PARAM_NAME);
                if (loginURL == null) {
                    loginURL = AuthenticationManager.LOGIN_PAGE;
                    getServletContext().getRequestDispatcher(loginURL).forward(request, response);
                } else {
                    getServletContext().getRequestDispatcher(createRedirectUrl(request, loginURL)).forward(request, response);
                }
            } catch (final Exception e1) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, e1.getMessage());
                }
                throw new ServletException(e1);
            }
        } else {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
            throw new ServletException(e);
        }
    }

    private String getRedirectUrl(final HttpServletRequest request, final boolean redirectAfterLogin) {
        String redirectURL = request.getParameter(AuthenticationManager.REDIRECT_URL);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "redirecting to : " + redirectURL);
        }
        if (redirectAfterLogin && (redirectURL == null || redirectURL.isEmpty())) {
            redirectURL = AuthenticationManager.DEFAULT_DIRECT_URL;
        } else {
            if (redirectURL != null) {
                redirectURL = new URLProtector().protectRedirectUrl(redirectURL);
            }
        }
        return redirectURL;
    }

    private boolean hasRedirection(final HttpServletRequest request) {
        boolean redirectAfterLogin = true;
        final String redirectAfterLoginStr = request.getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        // Do not modify this condition: the redirection should happen unless there is redirect=false in the URL
        if (redirectAfterLoginStr != null && Boolean.FALSE.toString().equals(redirectAfterLoginStr)) {
            redirectAfterLogin = false;
        }
        return redirectAfterLogin;
    }

    private String createRedirectUrl(final HttpServletRequest request, final String redirectURL) {
        return new RedirectUrlBuilder(redirectURL).build().getUrl();
    }

    protected void doLogin(final HttpServletRequest request, HttpServletResponse response) throws AuthenticationManagerNotFoundException, LoginFailedException, ServletException {
        try {
            final long tenantId = getTenantId(request);
            final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request);
            final StandardCredentials userCredentials = createUserCredentials(tenantId, requestAccessor);
            final LoginManager loginManager = getLoginManager();
            loginManager.login(requestAccessor, response, createUserLogger(), userCredentials);
        } catch (final AuthenticationFailedException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Authentication failed : " + e.getMessage(), e);
            }
            throw new LoginFailedException(e);
        } catch (final TenantStatusException e) {
            request.setAttribute(TENANT_IN_MAINTENACE_MESSAGE, TENANT_IN_MAINTENACE_MESSAGE);
            throw new LoginFailedException(TENANT_IN_MAINTENACE_MESSAGE, e);
        }
    }

    protected LoginManager getLoginManager() {
        return new LoginManager();
    }

    protected StandardCredentials createUserCredentials(final long tenantId, final HttpServletRequestAccessor requestAccessor) {
        return new StandardCredentials(requestAccessor.getUsername(), requestAccessor.getPassword(), tenantId);
    }

    protected UserLogger createUserLogger() {
        return new UserLogger();
    }

    /*
     * protected for testing
     */
    protected long getTenantId(final HttpServletRequest request) throws ServletException {
        long tenantId = -1L;
        try {
            final APISession session = TenantAPIAccessor.getLoginAPI().login(TenantsManagementUtils.getTechnicalUserUsername(),
                    TenantsManagementUtils.getTechnicalUserPassword());
            tenantId = session.getTenantId();
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to get the default tenant.", e);
            }
        }
        return tenantId;
    }

    public String dropPassword(final String content) {
        String tmp = content;
        if (content != null && content.contains("password")) {
            tmp = tmp.replaceAll("[&]?password=([^&|#]*)?", "");
        }
        return tmp;
    }

}
