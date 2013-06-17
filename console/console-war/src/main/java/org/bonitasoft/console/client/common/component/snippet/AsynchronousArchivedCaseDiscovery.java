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
package org.bonitasoft.console.client.common.component.snippet;

import java.util.Map;

import org.bonitasoft.web.rest.api.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;

/**
 * @author Vincent Elcrin
 * 
 */
public class AsynchronousArchivedCaseDiscovery extends ArchivedCaseDiscovery {

    private APIID caseId;

    public AsynchronousArchivedCaseDiscovery(APIID caseId) {
        super(false);
        this.caseId = caseId;
    }

    @Override
    public void isArchived(final ArchiveDiscoveryCallback callback) {
        APIRequest.get(caseId,
                CaseDefinition.get(),
                new APICallback() {

                    @Override
                    public void onSuccess(int httpStatusCode, String response, Map<String, String> headers) {
                        callback.isArchived(false);
                    }

                    @Override
                    protected void on404NotFound(String message) {
                        callback.isArchived(true);
                    }

                }).run();
    }

}
