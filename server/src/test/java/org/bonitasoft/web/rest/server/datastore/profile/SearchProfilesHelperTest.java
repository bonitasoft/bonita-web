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
package org.bonitasoft.web.rest.server.datastore.profile;

import static junit.framework.Assert.assertTrue;
import static org.bonitasoft.web.rest.model.builder.profile.EngineProfileBuilder.anEngineProfile;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.List;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.datastore.utils.SearchUtils;
import org.bonitasoft.web.rest.server.engineclient.ProfileEngineClient;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class SearchProfilesHelperTest extends APITestWithMock {

    @Mock
    ProfileEngineClient profileClient;

    SearchProfilesHelper searchProfilesHelper;

    @Before
    public void setUp() {
        initMocks(this);
        searchProfilesHelper = new SearchProfilesHelper(profileClient);
    }

    @Test
    public void testWeCanSearchProfiles() throws Exception {
        final SearchResultImpl<Profile> aKnownSearchResult = aKnownSearchResult();
        final List<ProfileItem> expectedProfiles = new ProfileItemConverter().convert(aKnownSearchResult.getResult());
        when(profileClient.searchProfiles(any(SearchOptions.class))).thenReturn(aKnownSearchResult);

        final ItemSearchResult<ProfileItem> searchResult = searchProfilesHelper.search(0, 10, null, null, null);

        assertTrue(SearchUtils.areEquals(expectedProfiles, searchResult.getResults()));
    }

    @Test
    public void testWeCanListUserProfiles() throws Exception {
        final SearchResultImpl<Profile> aKnownSearchResult = aKnownSearchResult();
        final List<ProfileItem> expectedProfiles = new ProfileItemConverter().convert(aKnownSearchResult.getResult());
        when(profileClient.listProfilesForUser(2L)).thenReturn(aKnownSearchResult.getResult());

        final ItemSearchResult<ProfileItem> searchResult = searchProfilesHelper.search(0, 10, null, null, filterOnUserId(2L));

        assertTrue(SearchUtils.areEquals(expectedProfiles, searchResult.getResults()));
    }

    private HashMap<String, String> filterOnUserId(final long id) {
        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(ProfileItem.FILTER_USER_ID, String.valueOf(id));
        return filters;
    }

    private SearchResultImpl<Profile> aKnownSearchResult() {
        final Profile aKnownProfile = anEngineProfile().withName("aName").withDescription("aDescription").build();
        final Profile anotherKnownProfile = anEngineProfile().withName("anotherName").withDescription("anotherDescription").build();
        return SearchUtils.createEngineSearchResult(aKnownProfile, anotherKnownProfile);
    }
}
