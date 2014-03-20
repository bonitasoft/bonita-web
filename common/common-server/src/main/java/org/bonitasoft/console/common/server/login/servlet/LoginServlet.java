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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.LoginManagerFactory;
import org.bonitasoft.console.common.server.login.LoginManagerNotFoundException;
import org.bonitasoft.console.common.server.login.datastore.UserCredentials;
import org.bonitasoft.console.common.server.login.localization.RedirectUrlBuilder;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.api.TenantAPIAccessor;
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
     * the URL param for the login page
     */
    protected static final String LOGIN_URL_PARAM_NAME = "loginUrl";

    /**
     * Necessary studio integration (username and password are passed in the URL in development mode)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {

        boolean redirectAfterLogin = true;
        final String redirectAfterLoginStr = request.getParameter(LoginManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        // Do not modify this condition: the redirection should happen unless there is redirect=false in the URL
        if (Boolean.FALSE.toString().equals(redirectAfterLoginStr)) {
            redirectAfterLogin = false;
        }
        final long tenantId = getTenantId(request, response);
        String redirectURL = request.getParameter(LoginManager.REDIRECT_URL);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "redirecting to : " + redirectURL + " (" + dropPassword(request.getQueryString()) + ")");
        }
        if (redirectAfterLogin && (redirectURL == null || redirectURL.isEmpty())) {
            redirectURL = LoginManager.DEFAULT_DIRECT_URL;
        } else {
            if (redirectURL != null) {
                redirectURL = new URLProtector().protectRedirectUrl(redirectURL);    
            }
        }
        try {
            doLogin(request, tenantId);
            final APISession apiSession = (APISession) request.getSession().getAttribute(LoginManager.API_SESSION_PARAM_KEY);
            // if there a redirect=false attribute in the request do nothing (API login), otherwise, redirect (Portal login)
            if (redirectAfterLogin) {
                if (apiSession.isTechnicalUser() || TenantsManagementUtils.hasProfileForUser(apiSession)) {
                    response.sendRedirect(createRedirectUrl(request, redirectURL));
                } else {
                    request.setAttribute(LOGIN_FAIL_MESSAGE, "noProfileForUser");
                    getServletContext().getRequestDispatcher(LoginManager.LOGIN_PAGE).forward(request, response);
                }
            }
        } catch (final LoginFailedException e) {
            // if there a redirect=false attribute in the request do nothing (API login), otherwise, redirect (Portal login)
            if (redirectAfterLogin) {
                try {
                    request.setAttribute(LOGIN_FAIL_MESSAGE, LOGIN_FAIL_MESSAGE);
                    String loginURL = request.getParameter(LOGIN_URL_PARAM_NAME);
                    if (loginURL == null) {
                        loginURL = LoginManager.LOGIN_PAGE;
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
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while trying to log in", e);
            }
            throw new ServletException(e);
        }
    }

    private String createRedirectUrl(final HttpServletRequest request, final String redirectURL) {
        return new RedirectUrlBuilder(redirectURL).build().getUrl();
    }

    protected void doLogin(final HttpServletRequest request, final long tenantId) throws LoginManagerNotFoundException, LoginFailedException {
        final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request);
        getLoginManager(tenantId).login(requestAccessor, createUserCredentials(tenantId, requestAccessor));
    }

    private UserCredentials createUserCredentials(final long tenantId, final HttpServletRequestAccessor requestAccessor) {
        return new UserCredentials(requestAccessor.getUsername(), requestAccessor.getPassword(), tenantId);
    }

    private LoginManager getLoginManager(final long tenantId) throws LoginManagerNotFoundException {
        return LoginManagerFactory.getLoginManager(tenantId);
    }

    protected long getTenantId(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
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
        if (content.contains("password")) {
            tmp = tmp.replaceAll("[&]?password=([^&|#]*)?", "");
        }
        return tmp;
    }

}
