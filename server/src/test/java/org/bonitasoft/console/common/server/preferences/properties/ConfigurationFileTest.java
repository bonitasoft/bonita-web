package org.bonitasoft.console.common.server.preferences.properties;

import java.util.Properties;
import java.util.Set;

import org.bonitasoft.engine.commons.io.IOUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.console.common.server.preferences.properties.ConfigurationFilesManager.getProperties;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationFileTest {

    public static final String COMPOUND_PERMISSIONS_MAPPING_FILE = "compound.properties";
    public static final String RESOURCES_PERMISSIONS_MAPPING_FILE = "resources.properties";
    public static final String CUSTOM_PERMISSIONS_MAPPING_FILE = "custom.properties";
    private static Properties compoundProperties;
    private static Properties resourcesProperties;
    private static Properties customProperties;

    @BeforeClass
    public static void init() throws Exception {
        compoundProperties = getProperties(IOUtil.getAllContentFrom(ConfigurationFileTest.class.getResourceAsStream("/compound-permissions-mapping.properties")));
        resourcesProperties = getProperties(IOUtil.getAllContentFrom(ConfigurationFileTest.class.getResourceAsStream("/resources-permissions-mapping.properties")));
        customProperties = getProperties(IOUtil.getAllContentFrom(ConfigurationFileTest.class.getResourceAsStream("/custom-permissions-mapping.properties")));
    }

    @Test
    public void should_getProperty_return_the_right_custom_permissions_with_special_characters() {
        final ConfigurationFile tenantProperties = spy(new ConfigurationFile(CUSTOM_PERMISSIONS_MAPPING_FILE));
        doReturn(customProperties).when(tenantProperties).getTenantPropertiesOfScope();

        final String customValue = tenantProperties.getTenantProperty("profile|HR manager");

        assertEquals("[ManageProfiles]", customValue);
    }

    @Test
    public void should_getProperty_return_the_right_permissions_with_trailing_spaces() {
        final ConfigurationFile tenantProperties = spy(new ConfigurationFile(COMPOUND_PERMISSIONS_MAPPING_FILE));
        doReturn(compoundProperties).when(tenantProperties).getTenantPropertiesOfScope();

        final String value = tenantProperties.getTenantProperty("caseListingPage");
        final Set<String> valueAsList = tenantProperties.getPropertyAsSet("caseListingPage");

        assertEquals("caseVisualizationWithTrailingSpace", value);
        assertThat(valueAsList).containsOnly("caseVisualizationWithTrailingSpace");
    }

    @Test
    public void should_getProperty_return_null_with_unknown_permissions() {
        final ConfigurationFile tenantProperties = spy(new ConfigurationFile(COMPOUND_PERMISSIONS_MAPPING_FILE));
        doReturn(compoundProperties).when(tenantProperties).getTenantPropertiesOfScope();

        final String value = tenantProperties.getTenantProperty("unknownListingPage");
        
        Assert.assertNull(value);
    }

    @Test
    public void should_getProperty_return_the_right_compound_permissions() {
        final ConfigurationFile tenantProperties = spy(new ConfigurationFile(COMPOUND_PERMISSIONS_MAPPING_FILE));
        doReturn(compoundProperties).when(tenantProperties).getTenantPropertiesOfScope();

        final String compoundValue = tenantProperties.getTenantProperty("taskListingPage");

        assertEquals("[TaskVisualization, CaseVisualization]", compoundValue);
    }

    @Test
    public void should_getProperty_return_the_right_resource_permissions() {
        final ConfigurationFile tenantProperties = spy(new ConfigurationFile(RESOURCES_PERMISSIONS_MAPPING_FILE));
        doReturn(resourcesProperties).when(tenantProperties).getTenantPropertiesOfScope();

        final String resourcesValue = tenantProperties.getTenantProperty("GET|bpm/identity");

        assertEquals("[UserVisualization, groupVisualization]", resourcesValue);
    }

    @Test
    public void should_getProperty_return_the_right_custom_permissions() {
        final ConfigurationFile tenantProperties = spy(new ConfigurationFile(CUSTOM_PERMISSIONS_MAPPING_FILE));
        doReturn(customProperties).when(tenantProperties).getTenantPropertiesOfScope();

        final String customValue = tenantProperties.getTenantProperty("profile|User");

        assertEquals("[ManageLooknFeel, ManageProfiles]", customValue);
    }

    @Test
    public void should_getProperty_return_the_right_permissions_list() {
        final ConfigurationFile tenantProperties = spy(new ConfigurationFile(COMPOUND_PERMISSIONS_MAPPING_FILE));
        doReturn(compoundProperties).when(tenantProperties).getTenantPropertiesOfScope();

        final Set<String> compoundPermissionsList = tenantProperties.getPropertyAsSet("taskListingPage");

        assertThat(compoundPermissionsList).containsOnly("TaskVisualization", "CaseVisualization");
    }

    @Test
    public void should_getProperty_return_the_right_permissions_list_with_single_value() {
        final ConfigurationFile tenantProperties = spy(new ConfigurationFile(COMPOUND_PERMISSIONS_MAPPING_FILE));
        doReturn(compoundProperties).when(tenantProperties).getTenantPropertiesOfScope();

        final Set<String> compoundPermissionsList = tenantProperties.getPropertyAsSet("processListingPage");

        assertThat(compoundPermissionsList).containsOnly("processVisualization");
    }

}
