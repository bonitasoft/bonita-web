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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoUpdater;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;


/**
 * Process engine API client
 * 
 * Wrapper to processAPI for all process methods
 * 
 * @author Colin PUY
 */
public class ProcessEngineClient {

    private static int DELETE_PROCESS_BUNCH_SIZE = 100;
    
    protected ProcessAPI processAPI;
    
    public ProcessEngineClient(ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    public ProcessDeploymentInfo getProcessDeploymentInfo(long processId) {
        try {
            return processAPI.getProcessDeploymentInfo(processId);
        } catch (ProcessDefinitionNotFoundException e) {
            // if not found, return null
            return null;
        } catch (Exception e) {
            throw new APIException("Error when getting process deployement informations", e);
        }
    }
    
    
    public ProcessDefinition deploy(BusinessArchive businessArchive) {
        try {
            return processAPI.deploy(businessArchive);
        } catch (AlreadyExistsException e) {
            DesignProcessDefinition processDefinition = businessArchive.getProcessDefinition();
            throw new APIException(_("Apps %appName% in version %version% already exists", 
                    new Arg("appName", processDefinition.getName()), 
                    new Arg("version", processDefinition.getVersion())), e);
        } catch (Exception e) {
            throw new APIException(_("Unable to deploy business archive"), e);
        }
    }
    
    public void enableProcess(long processId) {
        try {
            processAPI.enableProcess(processId);
        } catch (Exception e) {
            throw new APIException(_("Unable to enable process"), e);
        }
    }
    
    public void disableProcess(long processId) {
        try {
            processAPI.disableProcess(processId);
        } catch (Exception e) {
            throw new APIException(_("Unable to disable process"), e);
        }
    }
    
    public ProcessDeploymentInfo updateProcessDeploymentInfo(long processId, ProcessDeploymentInfoUpdater processDeploymentInfoUpdater) {
        try {
            processAPI.updateProcessDeploymentInfo(processId, processDeploymentInfoUpdater);
            return processAPI.getProcessDeploymentInfo(processId);
        } catch (Exception e) {
            throw new APIException("Error when updating process deployment informations", e);
        }
    }
    
    public void deleteDisabledProcesses(List<Long> processIds) {
        try {
            for (Long id : processIds) {
                deleteProcessInstancesByBunch(id, DELETE_PROCESS_BUNCH_SIZE);
                deleteArchivedProcessInstancesByBunch(id, DELETE_PROCESS_BUNCH_SIZE);
                processAPI.deleteProcessDefinition(id);
            }
        } catch (DeletionException e) {
            throw new APIException("Error when deleting processe(s) " + processIds, e);
        }
    }

    /**
     * Delete archived process instances by bunch for a given processId
     */
    public void deleteArchivedProcessInstancesByBunch(long processId, int bunchSize) throws DeletionException {
        long numberOfDeletedArchivedProcessInstances = 0;
        do {
            numberOfDeletedArchivedProcessInstances = processAPI.deleteArchivedProcessInstances(processId, 0, bunchSize);
        } while (numberOfDeletedArchivedProcessInstances >= bunchSize);
    }

    /**
     * Delete process instances by bunch for a given processId
     */
    public void deleteProcessInstancesByBunch(long processId, int bunchSize) throws DeletionException {
        long numberOfDeletedProcessInstances = 0;
        do {
            numberOfDeletedProcessInstances = processAPI.deleteProcessInstances(processId, 0, bunchSize);
        } while (numberOfDeletedProcessInstances >= bunchSize);
    }
    
    public SearchResult<ProcessDeploymentInfo> searchProcessDefinitions(SearchOptions searchOptions) {
        try {
            return processAPI.searchProcessDeploymentInfos(searchOptions);
        } catch (Exception e) {
            throw new APIException("Error when searching process definition", e);
        }
    }
    
    public SearchResult<ProcessDeploymentInfo> searchUncategorizedProcessDefinitionsSupervisedBy(long userId, SearchOptions searchOptions) {
        try {
            return processAPI.searchUncategorizedProcessDeploymentInfosSupervisedBy(userId, searchOptions);
        } catch (Exception e) {
            throw new APIException("Error when searching uncategorized process definition supervised by user " + userId, e);
        }
    }
    
    public SearchResult<ProcessDeploymentInfo> searchProcessDefinitionsSupervisedBy(long userId, SearchOptions searchOptions) {
        try {
            return processAPI.searchUncategorizedProcessDeploymentInfosSupervisedBy(userId, searchOptions);
        } catch (Exception e) {
            throw new APIException("Error when searching process definition supervised by user " + userId, e);
        } 
    }
    
    public SearchResult<ProcessDeploymentInfo> searchUncategorizedProcessDefinitionsUserCanStart(long userId, SearchOptions searchOptions) {
        try {
            return processAPI.searchUncategorizedProcessDeploymentInfosUserCanStart(userId, searchOptions);
        } catch (Exception e) {
            throw new APIException("Error when searching uncategorized process definition which can be started by user " + userId, e);
        } 
    }
    
    public SearchResult<ProcessDeploymentInfo> searchRecentlyStartedProcessDefinitions(long userId, SearchOptions searchOptions) {
        try {
            return processAPI.searchProcessDeploymentInfosStartedBy(userId, searchOptions);
        } catch (Exception e) {
            throw new APIException("Error when searching recently started process by user " + userId, e);
        } 
    }
    
    public long countResolvedProcesses() {
        SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 0);
        builder.filter(ProcessDeploymentInfoSearchDescriptor.CONFIGURATION_STATE, ProcessItem.VALUE_CONFIGURATION_STATE_RESOLVED);
        try {
            return processAPI.searchProcessDeploymentInfos(builder.done()).getCount();
        } catch (Exception e) {
            throw new APIException("Error when counting resolved processes", e);
        }
    }
    
    public SearchResult<ProcessDeploymentInfo> searchProcessDeploymentInfos(long userId, SearchOptions searchOptions) {
        try {
            return processAPI.searchProcessDeploymentInfos(userId, searchOptions);
        } catch (SearchException e) {
            throw new APIException("Error when searching process user can start", e);
        }
    }
}
