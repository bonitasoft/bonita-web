/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.engineclient;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.profile.ProfileEntryNotFoundException;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Paul Amar
 * 
 */
public class ProfileEntryEngineClientTest extends APITestWithMock  {

    @Mock
    private ProfileAPI profileAPI;

    private ProfileEntryEngineClient engineClient;

    @Before
    public void initializeEngineClient() {
        initMocks(this);
        engineClient = new ProfileEntryEngineClient(profileAPI);
    }
    
    
    @Test
    public void getProfileEntryItem_call_engine_and_get_profile_entry_with_specific_id() throws ProfileEntryNotFoundException {
        engineClient.getProfileEntry(1L);
        
        verify(profileAPI).getProfileEntry(1L);
    }
    
    @Test(expected = APIItemNotFoundException.class)
    public void getProfileEntryItem_call_engine_and_if_profile_entry_not_found_raises_ProfileEntryException() throws APIItemNotFoundException, ProfileEntryNotFoundException {
               
        doThrow(new APIItemNotFoundException("profile entry not found", APIID.makeAPIID(-1L))).when(profileAPI).getProfileEntry(-1L);
        engineClient.getProfileEntry(-1L);
    }
}
