/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.bpm.process;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceState;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.api.deployer.UserDeployer;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CaseDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.process.ProcessDatastore;
import org.bonitasoft.web.rest.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.api.APIHasUpdate;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Nicolas Tith
 * @author Celine Souchet
 */
public class APIProcess extends ConsoleAPI<ProcessItem> implements
APIHasAdd<ProcessItem>,
APIHasUpdate<ProcessItem>,
APIHasGet<ProcessItem>,
APIHasSearch<ProcessItem>,
APIHasDelete
{

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected ItemDefinition<ProcessItem> defineItemDefinition() {
        return ProcessDefinition.get();
    }

    @Override
    public String defineDefaultSearchOrder() {
        return ProcessItem.ATTRIBUTE_NAME + " ASC";
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new ProcessDatastore(getEngineSession());
    }

    @Override
    public ProcessItem add(final ProcessItem item) {
        // Finish the upload of the icon
        if (item.getIcon() != null && !item.getIcon().isEmpty()) {
            item.setIcon(uploadIcon(item.getIcon()));
        }

        return getProcessDatastore().add(item);
    }

    @Override
    public ProcessItem update(final APIID id, final Map<String, String> attributes) {
        if (attributes != null) {
            // Finish the upload of the icon
            final String icon = attributes.get(ProcessItem.ATTRIBUTE_ICON);
            if (!MapUtil.removeIfBlank(attributes, ProcessItem.ATTRIBUTE_ICON)) {
                deleteOldIconFile(id);
                // Upload new icon file
                attributes.put(ProcessItem.ATTRIBUTE_ICON, uploadIcon(icon));
            }
        }
        // Update
        return getProcessDatastore().update(id, attributes);
    }

    void deleteOldIconFile(final APIID id) {
        final ProcessItem item = getProcessDatastore().get(id);
        if (item.getIcon() != null || !item.getIcon().isEmpty()) {
            new File(getWebBonitaConstantsUtils().getConsoleDefaultIconsFolder().getPath() + item.getIcon()).delete();
        }
    }

    WebBonitaConstantsUtils getWebBonitaConstantsUtils() {
        return WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId());
    }

    @Override
    public ProcessItem get(final APIID id) {
        final ProcessItem item = getProcessDatastore().get(id);
        if (item != null) {
            final String iconPath = item.getIcon();
            if (iconPath == null || iconPath.isEmpty()) {
                item.setIcon("icons/default/process.png");
            }
        }
        return item;
    }

    @Override
    public ItemSearchResult<ProcessItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        if (filters.containsKey(ProcessItem.FILTER_TEAM_MANAGER_ID) && filters.containsKey(ProcessItem.FILTER_SUPERVISOR_ID)) {
            throw new APIException("Can't set those filters at the same time : " + ProcessItem.FILTER_TEAM_MANAGER_ID + " and "
                    + ProcessItem.FILTER_SUPERVISOR_ID);
        }

        return getProcessDatastore().search(page, resultsByPage, search, orders, filters);
    }

    @Override
    public void delete(final List<APIID> ids) {
        getProcessDatastore().delete(ids);
    }

    @Override
    protected void fillDeploys(final ProcessItem item, final List<String> deploys) {
        addDeployer(new UserDeployer(
                new UserDatastore(getEngineSession()), ProcessItem.ATTRIBUTE_DEPLOYED_BY_USER_ID));
        super.fillDeploys(item, deploys);
    }

    @Override
    protected void fillCounters(final ProcessItem item, final List<String> counters) {
        fillNumberOfFailedCasesIfFailedCounterExists(item, counters);
        fillNumberOfOpenCasesIfOpenCounterExists(item, counters);
    }

    private void fillNumberOfFailedCasesIfFailedCounterExists(final ProcessItem item, final List<String> counters) {
        if (counters.contains(ProcessItem.COUNTER_FAILED_CASES)) {
            final Map<String, String> filters = new HashMap<String, String>();
            filters.put(CaseItem.FILTER_CALLER, "any");
            filters.put(CaseItem.ATTRIBUTE_PROCESS_ID, item.getId().toString());
            filters.put(CaseItem.FILTER_STATE, ProcessInstanceState.ERROR.name());
            item.setAttribute(ProcessItem.COUNTER_FAILED_CASES, getCaseDatastore().count(null, null, filters));
        }
    }

    private void fillNumberOfOpenCasesIfOpenCounterExists(final ProcessItem item, final List<String> counters) {
        if (counters.contains(ProcessItem.COUNTER_OPEN_CASES)) {
            // Open is all states without the terminal states
            final Map<String, String> filters = new HashMap<String, String>();
            filters.put(CaseItem.FILTER_CALLER, "any");
            filters.put(CaseItem.ATTRIBUTE_PROCESS_ID, item.getId().toString());
            item.setAttribute(ProcessItem.COUNTER_OPEN_CASES, getCaseDatastore().count(null, null, filters));
        }
    }

    String uploadIcon(final String iconTempPath) {
        String completeIconTempPath;
        try {
            completeIconTempPath = getCompleteTempFilePath(iconTempPath);
        } catch (final UnauthorizedFolderException e) {
            throw new APIForbiddenException(e.getMessage());
        } catch (final IOException e) {
            throw new APIException(e);
        }

        final String path = uploadAutoRename(
                ProcessItem.ATTRIBUTE_ICON,
                completeIconTempPath,
                getWebBonitaConstantsUtils().getConsoleUserIconsFolder().getPath())
                .getPath();

        return path.substring(getWebBonitaConstantsUtils().getConsoleDefaultIconsFolder().getPath().length());
    }

    protected ProcessDatastore getProcessDatastore() {
        return new ProcessDatastore(getEngineSession());
    }

    protected CaseDatastore getCaseDatastore() {
        return new CaseDatastore(getEngineSession());
    }

}
