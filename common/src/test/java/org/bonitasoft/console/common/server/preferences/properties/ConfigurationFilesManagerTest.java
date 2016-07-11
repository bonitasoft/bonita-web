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

package org.bonitasoft.console.common.server.preferences.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.utils.PlatformManagementUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Baptiste Mesta
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationFilesManagerTest {

    private static final long TENANT_ID = 543892L;
    private static final String MY_PROP_PROPERTIES = "myProp.properties";
    @Mock
    private PlatformManagementUtils platformManagementUtils;
    @Spy
    private ConfigurationFilesManager configurationFilesManager;
    @Captor
    private ArgumentCaptor<byte[]> contentCaptor;

    @Before
    public void before() throws Exception {
        final HashMap<String, byte[]> configurationFiles = new HashMap<>();
        configurationFiles.put("configFile1.properties",
                ("myProp1=authKey\n" +
                        "myProp2=passHash").getBytes());
        configurationFiles.put(MY_PROP_PROPERTIES,
                ("testProperty=testValue\n" +
                        "propToRemove=willBeRemoved").getBytes());
        configurationFiles.put(PlatformManagementUtils.AUTOLOGIN_V6_JSON,
                "[{processname:\"Pool1\", processversion:\"1.0\"}]".getBytes());
        configurationFilesManager.setTenantConfigurations(configurationFiles, TENANT_ID);
        doReturn(platformManagementUtils).when(configurationFilesManager).getPlatformManagementUtils();
    }

    @Test
    public void should_removeProperty_call_update_with_new_content() throws Exception {
        //given
        assertThat(configurationFilesManager.getTenantProperties(MY_PROP_PROPERTIES, TENANT_ID)).contains(entry("propToRemove", "willBeRemoved"));
        //when
        configurationFilesManager.removeProperty(MY_PROP_PROPERTIES, TENANT_ID, "propToRemove");
        //then
        assertThat(configurationFilesManager.getTenantProperties(MY_PROP_PROPERTIES, TENANT_ID)).doesNotContainKey("propToRemove");
        verify(platformManagementUtils).updateConfigurationFile(eq(TENANT_ID), eq(MY_PROP_PROPERTIES), contentCaptor.capture());
        assertThat(new String(contentCaptor.getValue())).doesNotContain("propToRemove").contains("testProperty", "testValue");
    }

    @Test
    public void should_setProperty_call_update_with_new_content() throws Exception {
        //when
        configurationFilesManager.setProperty(MY_PROP_PROPERTIES, TENANT_ID, "testProperty", "new Value");
        //then
        assertThat(configurationFilesManager.getTenantProperties(MY_PROP_PROPERTIES, TENANT_ID)).contains(entry("testProperty", "new Value"));
        verify(platformManagementUtils).updateConfigurationFile(eq(TENANT_ID), eq(MY_PROP_PROPERTIES), contentCaptor.capture());
        assertThat(new String(contentCaptor.getValue())).doesNotContain("testValue").contains("testProperty", "new Value");
    }

    @Test
    public void should_update_configuration_when_calling_setTenantConfiguration() throws Exception {
        //given
        final String newFileContent = "[{processname:\"Pool2\", processversion:\"2.0\"}]";
        //when
        configurationFilesManager.setTenantConfiguration(PlatformManagementUtils.AUTOLOGIN_V6_JSON,
                newFileContent.getBytes(), TENANT_ID);
        //then
        final File configurationFile = configurationFilesManager.getTenantConfigurationFile(PlatformManagementUtils.AUTOLOGIN_V6_JSON, TENANT_ID);
        assertThat(FileUtils.readFileToString(configurationFile)).isEqualTo(newFileContent);
    }

    @Test
    public void should_update_configurations_when_calling_setTenantConfigurations() throws Exception {
        //given
        final String newFileContent = "[{processname:\"Pool2\", processversion:\"2.0\"}]";
        final Map<String, byte[]> configurationFiles = new HashMap<String, byte[]>();
        configurationFiles.put(PlatformManagementUtils.AUTOLOGIN_V6_JSON, newFileContent.getBytes());
        //when
        configurationFilesManager.setTenantConfigurations(configurationFiles, TENANT_ID);
        //then
        final File configurationFile = configurationFilesManager.getTenantConfigurationFile(PlatformManagementUtils.AUTOLOGIN_V6_JSON, TENANT_ID);
        assertThat(FileUtils.readFileToString(configurationFile)).isEqualTo(newFileContent);
    }

}
