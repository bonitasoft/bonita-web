/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
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
package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.bonitasoft.engine.bpm.data.DataDefinition;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.server.datastore.utils.VariableMapper;
import org.bonitasoft.web.rest.server.datastore.utils.VariablesMapper;
import org.bonitasoft.web.rest.server.engineclient.CaseEngineClient;
import org.bonitasoft.web.rest.server.engineclient.ProcessEngineClient;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

public class CaseSarter {

    private CaseItem caseItem;
    private CaseEngineClient caseEngineClient;
    private ProcessEngineClient processEngineClient;
    private Long processId;

    public CaseSarter(CaseItem caseItem, CaseEngineClient caseEngineClient, ProcessEngineClient processEngineClient) {
        this.caseItem = caseItem;
        this.processId = caseItem.getProcessId().toLong();
        this.caseEngineClient = caseEngineClient;
        this.processEngineClient = processEngineClient;
    }
    
    public CaseItem start() {
        HashMap<String, Serializable> variables = getVariables(caseItem);
        if (variables.isEmpty()) {
            return startCase();
        } else {
            return startCaseWithVariables(variables);
        }
    }
    
    private HashMap<String, Serializable> getVariables(CaseItem caseItem) {
        String jsonVariables = caseItem.getAttributeValue(CaseItem.ATTRIBUTE_VARIABLES);
        if (StringUtil.isBlank(jsonVariables)) {
            return new HashMap<String, Serializable>();
        }
        return buildVariablesMap(jsonVariables);
    }

    private HashMap<String, Serializable> buildVariablesMap(String jsonValue) {
        List<DataDefinition> dataDefinitions = processEngineClient.getProcessDataDefinitions(processId);
        
        HashMap<String, Serializable> map = new HashMap<String, Serializable>();
        for (VariableMapper var : VariablesMapper.fromJson(jsonValue).getVariables()) {
            DataDefinition data = getDataDefinitionByName(var.getName(), dataDefinitions);
            map.put(var.getName(), var.getSerializableValue(data.getClassName()));
        }
        return map;
    }

    private CaseItem startCaseWithVariables(HashMap<String, Serializable> variables) {
        ProcessInstance processInstance = caseEngineClient.start(processId, variables);
        return new CaseItemConverter().convert(processInstance);
    }

    private CaseItem startCase() {
        ProcessInstance processInstance = caseEngineClient.start(processId);
        return new CaseItemConverter().convert(processInstance);
    }

    private DataDefinition getDataDefinitionByName(String dataName, List<DataDefinition> dataDefinitions) {
        for (DataDefinition dataDefinition : dataDefinitions) {
            if (dataDefinition.getName().equals(dataName)) {
                return dataDefinition;
            }
        }
        throw new APIException(_("Data definition %dataName% doesn't exists for process %processId%", 
                new Arg("dataName", dataName), new Arg("processId", caseItem.getProcessId())));
    }
}
