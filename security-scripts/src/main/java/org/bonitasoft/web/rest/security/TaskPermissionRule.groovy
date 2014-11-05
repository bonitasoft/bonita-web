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
 **/


package org.bonitasoft.web.rest.security

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.Logger
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.api.permission.APICallContext
import org.bonitasoft.engine.api.permission.PermissionRule
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstanceNotFoundException
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstance
import org.bonitasoft.engine.identity.UserSearchDescriptor
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.session.APISession

/**
 *
 * Let a user access only tasks that are assigned or pending to him
 *
 *
 * can be added to
 * <ul>
 *     <li>bpm/humanTask</li>
 *     <li>bpm/userTask</li>
 *     <li>bpm/archivedHumanTask</li>
 *     <li>bpm/archivedUserTask</li>
 *     <li>bpm/hiddenUserTask</li>
 *     <li>bpm/activity</li>
 *     <li>bpm/archivedActivity</li>
 *     <li>bpm/task</li>
 *     <li>bpm/archivedTask</li>
 *     <li>bpm/flowNode</li>
 *     <li>bpm/archivedFlowNode</li>
 *     <li>bpm/manualTask</li>
 *     <li>bpm/archivedManualTask</li>
 *     <li>bpm/archivedTask</li>
 * </ul>
 *
 *
 * @author Baptiste Mesta
 */
class TaskPermissionRule implements PermissionRule {

    @Override
    public boolean check(APISession apiSession, APICallContext apiCallContext, APIAccessor apiAccessor, Logger logger) {
        long currentUserId = apiSession.getUserId();
        def userName = apiSession.getUserName()
        def processAPI = apiAccessor.getProcessAPI();
        def filters = apiCallContext.getFilters()
        if ("GET".equals(apiCallContext.getMethod())) {
            if (apiCallContext.getResourceId() != null) {
                return isTaskAccessibleByUser(processAPI, apiCallContext, logger, currentUserId, userName)
            } else if (hasFilter(currentUserId, filters, "assigned_id") || hasFilter(currentUserId, filters, "user_id") || hasFilter(currentUserId, filters, "hidden_user_id")) {
                logger.debug("FilterOnUser or FilterOnAssignUser")
                return true
            } else if (filters.containsKey("parentTaskId")) {
                def long parentTaskId = Long.valueOf(filters.get("parentTaskId"))
                return isHumanTaskAccessible(processAPI, parentTaskId, currentUserId, userName, logger)
            }
        } else if ("PUT".equals(apiCallContext.getMethod()) && apiCallContext.getResourceId() != null) {
            return isTaskAccessibleByUser(processAPI, apiCallContext, logger, currentUserId, userName)
        } else if ("POST".equals(apiCallContext.getMethod()) && "hiddenUserTask".equals(apiCallContext.getResourceName())) {
            def bodyAsJSON = apiCallContext.getBodyAsJSON()
            return currentUserId.equals(bodyAsJSON.getLong("user_id")) && isHumanTaskAccessible(processAPI, bodyAsJSON.getLong("task_id"), currentUserId, userName, logger)
        } else if ("DELETE".equals(apiCallContext.getMethod()) && "hiddenUserTask".equals(apiCallContext.getResourceName())) {
            def ids = apiCallContext.getCompoundResourceId()
            return currentUserId.equals(Long.valueOf(ids.get(0))) && isHumanTaskAccessible(processAPI, Long.valueOf(ids.get(1)), currentUserId, userName, logger)
        }
        return false
    }

    private boolean hasFilter(long currentUserId, Map<String, String> filters, String assigned_id) {
        return String.valueOf(currentUserId).equals(filters.get(assigned_id))
    }

    protected boolean isTaskAccessibleByUser(ProcessAPI processAPI, APICallContext apiCallContext, Logger logger, long currentUserId, String username) {
        if ("hiddenUserTask".equals(apiCallContext.getResourceName())) {
            return true
        } else if (apiCallContext.getResourceName().startsWith("archived")) {
            try{
                def archivedFlowNodeInstance = processAPI.getArchivedFlowNodeInstance(Long.valueOf(apiCallContext.getResourceId()))
                if (archivedFlowNodeInstance instanceof ArchivedHumanTaskInstance) {
                    return currentUserId == archivedFlowNodeInstance.getAssigneeId()
                }
            }catch(ArchivedFlowNodeInstanceNotFoundException e){
                logger.debug("flow node does not exists")
                return false
            }
        } else {
            return isHumanTaskAccessible(processAPI, Long.valueOf(apiCallContext.getResourceId()), currentUserId, username, logger)
        }
    }

    private boolean isHumanTaskAccessible(ProcessAPI processAPI, long flowNodeId, long currentUserId, String username, Logger logger) {
        def isAccessible = false
        try {
            def instance = processAPI.getHumanTaskInstance(flowNodeId)
            if (instance.assigneeId > 0) {
                isAccessible = instance.assigneeId == currentUserId;
            } else {
                final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 1);
                builder.filter(UserSearchDescriptor.USER_NAME, username);
                def searchResult = processAPI.searchUsersWhoCanExecutePendingHumanTask(flowNodeId, builder.done())
                def isTaskPendingForUser = searchResult.getCount() == 1l
                logger.debug("The task is pending for user? " + isTaskPendingForUser)
                isAccessible = isTaskPendingForUser
            }

        } catch (ActivityInstanceNotFoundException e) {
            logger.debug("The task is not found or is not human, " + e.getMessage())
        }
        isAccessible
    }
}
