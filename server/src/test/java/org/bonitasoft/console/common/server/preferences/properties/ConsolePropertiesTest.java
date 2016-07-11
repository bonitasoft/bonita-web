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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Rohart Bastien
 */
public class ConsolePropertiesTest {

    private static final long TENANT_ID = 236L;
    private ConsoleProperties properties;

    @Before
    public void setUp() throws IOException {
        ConfigurationFilesManager.getInstance().setTenantConfigurations(Collections.singletonMap("console-config.properties", ("" +
                "aProperty=aValue").getBytes()), TENANT_ID);
        properties = new ConsoleProperties(TENANT_ID);
    }

    @Test
    public void testGetNotExistingProperty() {
        assertNull("Cannot get a not existing property", properties.getProperty("test"));
    }

    @Test
    public void testWeCanRetrieveAProperty() {
        String value = properties.getProperty("aProperty");

        assertEquals("Cannot retrieve a property", value, "aValue");
    }

    @Test
    public void testDefaultIsRetrieveIfPropertyIsEmpty() {
        String defaultValue = properties.getProperty("notExsitingProperty", "defaultValue");

        assertEquals("Cannot return property default value", defaultValue, "defaultValue");
    }

}
