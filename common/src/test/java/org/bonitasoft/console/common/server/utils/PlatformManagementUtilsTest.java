/**
 * Copyright (C) 2016 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/

package org.bonitasoft.console.common.server.utils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.engine.session.impl.PlatformSessionImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Baptiste Mesta
 */
@RunWith(MockitoJUnitRunner.class)
public class PlatformManagementUtilsTest {

    private static final long TENANT_ID = 6543L;
    @Mock
    private PlatformAPI platformAPI;
    @Spy
    private PlatformManagementUtils platformManagementUtils;

    @Before
    public void before() throws Exception {
        doNothing().when(platformManagementUtils).platformLogout(any(PlatformSession.class));
        doReturn(new PlatformSessionImpl(1231, new Date(), 54325423, "testUser", 75463)).when(platformManagementUtils).platformLogin();
        doReturn(platformAPI).when(platformManagementUtils).getPlatformAPI(any(PlatformSession.class));
    }

    @Test
    public void should_updateConfigurationFile_call_engine() throws Exception {
        //when
        platformManagementUtils.updateConfigurationFile(TENANT_ID, "myFile", "theNewContent".getBytes());
        //then
        InOrder inOrder = inOrder(platformManagementUtils, platformAPI);
        inOrder.verify(platformManagementUtils).platformLogin();
        inOrder.verify(platformAPI).updateClientTenantConfigurationFile(TENANT_ID, "myFile", "theNewContent".getBytes());
        inOrder.verify(platformManagementUtils).platformLogout(any(PlatformSession.class));
    }
    
    @Test
    public void should_retrieveAutologinConfiguration() throws Exception {
        //when
        platformManagementUtils.retrieveAutologinConfiguration(TENANT_ID);
        //then
        InOrder inOrder = inOrder(platformManagementUtils, platformAPI);
        inOrder.verify(platformManagementUtils).platformLogin();
        inOrder.verify(platformAPI).getClientTenantConfiguration(TENANT_ID, PlatformManagementUtils.AUTOLOGIN_V6_JSON);
        inOrder.verify(platformManagementUtils).platformLogout(any(PlatformSession.class));
    }

}
