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
package org.bonitasoft.web.rest.server.api.organization;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.HumanTaskDatastore;
import org.bonitasoft.web.rest.server.datastore.organization.PersonalContactDataDatastore;
import org.bonitasoft.web.rest.server.datastore.organization.ProfessionalContactDataDatastore;
import org.bonitasoft.web.rest.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.api.APIHasUpdate;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidationError;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidationException;

/**
 * @author Séverin Moussel
 * 
 */
// TODO : implements APIhasFile
public class APIUser extends ConsoleAPI<UserItem> implements APIHasAdd<UserItem>, APIHasDelete, APIHasUpdate<UserItem>,
    APIHasGet<UserItem>, APIHasSearch<UserItem> {

    @Override
    protected ItemDefinition<UserItem> defineItemDefinition() {
        return UserDefinition.get();
    }

    @Override
    public String defineDefaultSearchOrder() {
        return UserItem.ATTRIBUTE_LASTNAME;
    }

    @Override
    public UserItem add(final UserItem item) {
        // Finish the upload of the icon
        if (item.getIcon() != null && !item.getIcon().isEmpty()) {
            item.setIcon(uploadIcon(item.getIcon()));
        }
        if (StringUtil.isBlank(item.getPassword())) {
            throw new ValidationException(Arrays.asList(new ValidationError("Password", "%attribute% is mandatory")));
        }
        // Add
        return new UserDatastore(getEngineSession()).add(item);

    }

    @Override
    public UserItem update(final APIID id, final Map<String, String> item) {
        if (item != null) {
            // Finish the upload of the icon
            final String icon = item.get(UserItem.ATTRIBUTE_ICON);
            if (!MapUtil.removeIfBlank(item, UserItem.ATTRIBUTE_ICON)) {

                deleteOldIconFileIfExists(id);
                String newIcon = uploadIcon(icon);
                item.put(UserItem.ATTRIBUTE_ICON, newIcon);
            }

            // Do not update password if not set
            MapUtil.removeIfBlank(item, UserItem.ATTRIBUTE_PASSWORD);
        }
        // Update
        return new UserDatastore(getEngineSession()).update(id, item);
    }

    private void deleteOldIconFileIfExists(final APIID id) {
        final UserItem oldUser = new UserDatastore(getEngineSession()).get(id);
        if (hasIcon(oldUser)) {
            getIconFile(oldUser).delete();
        }
    }

    private File getIconFile(final UserItem oldUser) {
        return new File(WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId())
                .getConsoleDefaultIconsFolder().getPath() + oldUser.getIcon());
    }

    private boolean hasIcon(final UserItem oldUser) {
        return oldUser.getIcon() != null && !oldUser.getIcon().isEmpty() && !oldUser.getIcon().equals(UserItem.DEFAULT_USER_ICON);
    }

    @Override
    public UserItem get(final APIID id) {
        final UserItem item = new UserDatastore(getEngineSession()).get(id);
        if (item != null) {
            // Do not let the password output from the API
            item.setPassword(null);
            final String iconPath = item.getIcon();
            if (iconPath == null || iconPath.isEmpty()) {
                item.setIcon(UserItem.DEFAULT_USER_ICON);
            }
        }

        return item;
    }

    @Override
    public ItemSearchResult<UserItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        final ItemSearchResult<UserItem> results = new UserDatastore(getEngineSession()).search(page, resultsByPage, search, filters, orders);

        for (final UserItem item : results.getResults()) {
            if (item != null) {
                // Do not let the password output from the API
                item.setPassword(null);
            }
        }

        return results;
    }

    @Override
    public void delete(final List<APIID> ids) {
        new UserDatastore(getEngineSession()).delete(ids);
    }

    @Override
    protected void fillDeploys(final UserItem item, final List<String> deploys) {
        if (isDeployable(UserItem.ATTRIBUTE_MANAGER_ID, deploys, item)) {
            item.setDeploy(UserItem.ATTRIBUTE_MANAGER_ID,
                    new UserDatastore(getEngineSession()).get(item.getManagerId()));
        }

        if (isDeployable(UserItem.ATTRIBUTE_CREATED_BY_USER_ID, deploys, item)) {
            item.setDeploy(UserItem.ATTRIBUTE_CREATED_BY_USER_ID,
                    new UserDatastore(getEngineSession()).get(item.getCreatedByUserId()));
        }

        if (deploys.contains(UserItem.DEPLOY_PERSONNAL_DATA)) {
            item.setDeploy(UserItem.DEPLOY_PERSONNAL_DATA,
                    new PersonalContactDataDatastore(getEngineSession()).get(item.getId()));

            // not a real deploy. force attribute to fix json conversion (Item#toJson)
            item.setAttribute(UserItem.DEPLOY_PERSONNAL_DATA, (String) null);
        }

        if (deploys.contains(UserItem.DEPLOY_PROFESSIONAL_DATA)) {
            item.setDeploy(UserItem.DEPLOY_PROFESSIONAL_DATA,
                    new ProfessionalContactDataDatastore(getEngineSession()).get(item.getId()));

            // not a real deploy. force attribute to fix json conversion (Item#toJson)
            item.setAttribute(UserItem.DEPLOY_PROFESSIONAL_DATA, (String) null);
        }

    }

    @Override
    protected void fillCounters(final UserItem item, final List<String> counters) {

        if (counters.contains(UserItem.COUNTER_OPEN_TASKS)) {
            item.setAttribute(UserItem.COUNTER_OPEN_TASKS,
                    new HumanTaskDatastore(getEngineSession()).getNumberOfOpenTasks(item.getId())
                    );
        }

        if (counters.contains(UserItem.COUNTER_OVERDUE_TASKS)) {
            item.setAttribute(UserItem.COUNTER_OVERDUE_TASKS,
                    new HumanTaskDatastore(getEngineSession()).getNumberOfOverdueOpenTasks(item.getId())
                    );
        }
    }

    private String uploadIcon(final String iconTempPath) {
        final String path = uploadAutoRename(
                UserItem.ATTRIBUTE_ICON,
                iconTempPath,
                WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId()).getConsoleUserIconsFolder().getPath())
                .getPath();
        return path.substring(WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId()).getConsoleDefaultIconsFolder().getPath().length());
    }
}
