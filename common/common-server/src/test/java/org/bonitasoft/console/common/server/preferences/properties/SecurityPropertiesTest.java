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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 */
public class SecurityPropertiesTest {

    @Before
    public void setUp() throws Exception {
        System.setProperty(WebBonitaConstants.BONITA_HOME, "src/test/resources/bonita");
    }

    @After
    public void tearDown() {
        System.clearProperty(WebBonitaConstants.BONITA_HOME);
    }

    /*
     * !!! These test depends on files in test resources !!!
     */
    @Test
    public void testTenantPropertiesCanBeRetrieveFromAFile() throws Exception {
        SecurityProperties tenantProperties = SecurityProperties.getInstance(1);

        checkTenantProperties(tenantProperties);
    }

    /*
     * !!! These test depends on files in test resources !!!
     */
    @Test
    public void testGettingPropertiesFromProcessWithoutSecurityConfigRetrieveTenantSecurityConfig() throws Exception {
        SecurityProperties processProperties = SecurityProperties.getInstance(1,
                new ProcessIdentifier("process2", "1.5"));

        assertTrue(checkTenantProperties(processProperties));
    }

    /*
     * !!! These test depends on files in test resources !!!
     */
    @Test
    public void testLastDeployedProcessPropertiesCanBeRetrieveFromAFile() throws Exception {
        SecurityProperties processProperties = SecurityProperties.getInstance(1,
                new ProcessIdentifier("process3", "2.9"));

        assertFalse(processProperties.allowAutoLogin());
        assertEquals("aUserNameForSecondDeployement", processProperties.getAutoLoginUserName());
        assertEquals("aPasswordForSecondDeployement", processProperties.getAutoLoginPassword());
    }

    private boolean checkTenantProperties(SecurityProperties properties) {
        return properties.allowAutoLogin()
                && "aUserNameForTenant".equals(properties.getAutoLoginUserName())
                && "aPasswordForTenant".equals(properties.getAutoLoginPassword());
    }
}
