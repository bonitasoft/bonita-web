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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.engine.identity.UserCriterion;
import org.bonitasoft.engine.identity.UserSearchDescriptor;
import org.bonitasoft.engine.identity.UserUpdater;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.datastore.Sort;
import org.bonitasoft.web.rest.server.datastore.Sorts;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.rest.server.framework.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APISessionInvalidException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 */
public class UserDatastore extends CommonDatastore<UserItem, User>
        implements DatastoreHasGet<UserItem> {

    public UserDatastore(final APISession engineSession) {
        super(engineSession);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // C.R.U.D.
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public UserItem add(final UserItem user) {
        try {
            UserCreator userCreator = convertConsoleToEngineItem(user);
            final User result = getIdentityAPI().createUser(userCreator);
            return convertEngineToConsoleItem(result);
        } catch (final AlreadyExistsException e) {
            String message = _("Can't create user. User '%userName%' already exists", new Arg("userName", user.getUserName()));
            throw new APIForbiddenException(message, e);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    public UserItem update(final APIID id, final Map<String, String> attributes) {
        try {
            final UserUpdater userUpdater = new UserUpdater();

            if (attributes.containsKey(UserItem.ATTRIBUTE_FIRSTNAME)) {
                userUpdater.setFirstName(attributes.get(UserItem.ATTRIBUTE_FIRSTNAME));
            }
            if (attributes.containsKey(UserItem.ATTRIBUTE_LASTNAME)) {
                userUpdater.setLastName(attributes.get(UserItem.ATTRIBUTE_LASTNAME));
            }
            if (attributes.containsKey(UserItem.ATTRIBUTE_PASSWORD)) {
                userUpdater.setPassword(attributes.get(UserItem.ATTRIBUTE_PASSWORD));
            }
            if (attributes.containsKey(UserItem.ATTRIBUTE_USERNAME)) {
                userUpdater.setUserName(attributes.get(UserItem.ATTRIBUTE_USERNAME));
            }
            if (attributes.containsKey(UserItem.ATTRIBUTE_MANAGER_ID)) {
                Long managerId = getManagerId(attributes);
                userUpdater.setManagerId(managerId);
            }
            if (attributes.containsKey(UserItem.ATTRIBUTE_ICON)) {
                userUpdater.setIconPath(attributes.get(UserItem.ATTRIBUTE_ICON));
            }
            if (attributes.containsKey(UserItem.ATTRIBUTE_TITLE)) {
                userUpdater.setTitle(attributes.get(UserItem.ATTRIBUTE_TITLE));
            }
            if (attributes.containsKey(UserItem.ATTRIBUTE_JOB_TITLE)) {
                userUpdater.setJobTitle(attributes.get(UserItem.ATTRIBUTE_JOB_TITLE));
            }

            final User result = getIdentityAPI().updateUser(id.toLong(), userUpdater);
            return convertEngineToConsoleItem(result);

        } catch (final InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (final Throwable e) {
            throw new APIException(e);
        }
    }

    private Long getManagerId(final Map<String, String> attributes) {
        try {
            return Long.valueOf(attributes.get(UserItem.ATTRIBUTE_MANAGER_ID));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public UserItem get(final APIID id) {
        try {
            final User result = getIdentityAPI().getUser(id.toLong());

            return convertEngineToConsoleItem(result);
        } catch (final NotFoundException e) {
            return null;
        } catch (final InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (final Exception e) {
            throw new APIException(e);
        }

    }

    /**
     * Search for users
     * 
     * @param page
     *            The page to display
     * @param resultsByPage
     *            The number of results by page
     * @param search
     *            Search terms
     * @param filters
     *            The filters to doAuthorize. There will be an AND operand between filters.
     * @param orders
     *            The order to doAuthorize to the search
     * @return This method returns an ItemSearch result containing the returned data and information about the total possible results.
     */
    public ItemSearchResult<UserItem> search(final int page, final int resultsByPage, final String search, final Map<String, String> filters,
            final String orders) {

        SearchOptionsBuilder builder = SearchOptionsBuilderUtil.buildSearchOptions(page, resultsByPage, null, null);
        addSorts(builder, orders);
        addSearchTerm(builder, search);
        addFilters(builder, filters);

        try {
            SearchResult<User> engineSearchResults;
            
            if (filters.containsKey(UserItem.FILTER_GROUP_ID)) {
                List<User> users =  getIdentityAPI().getUsersInGroup(Long.valueOf(filters.get(UserItem.FILTER_GROUP_ID)), 
                        SearchOptionsBuilderUtil.computeIndex(page, resultsByPage), resultsByPage, UserCriterion.LAST_NAME_ASC);
                
                final List<UserItem> consoleSearchResults = new ArrayList<UserItem>();
                for (final User engineItem : users) {
                    consoleSearchResults.add(convertEngineToConsoleItem(engineItem));
                }
                return new ItemSearchResult<UserItem>(page, resultsByPage, consoleSearchResults.size(), consoleSearchResults);
            }
            
            engineSearchResults = getIdentityAPI().searchUsers(builder.done());
            final List<UserItem> consoleSearchResults = new ArrayList<UserItem>();
            for (final User engineItem : engineSearchResults.getResult()) {
                consoleSearchResults.add(convertEngineToConsoleItem(engineItem));
            }

            return new ItemSearchResult<UserItem>(page, resultsByPage, engineSearchResults.getCount(), consoleSearchResults);

        } catch (final InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (final Exception e) {
            throw new APIException(e);
        }

    }

    private void addFilters(SearchOptionsBuilder builder, final Map<String, String> filters) {
        addFilterToSearchBuilder(filters, builder, UserItem.ATTRIBUTE_FIRSTNAME, UserSearchDescriptor.FIRST_NAME);
        addFilterToSearchBuilder(filters, builder, UserItem.ATTRIBUTE_LASTNAME, UserSearchDescriptor.LAST_NAME);
        addFilterToSearchBuilder(filters, builder, UserItem.ATTRIBUTE_USERNAME, UserSearchDescriptor.USER_NAME);
        addFilterToSearchBuilder(filters, builder, UserItem.ATTRIBUTE_MANAGER_ID, UserSearchDescriptor.MANAGER_USER_ID);
        addFilterToSearchBuilder(filters, builder, UserItem.FILTER_ROLE_ID, UserSearchDescriptor.ROLE_ID);
//        addFilterToSearchBuilder(filters, builder, UserItem.FILTER_GROUP_ID, UserSearchDescriptor.GROUP_ID);
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

    /**
     * Delete users
     * 
     * @param ids
     */
    public void delete(final List<APIID> ids) {
        try {
            getIdentityAPI().deleteUsers(APIID.toLongList(ids));
        } catch (final InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONVERTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected final UserCreator convertConsoleToEngineItem(final UserItem user) {
        if (user == null) {
            return null;
        }

        final UserCreator userCreator = new UserCreator(user.getUserName(), user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setTitle(user.getTitle())
                .setIconPath(user.getIcon())
                .setJobTitle(user.getJobTitle());

        final APIID managerId = user.getManagerId();
        if (managerId != null) {
            userCreator.setManagerUserId(managerId.toLong());
        }
        return userCreator;
    }

    // TODO remove while Tests have been migrated to new API
    public UserItem convertEngineToConsoleItem2(final User user) {
        return convertEngineToConsoleItem(user);
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
