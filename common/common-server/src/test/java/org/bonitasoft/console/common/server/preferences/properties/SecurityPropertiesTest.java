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
package org.bonitasoft.console.common.server.preferences.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.io.File;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityPropertiesTest {

    private static final File TENANT_CONF_FOLDER = new File("src/test/resources/bonita/client/tenants/1/conf");
    private static final File FORMS_WOR_FOLDER = new File("src/test/resources/bonita/client/tenants/1/work/forms");
    
    @Mock
    private WebBonitaConstantsUtils webConstants;

    @Before
    public void setUp() {
        when(webConstants.getConfFolder()).thenReturn(TENANT_CONF_FOLDER);
        when(webConstants.getFormsWorkFolder()).thenReturn(FORMS_WOR_FOLDER);
    }
    
    @Test
    public void testTenantPropertiesCanBeRetrieveFromAFile() throws Exception {
        SecurityProperties tenantProperties = new SecurityProperties(webConstants, SecurityProperties.TENANT_SCOPE_CONFIG_ID);

        assertThat(tenantProperties.getAutoLoginUserName(), is("aUserNameForTenant"));
        assertThat(tenantProperties.getAutoLoginPassword(), is("aPasswordForTenant"));
    }

    @Test
    public void testGettingPropertiesFromProcessWithoutSecurityConfigRetrieveTenantSecurityConfig() throws Exception {
        SecurityProperties processProperties = new SecurityProperties(webConstants, new ProcessIdentifier("process2", "1.5").getIdentifier());

        assertThat(processProperties.getAutoLoginUserName(), is("aUserNameForTenant"));
        assertThat(processProperties.getAutoLoginPassword(), is("aPasswordForTenant"));
    }

    @Test
    public void testLastDeployedProcessPropertiesCanBeRetrieveFromAFile() throws Exception {
        SecurityProperties processProperties = new SecurityProperties(webConstants, new ProcessIdentifier("process3", "2.9").getIdentifier());

        assertThat(processProperties.allowAutoLogin(), is(false));
        assertThat(processProperties.getAutoLoginUserName(), is("aUserNameForSecondDeployement"));
        assertThat(processProperties.getAutoLoginPassword(), is("aPasswordForSecondDeployement"));
    }
}
