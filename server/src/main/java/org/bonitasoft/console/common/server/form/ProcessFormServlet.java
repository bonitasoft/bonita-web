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
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
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
     * UUId
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

    private static final String USER_Id_PARAM = "user";

    protected PageRenderer pageRenderer = new PageRenderer();

    protected ProcessFormService processFormService = new ProcessFormService();

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        long processDefinitionId = -1L;
        long processInstanceId = -1L;
        long taskInstanceId = -1L;
        String taskName = null;
        final List<String> pathSegments = getPathSegments(request);
        final String user = request.getParameter(USER_Id_PARAM);
        final long userId = convertToLong(USER_Id_PARAM, user);
        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        try {
            String resourcePath = null;
            if (pathSegments.size() > 1) {
                processDefinitionId = getProcessDefinitionId(apiSession, pathSegments);
                processInstanceId = getProcessInstanceId(pathSegments);
                taskInstanceId = getTaskInstanceId(apiSession, pathSegments, userId);
                resourcePath = getResourcePath(pathSegments);
            }
            if (processDefinitionId == -1L && processInstanceId == -1L && taskInstanceId == -1L) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Either process name and version are required or process instance Id (with or without task name) or task instance Id.");
                return;
            }
            processDefinitionId = processFormService.ensureProcessDefinitionId(apiSession, processDefinitionId, processInstanceId, taskInstanceId);
            taskName = processFormService.getTaskName(apiSession, taskInstanceId);
            final FormReference form = processFormService.getForm(apiSession, processDefinitionId, taskName, processInstanceId != -1L);
            if (form.getReference() != null) {
                // for resources we don't check if the user is allowed
                if (resourcePath == null && !isAuthorized(apiSession, processDefinitionId, processInstanceId, taskInstanceId, userId)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not Authorized");
                } else {
                    displayForm(request, response, apiSession, form, resourcePath);
                }
            } else {
                displayLegacyForm(request, response, apiSession, processDefinitionId, processInstanceId, taskInstanceId, taskName);
            }
        } catch (final Exception e) {
            handleException(response, processDefinitionId, taskName, processInstanceId != -1L, e);
        }
    }

    protected List<String> getPathSegments(final HttpServletRequest request) {
        final List<String> segments = new ArrayList<String>();;
        final String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            for (final String segment : pathInfo.split("/")) {
                if (!segment.isEmpty()) {
                    segments.add(segment);
                }
            }
        }
        return segments;
    }

    protected long getProcessInstanceId(final List<String> pathSegments) {
        long processInstanceId = -1L;
        if (PROCESS_INSTANCE_PATH_SEGMENT.equals(pathSegments.get(0))) {
            final String processInstance = pathSegments.get(1);
            processInstanceId = convertToLong(PROCESS_INSTANCE_PATH_SEGMENT, processInstance);
        }
        return processInstanceId;
    }

    protected long getTaskInstanceId(final APISession apiSession, final List<String> pathSegments, final long userId) throws BonitaException {
        if (TASK_INSTANCE_PATH_SEGMENT.equals(pathSegments.get(0))) {
            final String taskInstance = pathSegments.get(1);
            return convertToLong(TASK_INSTANCE_PATH_SEGMENT, taskInstance);
        } else if (PROCESS_INSTANCE_PATH_SEGMENT.equals(pathSegments.get(0))) {
            final String processInstance = pathSegments.get(1);
            final long processInstanceId = convertToLong(PROCESS_INSTANCE_PATH_SEGMENT, processInstance);
            if (pathSegments.size() > 2 && TASK_PATH_SEGMENT.equals(pathSegments.get(2))) {
                final String taskName = pathSegments.get(3);
                return processFormService.getTaskInstanceId(apiSession, processInstanceId, taskName, userId);
            }
        }
        return -1L;

    }

    protected long getProcessDefinitionId(final APISession apiSession, final List<String> pathSegments) throws BonitaException {
        long processDefinitionId = -1L;
        if (PROCESS_PATH_SEGMENT.equals(pathSegments.get(0))) {
            final String processName = pathSegments.get(1);
            if (pathSegments.size() > 2) {
                final String processVersion = pathSegments.get(2);
                processDefinitionId = processFormService.getProcessDefinitionId(apiSession, processName, processVersion);
            }
        }
        return processDefinitionId;
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
            final long processDefinitionId,
            final long processInstanceId, final long taskInstanceId, final String taskName) throws IOException, BonitaException {
        //FIXME not sure we should check if there is a forms XML > this will impact the performances when displaying a legacy for in the portal
        if (processFormService.hasFormsXML(apiSession, processDefinitionId)) {
            //TODO fallback to legacy form application if there is a form.xml in the business archive
            response.sendRedirect(response.encodeRedirectURL("homepage"));
        } else {
            String message = "Cannot find the form for process " + processDefinitionId;
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
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cannot find the form with name " + form.getReference());
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

    protected boolean isAuthorized(final APISession apiSession, final long processDefinitionId, final long processInstanceId, final long taskInstanceId,
            final long userId) throws BonitaException {
        final long enforcedUserId;
        if (userId == -1L) {
            enforcedUserId = apiSession.getUserId();
        } else {
            enforcedUserId = userId;
        }
        if (taskInstanceId != -1L) {
            return processFormService.isAllowedToSeeTask(apiSession, processDefinitionId, taskInstanceId, enforcedUserId);
        } else if (processInstanceId != -1L) {
            return processFormService.isAllowedToSeeProcessInstance(apiSession, processDefinitionId, processInstanceId, enforcedUserId);
        } else {
            return processFormService.isAllowedToStartProcess(apiSession, processDefinitionId, enforcedUserId);
        }
    }

    protected void handleException(final HttpServletResponse response, final long processDefinitionId, final String taskName,
            final boolean hasProcessInstanceId, final Exception e)
            throws ServletException {
        try {
            if (e instanceof ProcessDefinitionNotFoundException) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cannot find the process");
            } else if (e instanceof ArchivedProcessInstanceNotFoundException) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cannot find the process instance");
            } else if (e instanceof ActivityInstanceNotFoundException) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cannot find the task instance");
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    String message = "Error while trying to display a form";
                    if (processDefinitionId != -1) {
                        message = message + " for process " + processDefinitionId;
                    }
                    if (taskName != null) {
                        message = message + " for task " + taskName;
                    } else if (hasProcessInstanceId) {
                        message = message + " ( instance overview)";
                    }
                    LOGGER.log(Level.WARNING, message, e);
                }
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } catch (final IOException ioe) {
            throw new ServletException(ioe);
        }
    }

}
