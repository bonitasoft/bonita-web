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

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Baptiste Mesta
 */
@RunWith(MockitoJUnitRunner.class)
public class PermissionsBuilderAccessorTest {

    @Mock
    private SecurityProperties securityProperties;
    @Mock
    private PlatformManagementUtils platformManagementUtils;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void before() throws Exception {
    }

    @Test
    public void should_reloadPropertiesIfInDebug_reload_properties_when_in_debug() throws Exception {
        //given
        doReturn(true).when(securityProperties).isAPIAuthorizationsCheckInDebugMode();
        //when
        PermissionsBuilderAccessor.reloadPropertiesIfInDebug(securityProperties, platformManagementUtils);
        //then
        verify(platformManagementUtils).initializePlatformConfiguration();
    }

    @Test
    public void should_reloadPropertiesIfInDebug_do_not_reload_properties_when_not_in_debug() throws Exception {
        //given
        doReturn(false).when(securityProperties).isAPIAuthorizationsCheckInDebugMode();
        //when
        PermissionsBuilderAccessor.reloadPropertiesIfInDebug(securityProperties, platformManagementUtils);
        //then
        verify(platformManagementUtils, never()).initializePlatformConfiguration();
    }

    @Test
    public void should_reloadPropertiesIfInDebug_handles_exception() throws Exception {
        //given
        doReturn(true).when(securityProperties).isAPIAuthorizationsCheckInDebugMode();
        doThrow(IOException.class).when(platformManagementUtils).initializePlatformConfiguration();
        //when
        expectedException.expect(LoginFailedException.class);
        expectedException.expectMessage("debug mode");
        PermissionsBuilderAccessor.reloadPropertiesIfInDebug(securityProperties, platformManagementUtils);
        //then exception
    }

}
