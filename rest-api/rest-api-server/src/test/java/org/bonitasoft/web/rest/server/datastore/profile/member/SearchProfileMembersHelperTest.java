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

import static junit.framework.Assert.assertTrue;
import static org.bonitasoft.web.rest.model.builder.profile.member.EngineProfileMemberBuilder.anEngineProfileMember;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bonitasoft.engine.profile.ProfileMember;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.datastore.profile.member.MemberType;
import org.bonitasoft.web.rest.server.datastore.profile.member.ProfileMemberItemConverter;
import org.bonitasoft.web.rest.server.datastore.profile.member.SearchProfileMembersHelper;
import org.bonitasoft.web.rest.server.datastore.utils.SearchUtils;
import org.bonitasoft.web.rest.server.engineclient.ProfileMemberEngineClient;
import org.bonitasoft.web.rest.server.framework.exception.APIFilterMandatoryException;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class SearchProfileMembersHelperTest extends APITestWithMock {

    @Mock
    ProfileMemberEngineClient engineClient;

    SearchProfileMembersHelper searchProfilesHelper;

    @Before
    public void setUp() {
        initMocks(this);
        searchProfilesHelper = new SearchProfileMembersHelper(engineClient);
    }

    @Test
    public void testWeCanSearchProfileMembers() throws Exception {
        SearchResultImpl<ProfileMember> aKnownSearchResult = aKnownSearchResult();
        List<ProfileMemberItem> expectedProfileMemberItems = new ProfileMemberItemConverter().convert(aKnownSearchResult().getResult());
        when(engineClient.searchProfileMembers(eq(MemberType.ROLE.getType()), any(SearchOptions.class))).thenReturn(aKnownSearchResult);
        HashMap<String, String> filters = filterOnProfileIdAndMemberType(5L, MemberType.ROLE);
        
        ItemSearchResult<ProfileMemberItem> searchResult = searchProfilesHelper.search(0, 10, null, null, filters);

        assertTrue(SearchUtils.areEquals(expectedProfileMemberItems, searchResult.getResults()));
    }

    @Test(expected = APIFilterMandatoryException.class)
    public void testSearchWithoutMandatoryFiltersThrowError() {
        
        searchProfilesHelper.search(0, 10, null, null, Collections.<String, String> emptyMap());
    }

    private SearchResultImpl<ProfileMember> aKnownSearchResult() {
        return SearchUtils.createEngineSearchResult(aKnownProfile(), anotherKnownProfile());

    }
    private HashMap<String, String> filterOnProfileIdAndMemberType(long id, MemberType type) {
        HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, String.valueOf(id));
        filters.put(ProfileMemberItem.FILTER_MEMBER_TYPE, type.getType());
        return filters;
    }

    private ProfileMember aKnownProfile() {
        return anEngineProfileMember() .build();
    }

    private ProfileMember anotherKnownProfile() {
        return anEngineProfileMember()
                .withId(2L)
                .withProfileId(3L)
                .withUserId(4L)
                .withGroupId(5L)
                .withRoleId(6l)
                .build();
    }

}
