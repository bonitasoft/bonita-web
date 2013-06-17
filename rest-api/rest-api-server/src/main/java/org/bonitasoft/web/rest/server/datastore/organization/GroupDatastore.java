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
package org.bonitasoft.web.rest.server.datastore.organization;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;
import static org.bonitasoft.web.toolkit.client.common.util.StringUtil.isBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.identity.Group;
import org.bonitasoft.engine.identity.GroupCreator;
import org.bonitasoft.engine.identity.GroupNotFoundException;
import org.bonitasoft.engine.identity.GroupSearchDescriptor;
import org.bonitasoft.engine.identity.GroupUpdater;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.api.model.identity.GroupItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasAdd;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasDelete;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasGet;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasSearch;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasUpdate;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Nicolas Tith
 * 
 */
public class GroupDatastore extends CommonDatastore<GroupItem, Group> implements 
        DatastoreHasAdd<GroupItem>, 
        DatastoreHasUpdate<GroupItem>,
        DatastoreHasGet<GroupItem>,
        DatastoreHasSearch<GroupItem>, DatastoreHasDelete {

    /**
     * Default Constructor.
     * 
     * @param engineSession
     */
    public GroupDatastore(final APISession engineSession) {
        super(engineSession);
    }

    /*
     * Delete group(s)
     */
    @Override
    public void delete(final List<APIID> ids) {
        try {
            final IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(getEngineSession());
            identityAPI.deleteGroups(APIID.toLongList(ids));
        } catch (final Exception e) {
            throw new APIException(e);
        }

    }

    @Override
    public ItemSearchResult<GroupItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        try {
            final SearchOptionsBuilder builder = SearchOptionsBuilderUtil.buildSearchOptions(page, resultsByPage, orders, search);

            addFilterToSearchBuilder(filters, builder, GroupItem.ATTRIBUTE_NAME, GroupSearchDescriptor.NAME);
            addFilterToSearchBuilder(filters, builder, GroupItem.ATTRIBUTE_DISPLAY_NAME, GroupSearchDescriptor.DISPLAY_NAME);

            SearchResult<Group> engineSearchResults;
            engineSearchResults = TenantAPIAccessor.getIdentityAPI(getEngineSession()).searchGroups(builder.done());

            final List<GroupItem> consoleSearchResults = new ArrayList<GroupItem>();
            for (final Group engineItem : engineSearchResults.getResult()) {
                consoleSearchResults.add(convertEngineToConsoleItem(engineItem));
            }

            return new ItemSearchResult<GroupItem>(page, resultsByPage, engineSearchResults.getCount(), consoleSearchResults);

        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public GroupItem get(final APIID id) {
        try {
            final Group result = TenantAPIAccessor.getIdentityAPI(getEngineSession()).getGroup(id.toLong());
            return convertEngineToConsoleItem(result);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public GroupItem update(final APIID id, final Map<String, String> attributes) {
        final GroupUpdater updater = new GroupUpdater();
        if (attributes.containsKey(GroupItem.ATTRIBUTE_DESCRIPTION)) {
            updater.updateDescription(attributes.get(GroupItem.ATTRIBUTE_DESCRIPTION));
        }
        if (attributes.containsKey(GroupItem.ATTRIBUTE_PARENT_PATH)) {
            updater.updateParentPath(attributes.get(GroupItem.ATTRIBUTE_PARENT_PATH));
        }
        if (attributes.containsKey(GroupItem.ATTRIBUTE_PARENT_GROUP_ID)) {
            updater.updateParentPath(attributes.get(GroupItem.ATTRIBUTE_PARENT_GROUP_ID));
        }
        if (attributes.containsKey(GroupItem.ATTRIBUTE_ICON)) {
            updater.updateIconPath(attributes.get(GroupItem.ATTRIBUTE_ICON));
        }
        if (attributes.containsKey(GroupItem.ATTRIBUTE_NAME)) {
            updater.updateName(attributes.get(GroupItem.ATTRIBUTE_NAME));
        }

        try {
            final Group result = TenantAPIAccessor.getIdentityAPI(getEngineSession()).updateGroup(id.toLong(), updater);
            return convertEngineToConsoleItem(result);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public GroupItem add(final GroupItem group) {
        try {
            final Group result = TenantAPIAccessor.getIdentityAPI(getEngineSession()).createGroup(createGroupCreator(group));
            return convertEngineToConsoleItem(result);
        } catch (final AlreadyExistsException e) {
            String message = _("Can't create group. Group '%groupName%' already exists", new Arg("groupName", group.getName()));
            throw new APIForbiddenException(message, e);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COUNTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Long getNumberOfUsers(final APIID groupId) {
        try {
            return TenantAPIAccessor.getIdentityAPI(getEngineSession()).getNumberOfUsersInGroup(groupId.toLong());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONVERTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected GroupItem convertEngineToConsoleItem(final Group group) {
        final GroupItem groupItem = new GroupItem();
        groupItem.setCreatedByUserId(group.getCreatedBy());
        groupItem.setCreationDate(group.getCreationDate());
        groupItem.setDescription(group.getDescription());
        groupItem.setDisplayName(group.getDisplayName());
        groupItem.setIcon(group.getIconPath());
        groupItem.setId(group.getId());
        groupItem.setLastUpdateDate(group.getLastUpdate());
        groupItem.setName(group.getName());
        groupItem.setParentPath(group.getParentPath());
        groupItem.setParentGroupId(group.getPath());
        return groupItem;
    }

    protected final GroupCreator createGroupCreator(final GroupItem item) {
        if (item == null) {
            return null;
        }

        GroupCreator builder = new GroupCreator(item.getName());

        if (!isBlank(item.getDescription())) {
            builder.setDescription(item.getDescription());
        }

        if (!isBlank(item.getDisplayName())) {
            builder.setDisplayName(item.getDisplayName());
        }

        if (!isBlank(item.getIcon())) {
            builder.setIconName(item.getIcon());
            builder.setIconPath(item.getIcon());
        }
        
        if (!isBlank(item.getParentGroupId())) {
            try {
                Group group = TenantAPIAccessor.getIdentityAPI(getEngineSession()).getGroup(Long.parseLong(item.getParentGroupId()));
                if (group.getParentPath() == null) {
                    builder.setParentPath("/"+group.getName());
                } else {
                    builder.setParentPath(group.getParentPath()+"/"+group.getName());
                }
            } catch (GroupNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final Exception e) {
                throw new APIException(e);
            }
        }

        return builder;
    }
}
