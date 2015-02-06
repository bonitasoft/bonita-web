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

    private static final String TASK_INSTANCE_ID_PARAM = "taskInstance";

    private static final String PROCESS_INSTANCE_ID_PARAM = "processInstance";

    private static final String USER_ID_PARAM = "user";

    protected PageRenderer pageRenderer = new PageRenderer();

    protected ProcessFormService processFormService = new ProcessFormService();

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String processName = request.getParameter(PROCESS_NAME_PARAM);
        final String processVersion = request.getParameter(PROCESS_VERSION_PARAM);
        String taskName = request.getParameter(TASK_NAME_PARAM);
        final String taskInstance = request.getParameter(TASK_INSTANCE_ID_PARAM);
        final String processInstance = request.getParameter(PROCESS_INSTANCE_ID_PARAM);
        final String user = request.getParameter(USER_ID_PARAM);
        final long userID = convertToLong(USER_ID_PARAM, user);
        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        try {
            final long processDefinitionID = processFormService.getProcessDefinitionID(apiSession, processName, processVersion);
            final long processInstanceID = convertToLong(PROCESS_INSTANCE_ID_PARAM, processInstance);
            final long taskInstanceID = processFormService.getTaskInstanceID(apiSession, convertToLong(PROCESS_INSTANCE_ID_PARAM, processInstance), taskName,
                    convertToLong(TASK_NAME_PARAM, taskInstance), userID);
            taskName = processFormService.ensureTaskName(apiSession, taskName, taskInstanceID);
            if (processDefinitionID == -1 && processInstanceID == -1 && taskInstanceID == -1) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Either process and version parameters are required or processInstance (with or without task) or taskInstance.");
                return;
            }
            if (!isAuthorized(apiSession, processDefinitionID, processInstanceID, taskInstanceID, userID)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not Authorized");
                return;
            }
            final FormReference form = processFormService.getForm(apiSession, processDefinitionID, taskName, processInstanceID != -1);
            if (form.getReference() != null) {
                displayForm(request, response, apiSession, form);
            } else {
                displayLegacyForm(request, response, processDefinitionID, processInstanceID, taskInstanceID);
            }
        } catch (final Exception e) {
            handleException(processName, processVersion, taskName, e);
        }
    }

    protected void displayLegacyForm(final HttpServletRequest request, final HttpServletResponse response, final long processDefinitionID,
            final long processInstanceID, final long taskInstanceID) {
        //TODO fallback to legacy form application if there is a form.xml in the business archive
    }

    protected void displayForm(final HttpServletRequest request, final HttpServletResponse response, final APISession apiSession, final FormReference form)
            throws InstantiationException, IllegalAccessException, IOException, BonitaException {
        if (!form.isExternal()) {
            try {
                pageRenderer.displayCustomPage(request, response, apiSession, form.getReference());
            } catch (final PageNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Can't find the form with name " + form.getReference());
            }
        } else {
            displayExternalPage(request, response, form.getReference());
        }
    }

    protected long convertToLong(final String parameterName, final String idAsString) {
        if (idAsString != null) {
            try {
                return Long.parseLong(idAsString);
            } catch (final NumberFormatException e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "Wrong parameter for " + parameterName + " expecting a number (long value)");
                }
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    protected void displayExternalPage(final HttpServletRequest request, final HttpServletResponse response, final String url) throws IOException {
        final UrlBuilder urlBuilder = new UrlBuilder(url);
        urlBuilder.appendParameters(request.getParameterMap());
        response.sendRedirect(response.encodeRedirectURL(urlBuilder.build()));
    }

    protected boolean isAuthorized(final APISession apiSession, final long processDefinitionID, final long processInstanceID, final long taskInstanceID,
            final long userID) throws BonitaException {
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
