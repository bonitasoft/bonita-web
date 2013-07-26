/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.datastore.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.engine.identity.UserSearchDescriptor;
import org.bonitasoft.engine.identity.UserUpdater;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.datastore.Sort;
import org.bonitasoft.web.rest.server.datastore.Sorts;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.UserEngineClient;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.rest.server.framework.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel, Colin PUY
 */
public class UserDatastore extends CommonDatastore<UserItem, User> implements DatastoreHasGet<UserItem> {

    private EngineClientFactory engineClientFactory = new EngineClientFactory(new EngineAPIAccessor());
    
    public UserDatastore(final APISession engineSession) {
        super(engineSession);
    }

    public UserItem add(final UserItem user) {
        UserCreator userCreator = new UserCreatorConverter().convert(user);
        User createdUser = getUserEngineClient().create(userCreator);
        return convertEngineToConsoleItem(createdUser);
    }

    public UserItem update(final APIID id, final Map<String, String> attributes) {
        UserUpdater userUpdater = new UserUpdaterConverter().convert(attributes);
        User user = getUserEngineClient().update(id.toLong(), userUpdater);
        return convertEngineToConsoleItem(user);
    }
    
    public UserItem get(final APIID id) {
        User user = getUserEngineClient().get(id.toLong());
        return convertEngineToConsoleItem(user);
    }

    public void delete(final List<APIID> ids) {
        getUserEngineClient().delete(APIID.toLongList(ids));
    }

    // protected for tests
    protected UserEngineClient getUserEngineClient() {
        return engineClientFactory.createUserEngineClient(getEngineSession());
    }
    
    public ItemSearchResult<UserItem> search(final int page, final int resultsByPage, final String search,
            final Map<String, String> filters, final String orders) {

        SearchOptionsBuilder builder = SearchOptionsBuilderUtil.buildSearchOptions(page, resultsByPage, null, null);
        addSorts(builder, orders);
        addSearchTerm(builder, search);
        addFilters(builder, filters);

        SearchResult<User> engineSearchResults;
        engineSearchResults = getUserEngineClient().search(builder.done());
        final List<UserItem> consoleSearchResults = new ArrayList<UserItem>();
        for (final User engineItem : engineSearchResults.getResult()) {
            consoleSearchResults.add(convertEngineToConsoleItem(engineItem));
        }

        return new ItemSearchResult<UserItem>(page, resultsByPage, engineSearchResults.getCount(), consoleSearchResults);
    }

    private void addFilters(SearchOptionsBuilder builder, final Map<String, String> filters) {
        addFilterToSearchBuilder(filters, builder, UserItem.ATTRIBUTE_FIRSTNAME, UserSearchDescriptor.FIRST_NAME);
        addFilterToSearchBuilder(filters, builder, UserItem.ATTRIBUTE_LASTNAME, UserSearchDescriptor.LAST_NAME);
        addFilterToSearchBuilder(filters, builder, UserItem.ATTRIBUTE_USERNAME, UserSearchDescriptor.USER_NAME);
        addFilterToSearchBuilder(filters, builder, UserItem.ATTRIBUTE_MANAGER_ID, UserSearchDescriptor.MANAGER_USER_ID);
        addFilterToSearchBuilder(filters, builder, UserItem.FILTER_ROLE_ID, UserSearchDescriptor.ROLE_ID);
        addFilterToSearchBuilder(filters, builder, UserItem.FILTER_GROUP_ID, UserSearchDescriptor.GROUP_ID);
    }

    private void addSearchTerm(final SearchOptionsBuilder builder, final String search) {
        if (search != null && !search.isEmpty()) {
            builder.searchTerm(search);
        }
    }

    private void addSorts(final SearchOptionsBuilder builder, final String orders) {
        if (orders != null) {
            final List<Sort> sorts = new Sorts(orders, new UserSearchAttributeConverter()).asList();
            addSortsToBuilder(sorts, builder);
        }
    }

    private void addSortsToBuilder(final List<Sort> sorts, final SearchOptionsBuilder builder) {
        for (Sort sort : sorts) {
            builder.sort(sort.getField(), sort.getOrder());
        }
    }

    @Override
    protected UserItem convertEngineToConsoleItem(final User user) {
        if (user == null) {
            return null;
        }

        final UserItem result = new UserItem();
        result.setId(APIID.makeAPIID(user.getId()));
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setPassword(user.getPassword());
        result.setUserName(user.getUserName());
        result.setManagerId(user.getManagerUserId());

        // Add default icon if icon if empty
        final String iconPath = user.getIconPath();
        result.setIcon(iconPath == null || iconPath.isEmpty() ? UserItem.DEFAULT_USER_ICON : iconPath);

        result.setCreationDate(user.getCreationDate());
        result.setCreatedByUserId(user.getCreatedBy());
        result.setLastUpdateDate(user.getLastUpdate());
        result.setLastConnectionDate(user.getLastConnection());
        result.setTitle(user.getTitle());
        result.setJobTitle(user.getJobTitle());

        result.setState(UserItem.VALUE_ACTIVATION_STATE_ENABLED);

        return result;
    }

    protected IdentityAPI getIdentityAPI() {
        try {
            return TenantAPIAccessor.getIdentityAPI(getEngineSession());
        } catch (Exception e) {
            throw new APIException(e);
        }
    }
}
