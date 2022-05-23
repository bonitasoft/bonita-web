/**
 * Copyright (C) 2022 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.bpm.process;

import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.web.rest.model.bpm.cases.CaseInfo;
import org.bonitasoft.web.rest.model.bpm.process.ProcessInfo;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Get;

public class ProcessInfoResource extends CommonResource {

    public static final String PROCESS_ID = "processId";

    private final ProcessAPI processAPI;

    public ProcessInfoResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Get("json")
    public ProcessInfo getProcessInfo() {
        try {
            final long processId = Long.parseLong(getAttribute(PROCESS_ID));
            final ProcessInfo processInfo = new ProcessInfo();
            processInfo.setProcessDefinitionId(processId);
            processInfo.setFlowNodeStatesCounters(getFlownodeCounters(processId));
            return processInfo;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }


    protected Map<String, Map<String, Long>> getFlownodeCounters(final Long processId) throws DataNotFoundException {
        return processAPI.getActiveFlownodeStateCountersForProcessDefinition(processId);
    }
}
