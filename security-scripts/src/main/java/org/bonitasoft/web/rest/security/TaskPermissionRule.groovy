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
 * Secure
 *
 *
 * humanTask
 * userTask
 * hiddenUserTask
 * activity
 * task
 * flowNode
 * manualTask
 *
 * Archived:
 * archivedTask
 * archivedHumanTask
 * archivedUserTask
 * archivedActivity
 * archivedFlowNode
 * archivedManualTask
 *
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
                def humanTaskId = Long.valueOf(apiCallContext.getResourceId())
                return isTaskAccessibleByUser(processAPI, apiCallContext, logger, humanTaskId, currentUserId, userName)
            } else if (hasFilter(currentUserId, filters, "assigned_id") || hasFilter(currentUserId, filters, "user_id") || hasFilter(currentUserId, filters, "hidden_user_id")) {
                logger.debug("FilterOnUser or FilterOnAssignUser")
                return true
            }
        } else if ("PUT".equals(apiCallContext.getMethod()) && apiCallContext.getResourceId() != null) {
            def humanTaskId = Long.valueOf(apiCallContext.getResourceId())
            return isTaskAccessibleByUser(processAPI, apiCallContext, logger, humanTaskId, currentUserId, userName)
        }
        return false
    }

    private boolean hasFilter(long currentUserId, Map<String, String> filters, String assigned_id) {
        return String.valueOf(currentUserId).equals(filters.get(assigned_id))
    }

    protected boolean isTaskAccessibleByUser(ProcessAPI processAPI, APICallContext apiCallContext, Logger logger, long flowNodeId, long currentUserId, String username) {
        if ("hiddenUserTask".equals(apiCallContext.getResourceName())) {
            return processAPI.isTaskHidden(currentUserId, flowNodeId)
        } else if (apiCallContext.getResourceName().startsWith("archived")) {
            try{
                def archivedFlowNodeInstance = processAPI.getArchivedFlowNodeInstance(flowNodeId)
                if (archivedFlowNodeInstance instanceof ArchivedHumanTaskInstance) {
                    return currentUserId == archivedFlowNodeInstance.getAssigneeId()
                }
            }catch(ArchivedFlowNodeInstanceNotFoundException e){
                logger.debug("flow node does not exists")
                return false
            }
        } else {
            try {
                def instance = processAPI.getHumanTaskInstance(flowNodeId)
                if (instance.assigneeId > 0) {
                    return instance.assigneeId == currentUserId;
                } else {
                    final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 1);
                    builder.filter(UserSearchDescriptor.USER_NAME, username);
                    def searchResult = processAPI.searchUsersWhoCanExecutePendingHumanTask(flowNodeId, builder.done())
                    def isTaskPendingForUser = searchResult.getCount() == 1l
                    logger.debug("The task is pending for user? " + isTaskPendingForUser)
                    return isTaskPendingForUser
                }

            } catch (ActivityInstanceNotFoundException e) {
                logger.debug("The task is not found or is not human, " + e.getMessage())
                return false
            }
        }
    }
}
