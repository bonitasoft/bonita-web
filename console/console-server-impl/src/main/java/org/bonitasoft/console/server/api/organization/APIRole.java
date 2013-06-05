package org.bonitasoft.console.server.api.organization;

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.console.client.model.identity.RoleDefinition;
import org.bonitasoft.console.client.model.identity.RoleItem;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.server.api.ConsoleAPI;
import org.bonitasoft.console.server.datastore.organization.RoleDatastore;
import org.bonitasoft.console.server.datastore.organization.UserDatastore;
import org.bonitasoft.engine.identity.RoleCriterion;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.api.APIHasAdd;
import org.bonitasoft.web.toolkit.server.api.APIHasDelete;
import org.bonitasoft.web.toolkit.server.api.APIHasFiles;
import org.bonitasoft.web.toolkit.server.api.APIHasGet;
import org.bonitasoft.web.toolkit.server.api.APIHasSearch;
import org.bonitasoft.web.toolkit.server.api.APIHasUpdate;
import org.bonitasoft.web.toolkit.server.api.Datastore;

public class APIRole extends ConsoleAPI<RoleItem> implements
        APIHasGet<RoleItem>,
        APIHasSearch<RoleItem>,
        APIHasUpdate<RoleItem>,
        APIHasAdd<RoleItem>,
        APIHasDelete,
        APIHasFiles {

    private static final String ROLES_ICON_FOLDER_PATH = "/" + WebBonitaConstants.ROLES_ICONS_FOLDER_NAME;

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(RoleDefinition.TOKEN);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void fillDeploys(final RoleItem item, final List<String> deploys) {
        if (isDeployable(RoleItem.ATTRIBUTE_CREATED_BY_USER_ID, deploys, item)) {
            item.setDeploy(RoleItem.ATTRIBUTE_CREATED_BY_USER_ID, new UserDatastore(getEngineSession()).get(item.getCreatedByUserId()));
        }
    }

    @Override
    protected void fillCounters(final RoleItem item, final List<String> counters) {
        if (counters.contains(RoleItem.COUNTER_NUMBER_OF_USERS)) {
            item.setAttribute(RoleItem.COUNTER_NUMBER_OF_USERS,
                    ((RoleDatastore) defineDefaultDatastore()).getNumberOfUsers(item.getId()));
        }
    }

    @Override
    protected List<String> defineReadOnlyAttributes() {
        return Arrays.asList(
                RoleItem.ATTRIBUTE_CREATED_BY_USER_ID,
                RoleItem.ATTRIBUTE_CREATION_DATE,
                RoleItem.ATTRIBUTE_LAST_UPDATE_DATE
                );
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new RoleDatastore(getEngineSession());
    }

    @Override
    public String defineDefaultSearchOrder() {
        return RoleCriterion.DISPLAY_NAME_ASC.name();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // APIHasFiles
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getUploadPath(final String attributeName) {
        if (RoleItem.ATTRIBUTE_ICON.equals(attributeName)) {
            return getWebBonitaConstantsUtilsInstance().getConsoleRoleIconsFolder().getPath();
        }
        return null;
    }

    @Override
    public String getSavedPathPrefix(final String attributeName) {
        if (RoleItem.ATTRIBUTE_ICON.equals(attributeName)) {
            return ROLES_ICON_FOLDER_PATH;
        }
        return null;
    }

    private WebBonitaConstantsUtils getWebBonitaConstantsUtilsInstance() {
        return WebBonitaConstantsUtils.getInstance(getEngineSession().getTenantId());
    }
}
