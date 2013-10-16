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
package org.bonitasoft.web.rest.server.datastore.profile.member;

import java.util.Map;

import org.bonitasoft.engine.profile.ProfileMember;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemSearchResultConverter;
import org.bonitasoft.web.rest.server.datastore.filter.FilterAccessor;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.filter.GenericFilterCreator;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.rest.server.engineclient.ProfileMemberEngineClient;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;

/**
 * @author Vincent Elcrin
 * 
 */
public class SearchProfileMembersHelper implements DatastoreHasSearch<ProfileMemberItem> {

    private ProfileMemberEngineClient profileMemberClient;

    public SearchProfileMembersHelper(ProfileMemberEngineClient profileMemberClient) {
        this.profileMemberClient = profileMemberClient;
    }

    @Override
    public ItemSearchResult<ProfileMemberItem> search(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        SearchOptionsCreator options = makeSearchOptions(page, resultsByPage, search, orders, filters);
        SearchResult<ProfileMember> searchResult = 
                profileMemberClient.searchProfileMembers(getMemberType(filters).getType(), options.create());
        return new ItemSearchResultConverter<ProfileMemberItem, ProfileMember>(page, resultsByPage, searchResult, 
                new ProfileMemberItemConverter()).toItemSearchResult();
    }

    private MemberType getMemberType(Map<String, String> filters) {
        FilterAccessor filterAccess = new FilterAccessor(filters);
        return filterAccess.getMandatory(ProfileMemberItem.FILTER_MEMBER_TYPE, new MemberTypeConverter());
    }

    private SearchOptionsCreator makeSearchOptions(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        return new SearchOptionsCreator(page, resultsByPage, search,
                new Sorts(orders, new ProfileMemberSearchDescriptorConverter()),
                new Filters(filters, new GenericFilterCreator(new ProfileMemberSearchDescriptorConverter())));
    }
}
