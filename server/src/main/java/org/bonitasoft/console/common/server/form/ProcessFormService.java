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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ActivationState;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstancesSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;

public class ProcessFormService {

    protected static final String PROCESS_DEPLOY = "process_deploy";

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(ProcessFormService.class.getName());

    public String getProcessPath(final APISession apiSession, final long processDefinitionId) throws BonitaException, UnsupportedEncodingException {
        final ProcessDeploymentInfo processDeploymentInfo = getProcessAPI(apiSession).getProcessDeploymentInfo(processDefinitionId);
        return encodePathSegment(processDeploymentInfo.getName())
                + "/"
                + encodePathSegment(processDeploymentInfo.getVersion());
    }

    public String encodePathSegment(final String stringToEncode) throws UnsupportedEncodingException {
        return URLEncoder.encode(stringToEncode, "UTF-8").replaceAll("\\+", "%20").replaceAll("%2F", "/");
    }

    public long getProcessDefinitionId(final APISession apiSession, final String processName, final String processVersion)
            throws ProcessDefinitionNotFoundException, BonitaException {
        if (processName != null && processVersion != null) {
            try {
                return getProcessAPI(apiSession).getProcessDefinitionId(processName, processVersion);
            } catch (final ProcessDefinitionNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Wrong parameters for process name and version", e);
                }
            }
        }
        return -1L;
    }

    public long getTaskInstanceId(final APISession apiSession, final long processInstanceId, final String taskName, final long userId)
            throws BonitaException {
        final ProcessAPI processAPI = getProcessAPI(apiSession);
        long ensuredUserId;
        if (userId != -1L) {
            ensuredUserId = userId;
        } else {
            ensuredUserId = apiSession.getUserId();
        }
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 1);
        searchOptionsBuilder.filter(HumanTaskInstanceSearchDescriptor.PARENT_CONTAINER_ID, processInstanceId);
        searchOptionsBuilder.filter(HumanTaskInstanceSearchDescriptor.NAME, taskName);
        final SearchResult<HumanTaskInstance> searchMyAvailableHumanTasks = processAPI.searchMyAvailableHumanTasks(ensuredUserId,
                searchOptionsBuilder.done());
        if (searchMyAvailableHumanTasks.getCount() > 0) {
            return searchMyAvailableHumanTasks.getResult().get(0).getId();
        } else {
            final SearchOptionsBuilder archivedSearchOptionsBuilder = new SearchOptionsBuilder(0, 1);
            archivedSearchOptionsBuilder.filter(ArchivedHumanTaskInstanceSearchDescriptor.ASSIGNEE_ID, ensuredUserId);
            archivedSearchOptionsBuilder.filter(ArchivedHumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, processInstanceId);
            archivedSearchOptionsBuilder.filter(ArchivedHumanTaskInstanceSearchDescriptor.NAME, taskName);
            final SearchResult<ArchivedHumanTaskInstance> searchArchivedHumanTasks = processAPI.searchArchivedHumanTasks(archivedSearchOptionsBuilder
                    .done());
            if (searchArchivedHumanTasks.getCount() > 0) {
                return searchArchivedHumanTasks.getResult().get(0).getSourceObjectId();
            }
        }
        return -1L;
    }

    public String getTaskName(final APISession apiSession, final long taskInstanceId) throws ActivityInstanceNotFoundException, BonitaException {
        if (taskInstanceId != -1L) {
            final ProcessAPI processAPI = getProcessAPI(apiSession);
            try {
                final ActivityInstance activity = processAPI.getActivityInstance(taskInstanceId);
                return activity.getName();
            } catch (final ActivityInstanceNotFoundException e) {
                final ArchivedActivityInstance activity = processAPI.getArchivedActivityInstance(taskInstanceId);
                return activity.getName();
            }
        }
        return null;
    }

    public long ensureProcessDefinitionId(final APISession apiSession, final long processDefinitionId, final long processInstanceId, final long taskInstanceId)
            throws ArchivedProcessInstanceNotFoundException, ActivityInstanceNotFoundException, BonitaException {
        if (processDefinitionId != -1L) {
            return processDefinitionId;
        } else if (processInstanceId != -1L) {
            return getProcessDefinitionIdFromProcessInstanceId(apiSession, processInstanceId);
        } else {
            return getProcessDefinitionIdFromTaskId(apiSession, taskInstanceId);
        }
    }

    protected long getProcessDefinitionIdFromTaskId(final APISession apiSession, final long taskInstanceId) throws BonitaHomeNotSetException,
            ServerAPIException, UnknownAPITypeException, ActivityInstanceNotFoundException {
        final ProcessAPI processAPI = getProcessAPI(apiSession);
        try {
            final ActivityInstance activity = processAPI.getActivityInstance(taskInstanceId);
            return activity.getProcessDefinitionId();
        } catch (final ActivityInstanceNotFoundException e) {
            final ArchivedActivityInstance activity = processAPI.getArchivedActivityInstance(taskInstanceId);
            return activity.getProcessDefinitionId();
        }
    }

    protected long getProcessDefinitionIdFromProcessInstanceId(final APISession apiSession, final long processInstanceId) throws BonitaHomeNotSetException,
            ServerAPIException, UnknownAPITypeException, ArchivedProcessInstanceNotFoundException {
        final ProcessAPI processAPI = getProcessAPI(apiSession);
        try {
            final ProcessInstance processInstance = processAPI.getProcessInstance(processInstanceId);
            return processInstance.getProcessDefinitionId();
        } catch (final ProcessInstanceNotFoundException e) {
            final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 1);
            searchOptionsBuilder.filter(ArchivedProcessInstancesSearchDescriptor.SOURCE_OBJECT_ID, processInstanceId);
            searchOptionsBuilder.sort(ArchivedProcessInstancesSearchDescriptor.ARCHIVE_DATE, Order.ASC);
            SearchResult<ArchivedProcessInstance> searchArchivedProcessInstances = null;
            try {
                searchArchivedProcessInstances = processAPI.searchArchivedProcessInstancesInAllStates(searchOptionsBuilder.done());
            } catch (final SearchException se) {
                throw new ArchivedProcessInstanceNotFoundException(se);
            }
            if (searchArchivedProcessInstances != null && searchArchivedProcessInstances.getCount() > 0) {
                return searchArchivedProcessInstances.getResult().get(0).getProcessDefinitionId();
            } else {
                throw new ArchivedProcessInstanceNotFoundException(processInstanceId);
            }
        }
    }

    protected boolean isLoggedUserAdmin(final HttpServletRequest request) {
    	final HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        final Set<String> userPermissions = (Set<String>) session.getAttribute(SessionUtil.PERMISSIONS_SESSION_PARAM_KEY);
    	return userPermissions.contains(PROCESS_DEPLOY);
    }

    protected boolean isLoggedUserProcessSupervisor(final APISession apiSession, final long processDefinitionId) throws BonitaException {
        final ProcessAPI processAPI = getProcessAPI(apiSession);
        return processAPI.isUserProcessSupervisor(processDefinitionId, apiSession.getUserId());
    }

    public boolean isAllowedToSeeTask(final APISession apiSession, final long taskInstanceId, final long enforcedUserId,
            final boolean assignTask) throws BonitaException {
        final ProcessAPI processAPI = getProcessAPI(apiSession);
        try {
            if (processAPI.isInvolvedInHumanTaskInstance(enforcedUserId, taskInstanceId)) {
                if (assignTask) {
                    assignTaskIfNotAssigned(apiSession, taskInstanceId, enforcedUserId);
                }
                return true;
            }
            return false;
        } catch (final ActivityInstanceNotFoundException e) {
            final ArchivedActivityInstance archivedActivity = processAPI.getArchivedActivityInstance(taskInstanceId);
            return enforcedUserId == archivedActivity.getExecutedBy();
        }
    }

    public boolean isAllowedToSeeProcessInstance(final APISession apiSession, final long processInstanceId,
            final long enforcedUserId) throws BonitaException {
        final ProcessAPI processAPI = getProcessAPI(apiSession);
        return processAPI.isInvolvedInProcessInstance(enforcedUserId, processInstanceId);
    }

    public boolean isAllowedToStartProcess(final APISession apiSession, final long processDefinitionId, final long enforcedUserId) throws BonitaException {
        if (ActivationState.ENABLED.equals(getProcessAPI(apiSession).getProcessDeploymentInfo(processDefinitionId).getActivationState())) {
            final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
            parameters.put("USER_ID_KEY", enforcedUserId);
            parameters.put("PROCESS_DEFINITION_ID_KEY", processDefinitionId);
            return (Boolean) getCommandAPI(apiSession).execute("canStartProcessDefinition", parameters);
        }
        return false;
    }

    public boolean isAllowedAsAdminOrProcessSupervisor(final HttpServletRequest request, final APISession apiSession, final long processDefinitionId,
            final long taskInstanceId, final long userId, final boolean assignTask) throws BonitaException {
        if (isLoggedUserAdmin(request) || isLoggedUserProcessSupervisor(apiSession, processDefinitionId)) {
            if (taskInstanceId != -1L && assignTask) {
                assignTaskIfNotAssigned(apiSession, taskInstanceId, userId);
            }
            return true;
        }
        return false;
    }

    protected void assignTaskIfNotAssigned(final APISession apiSession, final long taskId, final long userId) throws BonitaException {
        final HumanTaskInstance humanTaskInstance = getProcessAPI(apiSession).getHumanTaskInstance(taskId);
        if (userId != -1 && humanTaskInstance.getAssigneeId() != userId) {
            getProcessAPI(apiSession).assignUserTask(taskId, userId);
        }
    }

    protected ProcessAPI getProcessAPI(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getProcessAPI(apiSession);
    }

    protected CommandAPI getCommandAPI(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getCommandAPI(apiSession);
    }
}
