/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.preferences.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

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

    private static final File FORMS_WOR_FOLDER = new File("src/test/resources/formsWorkFolder/forms");
    public static final long TENANT_ID = 43882L;

    @Mock
    private WebBonitaConstantsUtils webConstants;

    @Before
    public void setUp() throws IOException {
        ConfigurationFilesManager.getInstance().setTenantConfigurations(Collections.singletonMap("security-config.properties", ("" +
                "forms.application.login.auto.username = aUserNameForTenant\n" +
                "forms.application.login.auto.password = aPasswordForTenant\n" +
                "security.rest.api.authorizations.check.enabled = true").getBytes()), TENANT_ID);
        when(webConstants.getFormsWorkFolder()).thenReturn(FORMS_WOR_FOLDER);
    }

    @Test
    public void testTenantPropertiesCanBeRetrieveFromAFile() throws Exception {
        final SecurityProperties tenantProperties = new SecurityProperties(TENANT_ID);

        assertThat(tenantProperties.getAutoLoginUserName(), is("aUserNameForTenant"));
        assertThat(tenantProperties.getAutoLoginPassword(), is("aPasswordForTenant"));
        assertTrue(tenantProperties.isAPIAuthorizationsCheckEnabled());
    }

    @Test
    public void testGettingPropertiesFromProcessWithoutSecurityConfigRetrieveTenantSecurityConfig() throws Exception {
        final SecurityProperties processProperties = createSecurityProperties(new ProcessIdentifier("process2", "1.5"));

        assertThat(processProperties.getAutoLoginUserName(), is("aUserNameForTenant"));
        assertThat(processProperties.getAutoLoginPassword(), is("aPasswordForTenant"));
    }

    private SecurityProperties createSecurityProperties(ProcessIdentifier process2) {
        return new SecurityProperties(TENANT_ID, process2) {

            @Override
            protected WebBonitaConstantsUtils getWebBonitaConstantUtils() {
                return webConstants;
            }
        };
    }

    @Test
    public void testLastDeployedProcessPropertiesCanBeRetrieveFromAFile() throws Exception {
        final SecurityProperties processProperties = createSecurityProperties(new ProcessIdentifier("process3", "2.9"));

        assertThat(processProperties.allowAutoLogin(), is(false));
        assertThat(processProperties.getAutoLoginUserName(), is("aUserNameForSecondDeployement"));
        assertThat(processProperties.getAutoLoginPassword(), is("aPasswordForSecondDeployement"));
    }
}
