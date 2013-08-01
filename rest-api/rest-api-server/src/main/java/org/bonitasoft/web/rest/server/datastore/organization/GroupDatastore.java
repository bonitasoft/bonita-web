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
import static org.bonitasoft.web.toolkit.client.data.APIID.toLongList;

import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.identity.Group;
import org.bonitasoft.engine.identity.GroupCreator;
import org.bonitasoft.engine.identity.GroupSearchDescriptor;
import org.bonitasoft.engine.identity.GroupUpdater;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.GroupEngineClient;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasAdd;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasDelete;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasUpdate;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.rest.server.framework.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Nicolas Tith
 * 
 */
public class GroupDatastore extends CommonDatastore<GroupItem, Group> implements 
        DatastoreHasAdd<GroupItem>, 
        DatastoreHasUpdate<GroupItem>,
        DatastoreHasGet<GroupItem>,
        DatastoreHasSearch<GroupItem>, DatastoreHasDelete {

    private EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor());
    
    public GroupDatastore(final APISession engineSession) {
        super(engineSession);
    }

    private GroupEngineClient getGroupEngineClient() {
        return engineClientFactory.createGroupEngineClient(getEngineSession()); 
    }
    
    @Override
    public void delete(final List<APIID> ids) {
        getGroupEngineClient().delete(toLongList(ids));
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

            return new ItemSearchResult<GroupItem>(page, resultsByPage, engineSearchResults.getCount(), 
                    new GroupItemConverter().convert(engineSearchResults.getResult()));

        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public GroupItem get(final APIID id) {
        Group result = getGroupEngineClient().get(id.toLong());
        return new GroupItemConverter().convert(result);
    }

    @Override
    public GroupItem update(final APIID id, final Map<String, String> attributes) {
        GroupUpdater updater = new GroupUpdaterConverter(getGroupEngineClient()).convert(attributes);
        Group group = getGroupEngineClient().update(id.toLong(), updater);
        return new GroupItemConverter().convert(group);
    }

    @Override
    public GroupItem add(final GroupItem group) {
        GroupCreator creator = new GroupCreatorConverter(getGroupEngineClient()).convert(group);
        try {
            final Group result = TenantAPIAccessor.getIdentityAPI(getEngineSession()).createGroup(creator);
            return new GroupItemConverter().convert(result);
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
       throw new RuntimeException("Unimplemented method");
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
            Group group = getGroupEngineClient().get(Long.parseLong(item.getParentGroupId()));
            builder.setParentPath(group.getPath());
        }
        return builder;
    }
}
