/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.bpm.flownode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.flownode.HiddenUserTaskItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasAdd;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasDelete;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Nicolas Tith
 * 
 */
public class HiddenUserTaskDatastore extends CommonDatastore<HiddenUserTaskItem, Serializable> implements DatastoreHasAdd<HiddenUserTaskItem>,
        DatastoreHasGet<HiddenUserTaskItem>, DatastoreHasDelete {

    /**
     * Default Constructor.
     * 
     * @param engineSession
     */
    public HiddenUserTaskDatastore(final APISession engineSession) {
        super(engineSession);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // C.R.U.D.
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected HiddenUserTaskItem convertEngineToConsoleItem(final Serializable item) {
        return null;
    }

    /**
     * @param id
     * @return null
     */
    @Override
    public HiddenUserTaskItem get(final APIID id) {
        String userId;
        String userTaskId;
        try {

            userId = id.getIds().get(0);
            userTaskId = id.getIds().get(1);
            final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(getEngineSession());

            if (processAPI.isTaskHidden(Long.parseLong(userTaskId), Long.parseLong(userId))) {
            } else {
                throw new APIItemNotFoundException("Hidden User Task", id);
            }

        } catch (final Exception e) {
            throw new APIException(e);
        }
        final HiddenUserTaskItem huti = new HiddenUserTaskItem();
        huti.setUserId(userId);
        huti.setTaskId(userTaskId);
        return huti;
    }

    /**
     * 
     * @param item
     * @return HiddenUserTaskItem
     */
    @Override
    public HiddenUserTaskItem add(final HiddenUserTaskItem item) {
        try {
            TenantAPIAccessor.getProcessAPI(getEngineSession())
                    .hideTasks(
                            item.getUserId().toLong(),
                            item.getTaskId().toLong()
                    );
            return item;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public void delete(final List<APIID> ids) {
        try {
            final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(getEngineSession());
            final HashMap<String, ArrayList<Long>> unhideByUser = new HashMap<String, ArrayList<Long>>();
            for (final APIID apiId : ids) {
                final String userId = apiId.getIds().get(0);
                final Long activityId = Long.parseLong(apiId.getIds().get(1));
                if (unhideByUser.containsKey(userId)) {
                    unhideByUser.get(userId).add(activityId);
                } else {
                    final ArrayList<Long> activityIds = new ArrayList<Long>();
                    activityIds.add(activityId);
                    unhideByUser.put(userId, activityIds);
                }
            }
            for (final String userIdAsString : unhideByUser.keySet()) {
                final long userId = Long.parseLong(userIdAsString);
                processAPI.unhideTasks(userId, unhideByUser.get(userIdAsString).toArray(new Long[0]));
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }
}
