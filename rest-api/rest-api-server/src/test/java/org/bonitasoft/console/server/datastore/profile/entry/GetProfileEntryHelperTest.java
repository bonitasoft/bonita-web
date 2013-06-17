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
package org.bonitasoft.console.server.datastore.profile.entry;

import static junit.framework.Assert.assertTrue;
import static org.bonitasoft.console.server.model.builder.profile.entry.EngineProfileEntryBuilder.anEngineProfileEntry;
import static org.bonitasoft.console.server.model.builder.profile.entry.ProfileEntryItemBuilder.aProfileEntryItem;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.console.server.APITestWithMock;
import org.bonitasoft.console.server.engineclient.ProfileEntryEngineClient;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class GetProfileEntryHelperTest extends APITestWithMock {

    @Mock
    ProfileEntryEngineClient profileEntryClient;

    GetProfileEntryHelper getProfileEntryHelper;

    @Before
    public void setUp() {
        initMocks(this);
        getProfileEntryHelper = new GetProfileEntryHelper(profileEntryClient);
    }

    @Test
    public void testWeCanRetrieveAProfileEntry() throws Exception {
        ProfileEntry expectedProfileEntry = anEngineProfileEntry().build();
        when(profileEntryClient.getProfileEntry(1L)).thenReturn(expectedProfileEntry);

        ProfileEntryItem item = getProfileEntryHelper.get(APIID.makeAPIID(1L));

        assertTrue(areEquals(aProfileEntryItem().from(expectedProfileEntry).build(), item));
    }
}
