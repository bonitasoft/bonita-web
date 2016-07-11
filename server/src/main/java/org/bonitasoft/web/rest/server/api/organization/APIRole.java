package org.bonitasoft.web.rest.server.api.organization;

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.engine.identity.RoleCriterion;
import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.organization.RoleDatastore;
import org.bonitasoft.web.rest.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.api.APIHasUpdate;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

public class APIRole extends ConsoleAPI<RoleItem> implements
        APIHasGet<RoleItem>,
        APIHasSearch<RoleItem>,
        APIHasUpdate<RoleItem>,
        APIHasAdd<RoleItem>,
        APIHasDelete {

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
                RoleItem.ATTRIBUTE_LAST_UPDATE_DATE);
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new RoleDatastore(getEngineSession());
    }

    @Override
    public String defineDefaultSearchOrder() {
        return RoleCriterion.DISPLAY_NAME_ASC.name();
    }

}
