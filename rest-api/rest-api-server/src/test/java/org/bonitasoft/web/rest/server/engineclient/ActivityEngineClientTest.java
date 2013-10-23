/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
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

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


public class ActivityEngineClientTest extends APITestWithMock {
    
    @Mock
    private ProcessAPI processAPI;
    
    private ActivityEngineClient activityEngineClient;

    @Before
    public void initializeClient() {
        initMocks(this);
        activityEngineClient = new ActivityEngineClient(processAPI);
    }
    
    @Test(expected = APIException.class)
    public void getDataInstance_throw_exception_if_data_is_not_found() throws Exception {
        when(processAPI.getActivityDataInstance(anyString(), anyLong())).thenThrow(DataNotFoundException.class);
        
        activityEngineClient.getDataInstance("aName", 1L);
    }

}
