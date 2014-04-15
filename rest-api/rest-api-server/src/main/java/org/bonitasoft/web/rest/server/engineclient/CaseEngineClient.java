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
package org.bonitasoft.web.rest.server.engineclient;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessExecutionException;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.common.i18n._;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Colin PUY
 * @author Elias Ricken de Medeiros
 * 
 */
// TODO migrate all engine methods relating to cases (i.e. especially those in CaseDatastore) in this class
public class CaseEngineClient {

    protected ProcessAPI processAPI;

    public CaseEngineClient(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    public ProcessInstance start(final long userId, final long processId) {
        return start(userId, processId, null);
    }

    public ProcessInstance start(final long userId, final long processId, final Map<String, Serializable> variables) {
        try {
            if (userId != -1L) {
                if (variables == null || variables.isEmpty()) {
                    return processAPI.startProcess(userId, processId);
                } else {
                    // FIXME restore this once the engine method is available
                    // return processAPI.startProcess(userId, processId, variables);
                    return processAPI.startProcess(userId, processId);
                }
            } else {
                if (variables == null || variables.isEmpty()) {
                    return processAPI.startProcess(processId);
                } else {
                    return processAPI.startProcess(processId, variables);
                }
            }
        } catch (final ProcessDefinitionNotFoundException e) {
            throw new APIException(new _("Can't start process, process %processId% not found", new Arg("processId", processId)), e);
        } catch (final ProcessActivationException e) {
            throw new APIException(new _("Can't start process, process %processId% is not enabled", new Arg("processId", processId)), e);
        } catch (final ProcessExecutionException e) {
            throw new APIException(new _("Error occured when starting process %processId%", new Arg("processId", processId)), e);
        } catch (final UserNotFoundException e) {
            throw new APIException(
                    new _("Can't start process %processId%, user %userId% not found", new Arg("processId", processId), new Arg("userId", userId)), e);
        }
    }

    public long countOpenedCases() {
        final SearchOptions search = new SearchOptionsBuilder(0, 0).done();
        try {
            return processAPI.searchOpenProcessInstances(search).getCount();
        } catch (final Exception e) {
            throw new APIException("Error when counting opened cases", e);
        }
    }

    public List<ArchivedProcessInstance> searchArchivedCasesInAllStates(final SearchOptions searchOptions) {
        try {
            return processAPI.searchArchivedProcessInstancesInAllStates(searchOptions).getResult();
        } catch (final SearchException e) {
            throw new APIException("Error when searching cases in all state", e);
        }
    }

    public ProcessInstance getProcessInstance(final long processInstanceId) {
        try {
            return processAPI.getProcessInstance(processInstanceId);
        } catch (final ProcessInstanceNotFoundException e) {
            throw new APIItemNotFoundException(CaseDefinition.TOKEN, APIID.makeAPIID(processInstanceId));
        }
    }

    public ArchivedProcessInstance getArchivedProcessInstance(final long processInstanceId) {
        try {
            return processAPI.getArchivedProcessInstance(processInstanceId);
        } catch (final ArchivedProcessInstanceNotFoundException e) {
            throw new APIItemNotFoundException(ArchivedCaseDefinition.TOKEN, APIID.makeAPIID(processInstanceId));
        }
    }
}
