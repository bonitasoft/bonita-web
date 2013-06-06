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
package org.bonitasoft.console.client.admin.bpm.cases.model;

import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIDeleteRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.APIQueue;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * @author Nicolas Tith
 * 
 */
public class CaseAPICaller {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DELETE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static APIDeleteRequest makeDeleteRequest(final APIID caseId) {

        return APIRequest.delete(caseId, Definitions.get(CaseDefinition.TOKEN), null);
    }

    public static void delete(final APIID caseId, final APICallback callback) {
        makeDeleteRequest(caseId).run(callback);
    }

    public static void delete(final APIID caseId, final Action onSuccess) {
        delete(caseId, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                onSuccess.execute();
            }

        });
    }

    public static void delete(final List<APIID> caseIds, final Action onFinish) {
        delete(caseIds, onFinish, null);
    }

    public static void delete(final List<APIID> caseIds, final Action onFinish, final Action onError) {
        final APIQueue queue = new APIQueue();
        for (final APIID caseId : caseIds) {
            queue.addRequest(makeDeleteRequest(caseId));
        }
        queue.run(onFinish, onError);
    }

}
