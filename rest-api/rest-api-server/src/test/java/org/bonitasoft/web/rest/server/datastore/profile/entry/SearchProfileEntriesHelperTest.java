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

import static junit.framework.Assert.assertTrue;
import static org.bonitasoft.web.rest.server.model.builder.profile.entry.EngineProfileEntryBuilder.anEngineProfileEntry;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem.VALUE_TYPE;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.datastore.profile.entry.ProfileEntryItemConverter;
import org.bonitasoft.web.rest.server.datastore.profile.entry.SearchProfileEntriesHelper;
import org.bonitasoft.web.rest.server.datastore.utils.SearchUtils;
import org.bonitasoft.web.rest.server.engineclient.ProfileEntryEngineClient;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class SearchProfileEntriesHelperTest extends APITestWithMock {

    @Mock
    ProfileEntryEngineClient profileEntryClient;

    SearchProfileEntriesHelper searchProfileEntriesHelper;

    @Before
    public void setUp() {
        initMocks(this);
        searchProfileEntriesHelper = new SearchProfileEntriesHelper(profileEntryClient);
    }

    @Test
    public void testWeCanSearchProfiles() throws Exception {
        SearchResult<ProfileEntry> aKnownSearchResult = 
                SearchUtils.createEngineSearchResult(aKnownProfileEntry(), anotherKnownProfileEntry());
        List<ProfileEntryItem> expectedProfileEntryItems = new ProfileEntryItemConverter().convert(aKnownSearchResult.getResult());
        when(profileEntryClient.searchProfiles(any(SearchOptions.class))).thenReturn(aKnownSearchResult);

        ItemSearchResult<ProfileEntryItem> searchResult = searchProfileEntriesHelper.search(0, 10, null, null, null);

        assertTrue(SearchUtils.areEquals(expectedProfileEntryItems, searchResult.getResults()));
    }

    private ProfileEntry aKnownProfileEntry() {
        return anEngineProfileEntry().build();
    }

    private ProfileEntry anotherKnownProfileEntry() {
        return anEngineProfileEntry()
                .withDescription("anotherDescription")
                .withId(5L)
                .withIndex(6)
                .withName("anotherName")
                .withPage("anotherPageName")
                .withParentId(7L)
                .withType(VALUE_TYPE.folder)
                .build();
    }
}
