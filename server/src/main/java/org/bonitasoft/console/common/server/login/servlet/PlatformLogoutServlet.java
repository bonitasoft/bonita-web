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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.PlatformSession;

/**
 * Servlet used to logout from the applications
 *
 * @author Ruiheng Fan
 *
 */
public class PlatformLogoutServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 739607235407639011L;

    /**
     * the URL of the login page
     */
    protected String PLATFORM_LOGIN_PAGE = "platformLogin.jsp";

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(PlatformLogoutServlet.class.getName());

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
    protected void logout(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final HttpSession session = request.getSession();

        final PlatformSession platformSession = (PlatformSession) session.getAttribute(PlatformLoginServlet.PLATFORMSESSION);
        if (platformSession != null) {
            try {
                final PlatformLoginAPI platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
                platformLoginAPI.logout(platformSession);
            } catch (final BonitaException e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Error while performing the logout", e);
                }
            }
        }
        session.removeAttribute(PlatformLoginServlet.PLATFORMSESSION);
        session.invalidate();

        final String redirectAfterLoginStr = request.getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        // Do not modify this condition: the redirection should happen unless there is redirect=false in the URL
        if (!Boolean.FALSE.toString().equals(redirectAfterLoginStr)) {
            response.sendRedirect(PLATFORM_LOGIN_PAGE);
        }
    }
}
