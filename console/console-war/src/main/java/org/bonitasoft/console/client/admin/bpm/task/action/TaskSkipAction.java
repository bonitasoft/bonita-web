/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.bpm.task.action;

import java.util.Collections;
import java.util.Map;

import org.bonitasoft.web.rest.api.model.bpm.flownode.ActivityDefinition;
import org.bonitasoft.web.rest.api.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APIUpdateCallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;
import org.bonitasoft.web.toolkit.client.ui.action.ActionOnItemId;

/**
 * @author Vincent Elcrin
 * 
 */
public class TaskSkipAction extends ActionOnItemId {

    private final APIUpdateCallback callback;

    public TaskSkipAction(APIID taskId, final APIUpdateCallback callback) {
        this.callback = callback;
        setItemId(taskId);
    }

    @Override
    protected void execute(APIID taskId) {
        APIRequest
                .update(taskId, createStateUpdateMap(), ActivityDefinition.get(), callback)
                .run();
    }

    private Map<String, String> createStateUpdateMap() {
        return Collections.singletonMap(IFlowNodeItem.ATTRIBUTE_STATE, IFlowNodeItem.VALUE_STATE_SKIPPED);
    }
}
