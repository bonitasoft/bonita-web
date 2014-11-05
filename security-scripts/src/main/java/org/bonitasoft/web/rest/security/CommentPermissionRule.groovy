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
import org.bonitasoft.engine.api.permission.APICallContext
import org.bonitasoft.engine.api.permission.PermissionRule
import org.bonitasoft.engine.session.APISession

/**
 *
 * Let a user access only comments on cases that he is involved in
 *
 * <ul>
 *     <li>bpm/comment</li>
 *     <li>bpm/archivedComment</li>
 * </ul>
 *
 *
 *
 * @author Baptiste Mesta
 */
class CommentPermissionRule implements PermissionRule {


    @Override
    public boolean check(APISession apiSession, APICallContext apiCallContext, APIAccessor apiAccessor, Logger logger) {
        long currentUserId = apiSession.getUserId();
        if ("GET".equals(apiCallContext.getMethod())) {
            return checkGetMethod(apiCallContext, apiAccessor, currentUserId)
        } else if ("POST".equals(apiCallContext.getMethod())) {
            return checkPostMethod(apiCallContext, apiAccessor, currentUserId, logger)
        }
        return false
    }

    private boolean checkPostMethod(APICallContext apiCallContext, APIAccessor apiAccessor, long currentUserId, Logger logger) {
        def body = apiCallContext.getBodyAsJSON()
        def processInstanceId = body.optLong("processInstanceId")
        if (processInstanceId <= 0) {
            return false;
        }
        def processAPI = apiAccessor.getProcessAPI()
        return processAPI.isInvolvedInProcessInstance(currentUserId, processInstanceId)
    }

    private boolean checkGetMethod(APICallContext apiCallContext, APIAccessor apiAccessor, long currentUserId) {
        def filters = apiCallContext.getFilters()
        def stringUserId = String.valueOf(currentUserId)
        if(stringUserId.equals(filters.get("team_manager_id")) || stringUserId.equals(filters.get("user_id")) || stringUserId.equals(filters.get("supervisor_id"))){
            return true
        }
        if(filters.containsKey("processInstanceId")){
            def processInstanceId = Long.valueOf(filters.get("processInstanceId"))
            return apiAccessor.getProcessAPI().isInvolvedInProcessInstance(currentUserId,processInstanceId)
        }

    }
}
