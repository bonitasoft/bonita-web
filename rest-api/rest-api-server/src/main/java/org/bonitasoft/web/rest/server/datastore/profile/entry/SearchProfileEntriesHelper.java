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
package org.bonitasoft.web.rest.server.datastore.profile.entry;

import java.util.Map;

import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemSearchResultConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.filter.GenericFilterCreator;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.rest.server.engineclient.ProfileEntryEngineClient;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;

/**
 * @author Vincent Elcrin
 * 
 */
public class SearchProfileEntriesHelper implements DatastoreHasSearch<ProfileEntryItem> {

    private ProfileEntryEngineClient profileEntryClient;

    public SearchProfileEntriesHelper(ProfileEntryEngineClient profileEntryClient) {
        this.profileEntryClient = profileEntryClient;
    }

    @Override
    public ItemSearchResult<ProfileEntryItem> search(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        SearchOptionsCreator options = makeSearchOptions(page, resultsByPage, search, orders, filters);
        SearchResult<ProfileEntry> profileEntries = profileEntryClient.searchProfiles(options.create());
        return new ItemSearchResultConverter<ProfileEntryItem, ProfileEntry>(page, resultsByPage, profileEntries, 
                new ProfileEntryItemConverter()).toItemSearchResult();
    }

    private SearchOptionsCreator makeSearchOptions(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        return new SearchOptionsCreator(page,
                resultsByPage,
                search,
                new Sorts(orders, new ProfileEntrySearchDescriptorConverter()),
                new Filters(filters, new GenericFilterCreator(new ProfileEntrySearchDescriptorConverter())));
    }

}
