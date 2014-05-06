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
package org.bonitasoft.web.rest.server.datastore.bpm.process;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.FormsResourcesUtils;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveFactory;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoUpdater;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.process.helper.ProcessItemConverter;
import org.bonitasoft.web.rest.server.datastore.bpm.process.helper.SearchProcessHelper;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.ProcessEngineClient;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasAdd;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasDelete;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasUpdate;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * Process data store
 * 
 * @author Vincent Elcrin
 * 
 */
public class ProcessDatastore extends CommonDatastore<ProcessItem, ProcessDeploymentInfo> implements
        DatastoreHasAdd<ProcessItem>,
        DatastoreHasUpdate<ProcessItem>,
        DatastoreHasGet<ProcessItem>,
        DatastoreHasSearch<ProcessItem>,
        DatastoreHasDelete
{

    /**
     * process file
     */
    private static final String FILE_UPLOAD = "fileupload";

    public ProcessDatastore(final APISession engineSession) {
        super(engineSession);
    }

    @Override
    public ProcessItem add(final ProcessItem process) {
        final ProcessEngineClient engineClient = getProcessEngineClient();
        final BusinessArchive businessArchive = readBusinessArchive(new File(process.getAttributes().get(FILE_UPLOAD)));
        final ProcessDefinition deployedArchive = engineClient.deploy(businessArchive);
        final ProcessDeploymentInfo processDeploymentInfo = engineClient.getProcessDeploymentInfo(deployedArchive.getId());

        try {
            FormsResourcesUtils.retrieveApplicationFiles(
                    getEngineSession(),
                    processDeploymentInfo.getProcessId(),
                    processDeploymentInfo.getDeploymentDate());
        } catch (IOException e) {
            throw new APIException("", e);
        } catch (ProcessDefinitionNotFoundException e) {
            throw new APIException("", e);
        } catch (BPMEngineException e) {
            throw new APIException("", e);
        }

        return convertEngineToConsoleItem(processDeploymentInfo);
    }

    /*
     * Overridden in SP
     */
    protected BusinessArchive readBusinessArchive(final File file) {
        try {
            return BusinessArchiveFactory.readBusinessArchive(file);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public ProcessItem update(final APIID id, final Map<String, String> attributes) {
        final ProcessDeploymentInfoUpdater updater = new ProcessDeploymentInfoUpdater();

        final ProcessEngineClient engineClient = getProcessEngineClient();
        if (attributes.containsKey(ProcessItem.ATTRIBUTE_DISPLAY_DESCRIPTION)) {
            updater.setDisplayDescription(attributes.get(ProcessItem.ATTRIBUTE_DISPLAY_DESCRIPTION));
        }

        if (attributes.containsKey(ProcessItem.ATTRIBUTE_DISPLAY_NAME)) {
            updater.setDisplayName(attributes.get(ProcessItem.ATTRIBUTE_DISPLAY_NAME));
        }

        if (attributes.containsKey(ProcessItem.ATTRIBUTE_ICON)) {
            updater.setIconPath(attributes.get(ProcessItem.ATTRIBUTE_ICON));
        }

        // specific engine methods
        if (attributes.containsKey(ProcessItem.ATTRIBUTE_ACTIVATION_STATE)) {
            changeProcessState(engineClient, id.toLong(), attributes.get(ProcessItem.ATTRIBUTE_ACTIVATION_STATE));
        }

        if (!updater.getFields().isEmpty()) {
            final ProcessDeploymentInfo processDeploymentInfo = engineClient.updateProcessDeploymentInfo(id.toLong(), updater);
            return convertEngineToConsoleItem(processDeploymentInfo);
        } else {
            return convertEngineToConsoleItem(engineClient.getProcessDeploymentInfo(id.toLong()));
        }
    }

    private void changeProcessState(final ProcessEngineClient engineClient, final Long processId, final String state) {
        if (ProcessItem.VALUE_ACTIVATION_STATE_DISABLED.equals(state)) {
            engineClient.disableProcess(processId);
        } else if (ProcessItem.VALUE_ACTIVATION_STATE_ENABLED.equals(state)) {
            engineClient.enableProcess(processId);
        }
    }

    @Override
    public ProcessItem get(final APIID id) {
        final ProcessEngineClient engineClient = getProcessEngineClient();
        final ProcessDeploymentInfo processDeploymentInfo = engineClient.getProcessDeploymentInfo(id.toLong());
        return convertEngineToConsoleItem(processDeploymentInfo);
    }

    @Override
    public void delete(final List<APIID> ids) {
        final ProcessEngineClient engineClient = getProcessEngineClient();
        engineClient.deleteDisabledProcesses(APIID.toLongList(ids));
    }

    @Override
    public ItemSearchResult<ProcessItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        final ProcessEngineClient engineClient = getProcessEngineClient();
        return new SearchProcessHelper(engineClient).search(page, resultsByPage, search, orders, filters);
    }

    @Override
    protected ProcessItem convertEngineToConsoleItem(final ProcessDeploymentInfo item) {
        if (item != null) {
            return new ProcessItemConverter(getProcessEngineClient().getProcessApi()).convert(item);
        }
        return null;
    }

	private ProcessEngineClient getProcessEngineClient() {
		return getEngineClientFactory().createProcessEngineClient();
	}

    private EngineClientFactory getEngineClientFactory() {
        return new EngineClientFactory(new EngineAPIAccessor(getEngineSession()));
    }
}
