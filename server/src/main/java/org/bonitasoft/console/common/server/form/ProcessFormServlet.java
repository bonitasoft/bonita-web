/**
 * Copyright (C) 2015 BonitaSoft S.A.
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private static final String PROCESS_PATH_SEGMENT = "process";

    private static final String PROCESS_INSTANCE_PATH_SEGMENT = "processInstance";

    private static final String TASK_INSTANCE_PATH_SEGMENT = "taskInstance";

    private static final String TASK_PATH_SEGMENT = "task";

    private static final String USER_ID_PARAM = "user";

    protected PageRenderer pageRenderer = new PageRenderer();

    protected ProcessFormService processFormService = new ProcessFormService();

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        long processDefinitionID = -1L;
        long processInstanceID = -1L;
        long taskInstanceID = -1L;
        String taskName = null;
        final List<String> pathSegments = getPathSegments(request);
        final String user = request.getParameter(USER_ID_PARAM);
        final long userID = convertToLong(USER_ID_PARAM, user);
        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        try {
            String resourcePath = null;
            if (pathSegments.size() > 1) {
                processDefinitionID = getProcessDefinitionID(apiSession, pathSegments);
                processInstanceID = getProcessInstanceID(pathSegments);
                taskInstanceID = getTaskInstanceID(apiSession, pathSegments, userID);
                resourcePath = getResourcePath(pathSegments);
            }
            if (processDefinitionID == -1L && processInstanceID == -1L && taskInstanceID == -1L) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Either process name and version are required or process instance ID (with or without task name) or task instance ID.");
                return;
            }
            processDefinitionID = processFormService.ensureProcessDefinitionID(apiSession, processDefinitionID, processInstanceID, taskInstanceID);
            taskName = processFormService.getTaskName(apiSession, taskInstanceID);
            final FormReference form = processFormService.getForm(apiSession, processDefinitionID, taskName, processInstanceID != -1L);
            if (form.getReference() != null) {
                // for resources path we don't check if the user is allowed
                if (resourcePath == null && !isAuthorized(apiSession, processDefinitionID, processInstanceID, taskInstanceID, userID)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not Authorized");
                } else {
                    displayForm(request, response, apiSession, form, resourcePath);
                }
            } else {
                displayLegacyForm(request, response, apiSession, processDefinitionID, processInstanceID, taskInstanceID, taskName);
            }
        } catch (final Exception e) {
            handleException(processDefinitionID, taskName, processInstanceID != -1L, e);
        }
    }

    protected List<String> getPathSegments(final HttpServletRequest request) {
        final List<String> segments = new ArrayList<String>();;
        final String pathInfo = request.getPathInfo();
        for (final String segment : pathInfo.split("/")) {
            if (!segment.isEmpty()) {
                segments.add(segment);
            }
        }
        return segments;
    }

    protected long getProcessInstanceID(final List<String> pathSegments) {
        long processInstanceID = -1L;
        if (PROCESS_INSTANCE_PATH_SEGMENT.equals(pathSegments.get(0))) {
            final String processInstance = pathSegments.get(1);
            processInstanceID = convertToLong(PROCESS_INSTANCE_PATH_SEGMENT, processInstance);
        }
        return processInstanceID;
    }

    protected long getTaskInstanceID(final APISession apiSession, final List<String> pathSegments, final long userID) throws BonitaException {
        if (TASK_INSTANCE_PATH_SEGMENT.equals(pathSegments.get(0))) {
            final String taskInstance = pathSegments.get(1);
            return convertToLong(TASK_INSTANCE_PATH_SEGMENT, taskInstance);
        } else if (PROCESS_INSTANCE_PATH_SEGMENT.equals(pathSegments.get(0))) {
            final String processInstance = pathSegments.get(1);
            final long processInstanceID = convertToLong(PROCESS_INSTANCE_PATH_SEGMENT, processInstance);
            if (pathSegments.size() > 2 && TASK_PATH_SEGMENT.equals(pathSegments.get(2))) {
                final String taskName = pathSegments.get(3);
                return processFormService.getTaskInstanceID(apiSession, processInstanceID, taskName, userID);
            }
        }
        return -1L;

    }

    protected long getProcessDefinitionID(final APISession apiSession, final List<String> pathSegments) throws BonitaException {
        long processDefinitionID = -1L;
        if (PROCESS_PATH_SEGMENT.equals(pathSegments.get(0))) {
            final String processName = pathSegments.get(1);
            if (pathSegments.size() > 2) {
                final String processVersion = pathSegments.get(2);
                processDefinitionID = processFormService.getProcessDefinitionID(apiSession, processName, processVersion);
            }
        }
        return processDefinitionID;
    }

    protected String getResourcePath(final List<String> pathSegments) {
        int resourcePathStartIndex;
        if (PROCESS_PATH_SEGMENT.equals(pathSegments.get(0))) {
            resourcePathStartIndex = 3;
        } else if (PROCESS_INSTANCE_PATH_SEGMENT.equals(pathSegments.get(0)) && pathSegments.size() > 2 && TASK_PATH_SEGMENT.equals(pathSegments.get(2))) {
            resourcePathStartIndex = 4;
        } else {
            resourcePathStartIndex = 2;
        }
        if (pathSegments.size() > resourcePathStartIndex) {
            final StringBuilder resourcePathBuilder = new StringBuilder();
            for (int i = resourcePathStartIndex; i < pathSegments.size(); i++) {
                if (i > resourcePathStartIndex) {
                    resourcePathBuilder.append(File.separator);
                }
                resourcePathBuilder.append(pathSegments.get(i));
            }
            return resourcePathBuilder.toString();
        }
        return null;
    }

    protected void displayLegacyForm(final HttpServletRequest request, final HttpServletResponse response, final APISession apiSession,
            final long processDefinitionID,
            final long processInstanceID, final long taskInstanceID, final String taskName) throws IOException, BonitaException {
        if (processFormService.hasFormsXML(apiSession, processDefinitionID)) {
            //TODO fallback to legacy form application if there is a form.xml in the business archive
            response.sendRedirect(response.encodeRedirectURL("homepage"));
        } else {
            String message = "Can't find the form for process " + processDefinitionID;
            if (taskName != null) {
                message = message + " and task " + taskName;
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, message);
        }
    }

    protected void displayForm(final HttpServletRequest request, final HttpServletResponse response, final APISession apiSession, final FormReference form,
            final String resourcePath)
            throws InstantiationException, IllegalAccessException, IOException, BonitaException {
        if (!form.isExternal()) {
            try {
                if (resourcePath == null) {
                    pageRenderer.displayCustomPage(request, response, apiSession, form.getReference());
                } else {
                    //TODO render the resource
                }
            } catch (final PageNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Can't find the form with name " + form.getReference());
            }
        } else {
            if (resourcePath == null) {
                displayExternalPage(request, response, form.getReference());
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not Authorized");
            }
        }
    }

    protected long convertToLong(final String parameterName, final String idAsString) {
        if (idAsString != null) {
            try {
                return Long.parseLong(idAsString);
            } catch (final NumberFormatException e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "Wrong value for " + parameterName + " expecting a number (long value)");
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
        final long enforcedUserID;
        if (userID == -1L) {
            enforcedUserID = apiSession.getUserId();
        } else {
            enforcedUserID = userID;
        }
        if (taskInstanceID != -1L) {
            return processFormService.isAllowedToSeeTask(apiSession, processDefinitionID, taskInstanceID, enforcedUserID);
        } else if (processInstanceID != -1L) {
            return processFormService.isAllowedToSeeProcessInstance(apiSession, processDefinitionID, processInstanceID, enforcedUserID);
        } else {
            return processFormService.isAllowedToStartProcess(apiSession, processDefinitionID, enforcedUserID);
        }
    }

    protected void handleException(final long processDefinitionID, final String taskName, final boolean hasProcessInstanceID, final Exception e)
            throws ServletException {
        if (LOGGER.isLoggable(Level.WARNING)) {
            String message = "Error while trying to display a form";
            if (processDefinitionID != -1) {
                message = message + " for process " + processDefinitionID;
            }
            if (taskName != null) {
                message = message + " for task " + taskName;
            } else if (hasProcessInstanceID) {
                message = message + " ( instance overview)";
            }
            LOGGER.log(Level.WARNING, message, e);
        }
        throw new ServletException(e.getMessage());
    }

}
