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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoUpdater;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.ProcessInstanceHierarchicalDeletionException;
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

    private static Logger LOGGER = Logger.getLogger(ProcessEngineClient.class.getName());

    private static int DELETE_PROCESS_BUNCH_SIZE = 100;

    protected ProcessAPI processAPI;

    public ProcessEngineClient(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    public ProcessDeploymentInfo getProcessDeploymentInfo(final long processId) {
        try {
            return processAPI.getProcessDeploymentInfo(processId);
        } catch (final ProcessDefinitionNotFoundException e) {
            // if not found, return null
            return null;
        } catch (final Exception e) {
            throw new APIException("Error when getting process deployement informations", e);
        }
    }

    public ProcessDefinition deploy(final BusinessArchive businessArchive) {
        try {
            return processAPI.deploy(businessArchive);
        } catch (final AlreadyExistsException e) {
            final DesignProcessDefinition processDefinition = businessArchive.getProcessDefinition();
            throw new APIException(_("Apps %appName% in version %version% already exists",
                    new Arg("appName", processDefinition.getName()),
                    new Arg("version", processDefinition.getVersion())), e);
        } catch (final Exception e) {
            throw new APIException(_("Unable to deploy business archive"), e);
        }
    }

    public void enableProcess(final long processId) {
        try {
            processAPI.enableProcess(processId);
        } catch (final Exception e) {
            throw new APIException(_("Unable to enable process"), e);
        }
    }

    public void disableProcess(final long processId) {
        try {
            processAPI.disableProcess(processId);
        } catch (final Exception e) {
            throw new APIException(_("Unable to disable process"), e);
        }
    }

    public ProcessDeploymentInfo updateProcessDeploymentInfo(final long processId, final ProcessDeploymentInfoUpdater processDeploymentInfoUpdater) {
        try {
            processAPI.updateProcessDeploymentInfo(processId, processDeploymentInfoUpdater);
            return processAPI.getProcessDeploymentInfo(processId);
        } catch (final Exception e) {
            throw new APIException("Error when updating process deployment informations", e);
        }
    }

    public void deleteDisabledProcesses(final List<Long> processIds) {
        try {
            for (final Long id : processIds) {
                deleteProcessInstancesByBunch(id, DELETE_PROCESS_BUNCH_SIZE, processIds);
                deleteArchivedProcessInstancesByBunch(id, DELETE_PROCESS_BUNCH_SIZE, processIds);
                processAPI.deleteProcessDefinition(id);
            }
        } catch (final BonitaException e) {
            throw new APIException("Error when deleting processe(s) " + processIds, e);
        }
    }

    /**
     * Delete archived process instances by bunch for a given processId
     * 
     * @throws ProcessDefinitionNotFoundException
     * @throws DeletionException
     */
    public void deleteArchivedProcessInstancesByBunch(final long processId, final int bunchSize, final List<Long> processesAllowedToBeDeletedIds)
            throws DeletionException, ProcessDefinitionNotFoundException {
        long numberOfDeletedArchivedProcessInstances = 0;
        do {
            try {
                numberOfDeletedArchivedProcessInstances = processAPI.deleteArchivedProcessInstances(processId, 0, bunchSize);
            } catch (final ProcessInstanceHierarchicalDeletionException e) {
                final long parentProcessInstanceID = e.getProcessInstanceId();
                final long parentProcessID = processAPI.getProcessDefinitionIdFromProcessInstanceId(parentProcessInstanceID);
                if (processesAllowedToBeDeletedIds.contains(parentProcessID)) {
                    deleteProcessInstancesByBunch(parentProcessID, DELETE_PROCESS_BUNCH_SIZE, processesAllowedToBeDeletedIds);
                } else {
                    LOGGER.log(Level.WARNING, "Process with ID " + processId + " cannot be deleted without also deleting its parent (" + parentProcessID + ").");
                }
            }
        } while (numberOfDeletedArchivedProcessInstances >= bunchSize);
    }

    /**
     * Delete process instances by bunch for a given processId
     * 
     * @throws ProcessDefinitionNotFoundException
     * @throws DeletionException
     */
    public void deleteProcessInstancesByBunch(final long processId, final int bunchSize, final List<Long> processesAllowedToBeDeletedIds)
            throws DeletionException, ProcessDefinitionNotFoundException {
        long numberOfDeletedProcessInstances = 0;
        do {
            try {
                numberOfDeletedProcessInstances = processAPI.deleteProcessInstances(processId, 0, bunchSize);
            } catch (final ProcessInstanceHierarchicalDeletionException e) {
                final long parentProcessInstanceID = e.getProcessInstanceId();
                final long parentProcessID = processAPI.getProcessDefinitionIdFromProcessInstanceId(parentProcessInstanceID);
                if (processesAllowedToBeDeletedIds.contains(parentProcessID)) {
                    deleteProcessInstancesByBunch(parentProcessID, DELETE_PROCESS_BUNCH_SIZE, processesAllowedToBeDeletedIds);
                } else {
                    LOGGER.log(Level.WARNING, "Process with ID " + processId + " cannot be deleted without also deleting its parent (" + parentProcessID + ").");
                }
            }
        } while (numberOfDeletedProcessInstances >= bunchSize);
    }

    public SearchResult<ProcessDeploymentInfo> searchProcessDefinitions(final SearchOptions searchOptions) {
        try {
            return processAPI.searchProcessDeploymentInfos(searchOptions);
        } catch (final Exception e) {
            throw new APIException("Error when searching process definition", e);
        }
    }

    public SearchResult<ProcessDeploymentInfo> searchUncategorizedProcessDefinitionsSupervisedBy(final long userId, final SearchOptions searchOptions) {
        try {
            return processAPI.searchUncategorizedProcessDeploymentInfosSupervisedBy(userId, searchOptions);
        } catch (final Exception e) {
            throw new APIException("Error when searching uncategorized process definition supervised by user " + userId, e);
        }
    }

    public SearchResult<ProcessDeploymentInfo> searchProcessDefinitionsSupervisedBy(final long userId, final SearchOptions searchOptions) {
        try {
            return processAPI.searchUncategorizedProcessDeploymentInfosSupervisedBy(userId, searchOptions);
        } catch (final Exception e) {
            throw new APIException("Error when searching process definition supervised by user " + userId, e);
        }
    }

    public SearchResult<ProcessDeploymentInfo> searchUncategorizedProcessDefinitionsUserCanStart(final long userId, final SearchOptions searchOptions) {
        try {
            return processAPI.searchUncategorizedProcessDeploymentInfosUserCanStart(userId, searchOptions);
        } catch (final Exception e) {
            throw new APIException("Error when searching uncategorized process definition which can be started by user " + userId, e);
        }
    }

    public SearchResult<ProcessDeploymentInfo> searchRecentlyStartedProcessDefinitions(final long userId, final SearchOptions searchOptions) {
        try {
            return processAPI.searchProcessDeploymentInfosStartedBy(userId, searchOptions);
        } catch (final Exception e) {
            throw new APIException("Error when searching recently started process by user " + userId, e);
        }
    }

    public long countResolvedProcesses() {
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 0);
        builder.filter(ProcessDeploymentInfoSearchDescriptor.CONFIGURATION_STATE, ProcessItem.VALUE_CONFIGURATION_STATE_RESOLVED);
        try {
            return processAPI.searchProcessDeploymentInfos(builder.done()).getCount();
        } catch (final Exception e) {
            throw new APIException("Error when counting resolved processes", e);
        }
    }

    public SearchResult<ProcessDeploymentInfo> searchProcessDeploymentInfos(final long userId, final SearchOptions searchOptions) {
        try {
            return processAPI.searchProcessDeploymentInfos(userId, searchOptions);
        } catch (final SearchException e) {
            throw new APIException("Error when searching process user can start", e);
        }
    }
}
