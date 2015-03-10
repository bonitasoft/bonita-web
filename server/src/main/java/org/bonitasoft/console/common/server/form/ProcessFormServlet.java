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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.bonitasoft.console.common.server.login.localization.UrlValue;
import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.FormMappingNotFoundException;
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

        long processDefinitionId = -1L;
        long processInstanceId = -1L;
        long taskInstanceId = -1L;
        String taskName = null;
        final List<String> pathSegments = getPathSegments(request);
        final String user = request.getParameter(USER_ID_PARAM);
        final long userId = convertToLong(USER_ID_PARAM, user);
        final HttpSession session = request.getSession();
        final APISession apiSession = (APISession) session.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        try {
            String resourcePath = null;
            if (pathSegments.size() > 1) {
                taskInstanceId = getTaskInstanceId(apiSession, pathSegments, userId);
                if (taskInstanceId != -1 && !TASK_INSTANCE_PATH_SEGMENT.equals(pathSegments.get(0))) {
                    redirectToTaskPage(request, response, taskInstanceId);
                }
                processInstanceId = getProcessInstanceId(pathSegments);
                processDefinitionId = getProcessDefinitionId(apiSession, pathSegments);
                resourcePath = getResourcePath(pathSegments);
            }
            if (processDefinitionId == -1L && processInstanceId == -1L && taskInstanceId == -1L) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Either process name and version are required or process instance Id (with or without task name) or task instance Id.");
                return;
            }
            processDefinitionId = processFormService.ensureProcessDefinitionId(apiSession, processDefinitionId, processInstanceId, taskInstanceId);
            taskName = processFormService.getTaskName(apiSession, taskInstanceId);
            displayFormOrResource(request, response, apiSession, processDefinitionId, processInstanceId, taskInstanceId, taskName, userId, resourcePath);
        } catch (final Exception e) {
            handleException(response, processDefinitionId, taskName, processInstanceId != -1L, e);
        }
    }

    protected void displayFormOrResource(final HttpServletRequest request, final HttpServletResponse response, final APISession apiSession,
            final long processDefinitionId, final long processInstanceId, final long taskInstanceId, final String taskName, final long userId, final String resourcePath)
            throws BonitaException, IOException, InstantiationException, IllegalAccessException {
        try {
            final FormReference form = processFormService.getForm(apiSession, processDefinitionId, taskName, processInstanceId != -1L);
            if (ProcessFormService.LEGACY_FORMS_NAME.equals(form.getForm())) {
                displayLegacyForm(request, response, apiSession, processDefinitionId, processInstanceId, taskInstanceId, taskName, userId);
            } else if (form.getForm() != null) {
                // for resources we don't check if the user is allowed
                if (resourcePath == null && !isAuthorized(apiSession, processDefinitionId, processInstanceId, taskInstanceId, userId)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not Authorized");
                } else {
                    displayForm(request, response, apiSession, processDefinitionId, processInstanceId, taskInstanceId, form, resourcePath);
                }
            } else {
                //TODO restore this once the studio export LEGACY mapping
                //response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cannot find the form mapping");
                //meanwhile fallback to legacy forms
                displayLegacyForm(request, response, apiSession, processDefinitionId, processInstanceId, taskInstanceId, taskName, userId);
            }
        } catch (final FormMappingNotFoundException e) {
            displayLegacyForm(request, response, apiSession, processDefinitionId, processInstanceId, taskInstanceId, taskName, userId);
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
            final long processDefinitionId, final long processInstanceId, final long taskInstanceId, final String taskName, final long userId)
            throws IOException, BonitaException {
        final String legacyFormURL = buildLegacyFormURL(request, apiSession, processDefinitionId, processInstanceId, taskInstanceId, taskName, userId);
        response.sendRedirect(response.encodeRedirectURL(legacyFormURL));
    }

    protected String buildLegacyFormURL(final HttpServletRequest request, final APISession apiSession, final long processDefinitionId,
            final long processInstanceId,
            final long taskInstanceId, final String taskName, final long userId) throws BonitaException, UnsupportedEncodingException {
        final StringBuilder legacyFormURL = new StringBuilder(request.getContextPath());
        legacyFormURL.append("/portal/homepage?ui=form&locale=")
                .append(pageRenderer.getCurrentLocale(request).toString())
                .append("&theme=")
                .append(processDefinitionId)
                .append("#mode=form&form=")
                .append(URLEncoder.encode(processFormService.getProcessDefinitionUUID(apiSession, processDefinitionId), "UTF-8"));
        if (taskInstanceId != -1L) {
            legacyFormURL.append(ProcessFormService.UUID_SEPERATOR)
                    .append(URLEncoder.encode(taskName + "$", "UTF-8"))
                    .append("entry&task=")
                    .append(taskInstanceId);
        } else if (processInstanceId != -1L) {
            legacyFormURL.append("$recap&instance=")
                    .append(processInstanceId);
        } else {
            legacyFormURL.append("$entry&process=")
                    .append(processDefinitionId)
                    .append("&autoInstantiate=false");
        }
        if (userId != -1L) {
            legacyFormURL.append("&userId=")
                    .append(userId);
        }
        return legacyFormURL.toString();
    }

    protected void displayForm(final HttpServletRequest request, final HttpServletResponse response, final APISession apiSession,
            final long processDefinitionId, final long processInstanceId, final long taskInstanceId, final FormReference form,
            final String resourcePath)
            throws InstantiationException, IllegalAccessException, IOException, BonitaException {
        if (!form.isExternal()) {
            try {
                if (resourcePath == null) {
                    //TODO pass the processDefinition, processInstance and taskInstance IDs in order to put them in the Context of the custom page
                    pageRenderer.displayCustomPage(request, response, apiSession, form.getForm());
                } else {
                    //TODO render the resource
                }
            } catch (final PageNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cannot find the form with name " + form.getForm());
            }
        } else {
            if (resourcePath == null) {
                displayExternalPage(request, response, processDefinitionId, processInstanceId, taskInstanceId, form.getForm());
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
    protected void redirectToTaskPage(final HttpServletRequest request, final HttpServletResponse response, final long taskInstanceId) throws IOException {
        final StringBuilder taskURLBuilder = new StringBuilder(request.getContextPath());
        taskURLBuilder.append(request.getServletPath())
                .append("/")
                .append(TASK_INSTANCE_PATH_SEGMENT)
                .append("/")
                .append(taskInstanceId);
        final UrlBuilder urlBuilder = new UrlBuilder(taskURLBuilder.toString());
        urlBuilder.appendParameters(request.getParameterMap());
        response.sendRedirect(response.encodeRedirectURL(urlBuilder.build()));
    }

    protected void displayExternalPage(final HttpServletRequest request, final HttpServletResponse response, final long processDefinitionId,
            final long processInstanceId, final long taskInstanceId, final String url) throws IOException {
        final String externalURL = buildExternalPageURL(request, processDefinitionId, processInstanceId, taskInstanceId, url);
        response.sendRedirect(response.encodeRedirectURL(externalURL));
    }

    @SuppressWarnings("unchecked")
    protected String buildExternalPageURL(final HttpServletRequest request, final long processDefinitionId, final long processInstanceId,
            final long taskInstanceId, final String url) {
        final UrlBuilder urlBuilder = new UrlBuilder(url);
        if (taskInstanceId != -1) {
            urlBuilder.appendParameter(TASK_INSTANCE_PATH_SEGMENT, new UrlValue(Long.toString(taskInstanceId)));
        }
        if (processInstanceId != -1) {
            urlBuilder.appendParameter(PROCESS_INSTANCE_PATH_SEGMENT, new UrlValue(Long.toString(processInstanceId)));
        }
        if (processDefinitionId != -1) {
            urlBuilder.appendParameter(PROCESS_PATH_SEGMENT, new UrlValue(Long.toString(processDefinitionId)));
        }
        urlBuilder.appendParameters(request.getParameterMap());
        return urlBuilder.build();
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
