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
import org.bonitasoft.engine.exception.NotFoundException
import org.bonitasoft.engine.session.APISession

/**
 *
 * Let a user add an actorMember only if he is process owner
 *
 * <ul>
 *     <li>bpm/actorMember</li>
 *     <li>bpm/delegation</li>
 * </ul>
 *
 *
 *
 * @author Baptiste Mesta
 */
class ActorMemberPermissionRule implements PermissionRule {

    public static final String ACTOR_ID = "actor_id"

    @Override
    public boolean check(APISession apiSession, APICallContext apiCallContext, APIAccessor apiAccessor, Logger logger) {
        long currentUserId = apiSession.getUserId();
        if (apiCallContext.isPOST()) {
            return checkPostMethod(apiCallContext, apiAccessor, currentUserId, logger)
        } else if (apiCallContext.isGET()) {
            return checkGetMethod(apiCallContext, apiAccessor, currentUserId, logger)
        } else if (apiCallContext.isDELETE()) {
            //TODO unable to find an actor member with the API!
            return false
        }
        //it's ok to read
        return true
    }

    private boolean checkPostMethod(APICallContext apiCallContext, APIAccessor apiAccessor, long currentUserId, Logger logger) {
        def body = apiCallContext.getBodyAsJSONArray()
        for (int i = 0; i < body.length(); i++) {
            def object = body.getJSONObject(i)
            def actorId = Long.valueOf(object.optString((ACTOR_ID)))
            if (actorId <= 0) {
                continue
            }
            def processAPI = apiAccessor.getProcessAPI()
            try {
                def actor = processAPI.getActor(actorId)
                def processDefinitionId = actor.getProcessDefinitionId()
                if (!processAPI.isUserProcessSupervisor(processDefinitionId, currentUserId)) {
                    return false
                }
            } catch (NotFoundException e) {
                return true
            }
        }
        return true
    }

    private boolean checkGetMethod(APICallContext apiCallContext, APIAccessor apiAccessor, long currentUserId, Logger logger) {
        try {
            def filters = apiCallContext.getFilters()
            if (filters.containsKey(ACTOR_ID)) {
                def processAPI = apiAccessor.getProcessAPI()
                def actor = processAPI.getActor(Long.parseLong(filters.get(ACTOR_ID)))
                def processDefinitionId = actor.getProcessDefinitionId()
                return processAPI.isUserProcessSupervisor(processDefinitionId, currentUserId)
            }
            return true
        } catch (NotFoundException e) {
            return true
        }
    }
}
