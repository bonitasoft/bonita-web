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
package org.bonitasoft.console.client.user.task.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIQueue;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.APIUpdateRequest;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * @author Séverin Moussel
 * 
 */
public class TaskAPI {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CLAIM
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static APIUpdateRequest makeClaimRequest(final APIID taskId, final APIID userId) {
        return makeAssignRequest(taskId, userId);
    }

    public static void claim(final APIID taskId, final APIID userId, final APICallback callback) {
        makeClaimRequest(taskId, userId).run(callback);
    }

    public static void claim(final APIID taskId, final APIID userId, final Action onSuccess) {
        claim(taskId, userId, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                onSuccess.execute();
            }

        });
    }

    public static void claim(final List<APIID> taskIds, final APIID userId, final Action onFinish) {
        claim(taskIds, userId, onFinish, null);
    }

    public static void claim(final List<APIID> taskIds, final APIID userId, final Action onFinish, final Action onError) {
        final APIQueue queue = new APIQueue();
        for (final APIID taskId : taskIds) {
            queue.addRequest(makeClaimRequest(taskId, userId));
        }
        queue.run(onFinish, onError);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ASSIGN
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static APIUpdateRequest makeAssignRequest(final APIID taskId, final APIID userId) {
        final HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, userId.toString());

        return APIRequest.update(taskId, attributes, Definitions.get(HumanTaskDefinition.TOKEN), null);
    }

    public static void assign(final APIID taskId, final APIID userId, final APICallback callback) {
        makeAssignRequest(taskId, userId).run(callback);
    }

    public static void assign(final APIID taskId, final APIID userId, final Action onSuccess) {
        assign(taskId, userId, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                onSuccess.execute();
            }

        });
    }

    public static void assign(final List<APIID> taskIds, final APIID userId, final Action onFinish) {
        assign(taskIds, userId, onFinish, null);
    }

    public static void assign(final List<APIID> taskIds, final APIID userId, final Action onFinish, final Action onError) {
        final APIQueue queue = new APIQueue();
        for (final APIID taskId : taskIds) {
            queue.addRequest(makeAssignRequest(taskId, userId));
        }
        queue.run(onFinish, onError);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // RELEASE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static APIUpdateRequest makeReleaseRequest(final APIID taskId) {
        final HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, null);

        return APIRequest.update(taskId, attributes, Definitions.get(HumanTaskDefinition.TOKEN), null);
    }

    public static void release(final APIID taskId, final APICallback callback) {
        makeReleaseRequest(taskId).run(callback);
    }

    public static void release(final APIID taskId, final Action onSuccess) {
        release(taskId, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                onSuccess.execute();
            }

        });
    }

    public static void release(final List<APIID> taskIds, final Action onFinish) {
        release(taskIds, onFinish, null);
    }

    public static void release(final List<APIID> taskIds, final Action onFinish, final Action onError) {
        final APIQueue queue = new APIQueue();
        for (final APIID taskId : taskIds) {
            queue.addRequest(makeReleaseRequest(taskId));
        }
        queue.run(onFinish, onError);
    }
}
