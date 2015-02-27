package org.bonitasoft.console.common.server.form;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;

public class ProcessFormService {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(ProcessFormService.class.getName());

    /**
     * @param apiSession
     * @param processName
     * @param processVersion
     * @param taskName
     * @param isInstance indicate if the form is an instance form (to differenciate the process instantiation form from the process instance overview)
     * @return
     * @throws BonitaException
     */
    public FormReference getForm(final APISession apiSession, final long processDefinitionID, final String taskName, final boolean isInstance)
            throws BonitaException {
        // TODO retrieve mapping from engine
        // Mock
        if ("request".equals(taskName)) {
            return new FormReference("custompage_form", false);
        } else {
            return new FormReference("/form.html", true);
        }
    }

    public long getProcessDefinitionID(final APISession apiSession, final String processName, final String processVersion) throws BonitaException {
        if (processName != null && processVersion != null) {
            try {
                return getProcessAPI(apiSession).getProcessDefinitionId(processName, processVersion);
            } catch (final ProcessDefinitionNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Wrong parameters for process name and version", e);
                }
            }
        }
        return -1;
    }

    public long getTaskInstanceID(final APISession apiSession, final long processInstanceID, final String taskName, final long taskInstanceID, final long userID)
            throws BonitaException {
        if (taskInstanceID != -1) {
            return taskInstanceID;
        } else {
            final ProcessAPI processAPI = getProcessAPI(apiSession);
            long filteredUserID;
            if (userID != -1) {
                filteredUserID = userID;
            } else {
                filteredUserID = apiSession.getId();
            }
            final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 1);
            searchOptionsBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, processInstanceID);
            searchOptionsBuilder.filter(HumanTaskInstanceSearchDescriptor.NAME, taskName);
            final SearchResult<HumanTaskInstance> searchMyAvailableHumanTasks = processAPI.searchMyAvailableHumanTasks(filteredUserID,
                    searchOptionsBuilder.done());
            if (searchMyAvailableHumanTasks.getCount() > 0) {
                return searchMyAvailableHumanTasks.getResult().get(0).getId();
            } else {
                final SearchOptionsBuilder archivedSearchOptionsBuilder = new SearchOptionsBuilder(0, 1);
                archivedSearchOptionsBuilder.filter(ArchivedHumanTaskInstanceSearchDescriptor.ASSIGNEE_ID, filteredUserID);
                archivedSearchOptionsBuilder.filter(ArchivedHumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, processInstanceID);
                archivedSearchOptionsBuilder.filter(ArchivedHumanTaskInstanceSearchDescriptor.NAME, taskName);
                final SearchResult<ArchivedHumanTaskInstance> searchArchivedHumanTasks = processAPI.searchArchivedHumanTasks(archivedSearchOptionsBuilder
                        .done());
                if (searchArchivedHumanTasks.getCount() > 0) {
                    return searchArchivedHumanTasks.getResult().get(0).getSourceObjectId();
                }
            }
        }
        return -1;
    }

    public String ensureTaskName(final APISession apiSession, final String taskName, final long taskInstanceID) throws BonitaException {
        if (taskName != null) {
            return taskName;
        } else if (taskInstanceID != -1) {
            try {
                final ActivityInstance activity = getProcessAPI(apiSession).getActivityInstance(taskInstanceID);
                return activity.getName();
            } catch (final ActivityInstanceNotFoundException e) {
                final ArchivedActivityInstance activity = getProcessAPI(apiSession).getArchivedActivityInstance(taskInstanceID);
                return activity.getName();
            }
        }
        return null;
    }

    protected ProcessAPI getProcessAPI(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return TenantAPIAccessor.getProcessAPI(apiSession);
    }

}
