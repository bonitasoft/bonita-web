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
package org.bonitasoft.console.server.api.bpm.cases;

import java.util.List;
import java.util.Map;

import org.bonitasoft.console.server.api.ConsoleAPI;
import org.bonitasoft.console.server.datastore.bpm.cases.ArchivedCommentDatastore;
import org.bonitasoft.console.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.rest.api.model.bpm.cases.ArchivedCommentDefinition;
import org.bonitasoft.web.rest.api.model.bpm.cases.ArchivedCommentItem;
import org.bonitasoft.web.rest.api.model.bpm.cases.CommentItem;
import org.bonitasoft.web.rest.api.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.api.APIHasSearch;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Nicolas Tith
 * 
 */
public class APIArchivedComment extends ConsoleAPI<ArchivedCommentItem> implements APIHasSearch<ArchivedCommentItem> {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(ArchivedCommentDefinition.TOKEN);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // C.R.U.D.S
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemSearchResult<ArchivedCommentItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        return getDatastore().search(page, resultsByPage, search, orders, filters);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArchivedCommentDatastore getDatastore() {
        return new ArchivedCommentDatastore(getEngineSession());
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }

    @Override
    protected void fillDeploys(final ArchivedCommentItem item, final List<String> deploys) {
        if (isDeployable(ArchivedCommentItem.ATTRIBUTE_USER_ID, deploys, item)) {
            item.setDeploy(ArchivedCommentItem.ATTRIBUTE_USER_ID,
                    new UserDatastore(getEngineSession()).get(item.getUserId()));
        } else {
            item.setDeploy(CommentItem.ATTRIBUTE_USER_ID, getSystemUser());
        }

        // TODO: Deploy process instance
    }

    private UserItem getSystemUser() {
        final UserItem systemUser = new UserItem();
        systemUser.setUserName("System");
        systemUser.setIcon(UserItem.DEFAULT_USER_ICON);
        return systemUser;
    }

    @Override
    protected void fillCounters(final ArchivedCommentItem item, final List<String> counters) {
    }

}
