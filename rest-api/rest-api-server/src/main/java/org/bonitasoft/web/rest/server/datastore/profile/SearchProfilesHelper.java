/**
 * 
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
package org.bonitasoft.web.rest.server.datastore.profile;

import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemSearchResultConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.filter.GenericFilterCreator;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.rest.server.engineclient.ProfileEngineClient;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;

public class SearchProfilesHelper implements DatastoreHasSearch<ProfileItem> {

    private ProfileEngineClient profileClient;

    public SearchProfilesHelper(ProfileEngineClient profileClient) {
        this.profileClient = profileClient;
    }

    @Override
    public ItemSearchResult<ProfileItem> search(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        if (isFilteredByUserId(filters)) {
            return searchProfilesForUser(page, filters);
        }
        return searchProfiles(page, resultsByPage, search, orders, filters);
    }

    private boolean isFilteredByUserId(Map<String, String> filters) {
        return filters != null && !MapUtil.isBlank(filters, ProfileItem.FILTER_USER_ID);
    }

    private ItemSearchResult<ProfileItem> searchProfiles(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        SearchOptionsCreator options = makeSearchOptions(page, resultsByPage, search, orders, filters);
        SearchResult<Profile> searchProfiles = profileClient.searchProfiles(options.create());
        return new ItemSearchResultConverter<ProfileItem, Profile>(page, resultsByPage, searchProfiles, new ProfileItemConverter()).toItemSearchResult();
    }

    private ItemSearchResult<ProfileItem> searchProfilesForUser(int page, Map<String, String> filters) {
        long userId = Long.parseLong(filters.get(ProfileItem.FILTER_USER_ID));
        List<Profile> profiles = profileClient.listProfilesForUser(userId);
        return new ItemSearchResult<ProfileItem>(page, profiles.size(), profiles.size(), new ProfileItemConverter().convert(profiles));
    }

    private SearchOptionsCreator makeSearchOptions(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        return new SearchOptionsCreator(page, resultsByPage, search,
                new Sorts(orders, new ProfileSearchDescriptorConverter()),
                new Filters(filters, new GenericFilterCreator(new ProfileSearchDescriptorConverter())));
    }
}
