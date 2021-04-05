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
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
    private static final String MY_PROP_INTERNAL_PROPERTIES = "myProp-internal.properties";
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
                ("myProp1=authKey\nmyProp2=passHash").getBytes());
        configurationFiles.put(MY_PROP_INTERNAL_PROPERTIES,
                ("testProperty=testValue\npropToRemove=willBeRemoved").getBytes());
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
        assertThat(configurationFilesManager.getTenantProperties(MY_PROP_INTERNAL_PROPERTIES, TENANT_ID)).doesNotContainKey("propToRemove");
        verify(platformManagementUtils).updateConfigurationFile(eq(TENANT_ID), eq(MY_PROP_INTERNAL_PROPERTIES), contentCaptor.capture());
        assertThat(new String(contentCaptor.getValue())).doesNotContain("propToRemove").contains("testProperty", "testValue");
    }

    @Test
    public void should_setProperty_call_update_with_new_content() throws Exception {
        //when
        configurationFilesManager.setProperty(MY_PROP_PROPERTIES, TENANT_ID, "testProperty", "new Value");
        //then
        assertThat(configurationFilesManager.getTenantProperties(MY_PROP_INTERNAL_PROPERTIES, TENANT_ID)).contains(entry("testProperty", "new Value"));
        verify(platformManagementUtils).updateConfigurationFile(eq(TENANT_ID), eq(MY_PROP_INTERNAL_PROPERTIES), contentCaptor.capture());
        assertThat(new String(contentCaptor.getValue())).doesNotContain("testValue").contains("testProperty", "new Value");
    }

    @Test
    public void getAlsoCustomAndInternalPropertiesFromFilename_should_merge_custom_properties_if_exist() throws Exception {
        // given:
        Map<String, Properties> propertiesMap = new HashMap<>(2);
        final Properties defaultProps = new Properties();
        defaultProps.put("defaultKey", "defaultValue");
        propertiesMap.put("toto.properties", defaultProps);
        final Properties customProps = new Properties();
        customProps.put("customKey", "customValue");
        propertiesMap.put("toto-custom.properties", customProps);

        // when:
        final Properties properties = configurationFilesManager.getAlsoCustomAndInternalPropertiesFromFilename(propertiesMap, "toto.properties");

        // then:
        assertThat(properties).containsEntry("defaultKey", "defaultValue").containsEntry("customKey", "customValue");
    }

    @Test
    public void getAlsoCustomAndInternalPropertiesFromFilename_should_merge_internal_properties_if_exist() throws Exception {
        // given:
        Map<String, Properties> propertiesMap = new HashMap<>(2);
        final Properties defaultProps = new Properties();
        defaultProps.put("defaultKey", "defaultValue");
        propertiesMap.put("toto.properties", defaultProps);
        final Properties internalProps = new Properties();
        internalProps.put("internalKey", "internalValue");
        propertiesMap.put("toto-internal.properties", internalProps);

        // when:
        final Properties properties = configurationFilesManager.getAlsoCustomAndInternalPropertiesFromFilename(propertiesMap, "toto.properties");

        // then:
        assertThat(properties).containsEntry("defaultKey", "defaultValue").containsEntry("internalKey", "internalValue");
    }

    @Test
    public void custom_properties_should_overwrite_internal_properties() throws Exception {
        // given:
        final Properties defaultProps = new Properties();
        defaultProps.put("defaultKey", "defaultValue");

        final Properties internalProps = new Properties();
        internalProps.put("otherKey", "someInternallyManagedValue");

        final Properties customProps = new Properties();
        final String expectedOverwrittenValue = "custom_changed_value";
        customProps.put("otherKey", expectedOverwrittenValue);

        Map<String, Properties> propertiesMap = new HashMap<>(3);
        propertiesMap.put("overwrite.properties", defaultProps);
        propertiesMap.put("overwrite-internal.properties", internalProps);
        propertiesMap.put("overwrite-custom.properties", customProps);

        // when:
        final Properties properties = configurationFilesManager.getAlsoCustomAndInternalPropertiesFromFilename(propertiesMap, "overwrite.properties");

        // then:
        assertThat(properties).containsEntry("defaultKey", "defaultValue").containsEntry("otherKey", expectedOverwrittenValue);
    }

    @Test
    public void removeProperty_should_remove_value_from_internal_file() throws Exception {
        // given:
        Map<String, Properties> propertiesMap = spy(new HashMap<>(1));
        final Properties internalProps = new Properties();
        internalProps.put("internalKey", "internalValue");
        propertiesMap.put("my_resources-internal.properties", internalProps);

        doReturn(propertiesMap).when(configurationFilesManager).getResources(TENANT_ID);

        // when:
        configurationFilesManager.removeProperty("my_resources.properties", TENANT_ID, "toBeRemoved");

        // then:
        verify(propertiesMap).get("my_resources-internal.properties");
    }
}
