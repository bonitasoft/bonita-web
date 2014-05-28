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
package org.bonitasoft.web.rest.server.api.bpm.process;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.api.deployer.UserDeployer;
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
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Nicolas Tith
 * 
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

                // Delete old icon file
                final ProcessItem item = getProcessDatastore().get(id);
                if (item.getIcon() != null || !item.getIcon().isEmpty()) {
                    new File(
                            WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId()).getConsoleDefaultIconsFolder().getPath()
                                    + item.getIcon())
                            .delete();
                }

                // Upload new icon file
                attributes.put(ProcessItem.ATTRIBUTE_ICON, uploadIcon(icon));
            }
        }
        // Update
        return getProcessDatastore().update(id, attributes);
    }

    @Override
    public ProcessItem get(final APIID id) {
        final ProcessItem item = getProcessDatastore().get(id);
        if (item != null) {
            final String iconPath = item.getIcon();
            if (iconPath == null || iconPath.isEmpty()) {
                item.setIcon("/default/process.png");
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

    }

    String uploadIcon(final String iconTempPath) {
        final String path = uploadAutoRename(
                ProcessItem.ATTRIBUTE_ICON,
                iconTempPath,
                WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId()).getConsoleUserIconsFolder().getPath())
                .getPath();

        return path.substring(WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId()).getConsoleDefaultIconsFolder().getPath().length());
    }

    protected ProcessDatastore getProcessDatastore() {
        return new ProcessDatastore(getEngineSession());

    }

}
