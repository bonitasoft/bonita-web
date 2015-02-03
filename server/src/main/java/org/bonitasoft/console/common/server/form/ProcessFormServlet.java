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
package org.bonitasoft.console.common.server.form;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.localization.UrlBuilder;
import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.engine.session.APISession;

/**
 * Servlet allowing to display a form for a process or a task
 * The form can be a custom page or an external page
 *
 * @author Anthony Birembaut
 */
public class ProcessFormServlet extends HttpServlet {

    /**
     * UUID
     */
    private static final long serialVersionUID = -6397856355139281873L;

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(ProcessFormServlet.class.getName());

    private static final String PROCESS_NAME_PARAM = "process";

    private static final String PROCESS_VERSION_PARAM = "version";

    private static final String TASK_NAME_PARAM = "task";

    private static final String INSTANCE_ID_PARAM = "id";

    protected PageRenderer pageRenderer = new PageRenderer();

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String processName = request.getParameter(PROCESS_NAME_PARAM);
        final String processVersion = request.getParameter(PROCESS_VERSION_PARAM);
        final String taskName = request.getParameter(TASK_NAME_PARAM);
        final String instanceID = request.getParameter(INSTANCE_ID_PARAM);
        if (processName == null || processVersion == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing process name and/or version");
            return;
        }
        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        try {
            final String formName = getForm(apiSession, processName, processVersion, taskName, instanceID != null);
            if (!isAuthorized(apiSession, processName, processVersion, taskName, instanceID)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not Authorized");
                return;
            }
            try {
                pageRenderer.displayCustomPage(request, response, apiSession, formName);
            } catch (final PageNotFoundException e) {
                displayExternalPage(request, response, formName);
            }
        } catch (final Exception e) {
            handleException(processName, processVersion, taskName, e);
        }
    }

    protected String getForm(final APISession apiSession, final String processName, final String processVersion, final String taskName, final boolean isInstance)
            throws BonitaException {
        final ProcessFormService processFormService = new ProcessFormService();
        final String formName = processFormService.getForm(apiSession, processName, processVersion, taskName, isInstance);
        return formName;
    }

    @SuppressWarnings("unchecked")
    protected void displayExternalPage(final HttpServletRequest request, final HttpServletResponse response, final String url) throws IOException {
        final UrlBuilder urlBuilder = new UrlBuilder(url);
        urlBuilder.appendParameters(request.getParameterMap());
        response.sendRedirect(response.encodeRedirectURL(urlBuilder.build()));
    }

    protected boolean isAuthorized(final APISession apiSession, final String processName, final String processVersion, final String taskName,
            final String instanceID) throws BonitaException {
        //TODO check if the user is allowed to see the process or task form
        return true;
    }

    protected void handleException(final String processName, final String processVersion, final String taskName, final Exception e) throws ServletException {
        if (LOGGER.isLoggable(Level.WARNING)) {
            String message = "Error while trying to retrieve the form for process " + processName + " " + processVersion;
            if (taskName != null) {
                message = message + " and task " + taskName;
            }
            LOGGER.log(Level.WARNING, message, e);
        }
        throw new ServletException(e.getMessage());
    }

}
