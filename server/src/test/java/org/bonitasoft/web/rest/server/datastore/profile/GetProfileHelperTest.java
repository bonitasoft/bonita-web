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
import static org.bonitasoft.web.rest.server.datastore.profile.EngineProfileBuilder.anEngineProfile;
import static org.bonitasoft.web.rest.model.builder.profile.ProfileItemBuilder.aProfileItem;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.engineclient.ProfileEngineClient;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class GetProfileHelperTest extends APITestWithMock {

    @Mock
    ProfileEngineClient profileClient;

    GetProfileHelper getProfileHelper;

    @Before
    public void setUp() {
        initMocks(this);
        getProfileHelper = new GetProfileHelper(profileClient);
    }

    @Test
    public void testWeCanRetrieveAProfile() throws Exception {
        final Profile aKnownProfile = anEngineProfile().withName("aName").withDescription("aDescription").build();
        when(profileClient.getProfile(1L)).thenReturn(aKnownProfile);

        final ProfileItem item = getProfileHelper.get(APIID.makeAPIID(1L));

        assertTrue(areEquals(aProfileItem().fromEngineItem(aKnownProfile).withIcon(ProfileItemConverter.DEFAULT_PROFILE_ICONPATH).build(), item));
    }
}
