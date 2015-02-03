/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.page;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;

public class CustomPageServlet extends HttpServlet {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(CustomPageServlet.class.getName());

    /**
     * uuid
     */
    private static final long serialVersionUID = -5410859017103815654L;

    public static final String PAGE_NAME_PARAM = "page";

    public static final String APP_ID_PARAM = "applicationId";

    protected PageRenderer pageRenderer = new PageRenderer();

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String pageName = request.getParameter(PAGE_NAME_PARAM);
        final String appID = request.getParameter(APP_ID_PARAM);
        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        try {
            if (!isAuthorized(apiSession, appID, pageName)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not Authorized");
                return;
            }
            pageRenderer.displayCustomPage(request, response, apiSession, pageName);
        } catch (final Exception e) {
            handleException(pageName, e);
        }
    }

    protected boolean isAuthorized(final APISession apiSession, final String appID, final String pageName) throws BonitaException {
        return getCustomPageAuthorizationsHelper(apiSession).isPageAuthorized(appID, pageName);
    }

    protected CustomPageAuthorizationsHelper getCustomPageAuthorizationsHelper(final APISession apiSession) throws BonitaHomeNotSetException,
            ServerAPIException, UnknownAPITypeException {
        return new CustomPageAuthorizationsHelper(new GetUserRightsHelper(apiSession),
                TenantAPIAccessor.getLivingApplicationAPI(apiSession), TenantAPIAccessor.getCustomPageAPI(apiSession));
    }

    protected void handleException(final String pageName, final Exception e) throws ServletException {
        if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Error while trying to render the custom page " + pageName, e);
        }
        throw new ServletException(e.getMessage());
    }

}
