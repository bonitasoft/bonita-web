package org.bonitasoft.console.common.server.preferences.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Set;

import org.bonitasoft.engine.commons.io.IOUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationFileTest {

    public static final long TENANT_ID = 12L;
    public static final String COMPOUND_PERMISSIONS_MAPPING_FILE = "compound.properties";
    public static final String RESOURCES_PERMISSIONS_MAPPING_FILE = "resources.properties";
    public static final String CUSTOM_PERMISSIONS_MAPPING_FILE = "custom.properties";

    @Before
    public void before() throws Exception {
        HashMap<String, byte[]> tenantConfigurationMap = new HashMap<>();
        tenantConfigurationMap.put(COMPOUND_PERMISSIONS_MAPPING_FILE,
                IOUtil.getAllContentFrom(ConfigurationFileTest.class.getResourceAsStream("/compound-permissions-mapping.properties")));
        tenantConfigurationMap.put(RESOURCES_PERMISSIONS_MAPPING_FILE,
                IOUtil.getAllContentFrom(ConfigurationFileTest.class.getResourceAsStream("/resources-permissions-mapping.properties")));
        tenantConfigurationMap.put(CUSTOM_PERMISSIONS_MAPPING_FILE,
                IOUtil.getAllContentFrom(ConfigurationFileTest.class.getResourceAsStream("/custom-permissions-mapping.properties")));
        ConfigurationFilesManager.getInstance().setTenantConfigurations(tenantConfigurationMap, TENANT_ID);
    }

    @Test
    public void should_getProperty_return_the_right_custom_permissions_with_special_characters() throws Exception {
        final ConfigurationFile tenantProperties = new ConfigurationFile(CUSTOM_PERMISSIONS_MAPPING_FILE, TENANT_ID);
        final String customValue = tenantProperties.getProperty("profile|HR manager");
        Assert.assertEquals("[ManageProfiles]", customValue);
    }

    @Test
    public void should_getProperty_return_the_right_compound_permissions() throws Exception {

        final ConfigurationFile tenantProperties = new ConfigurationFile(COMPOUND_PERMISSIONS_MAPPING_FILE, TENANT_ID);

        final String compoundValue = tenantProperties.getProperty("taskListingPage");

        Assert.assertEquals("[TaskVisualization, CaseVisualization]", compoundValue);
    }

    @Test
    public void should_getProperty_return_the_right_resource_permissions() throws Exception {

        final ConfigurationFile tenantProperties = new ConfigurationFile(RESOURCES_PERMISSIONS_MAPPING_FILE, TENANT_ID);

        final String resourcesValue = tenantProperties.getProperty("GET|bpm/identity");

        Assert.assertEquals("[UserVisualization, groupVisualization]", resourcesValue);
    }

    @Test
    public void should_getProperty_return_the_right_custom_permissions() throws Exception {

        final ConfigurationFile tenantProperties = new ConfigurationFile(CUSTOM_PERMISSIONS_MAPPING_FILE, TENANT_ID);

        final String customValue = tenantProperties.getProperty("profile|User");

        Assert.assertEquals("[ManageLooknFeel, ManageProfiles]", customValue);
    }

    @Test
    public void should_getProperty_return_the_right_permissions_list() throws Exception {

        final ConfigurationFile tenantProperties = new ConfigurationFile(COMPOUND_PERMISSIONS_MAPPING_FILE, TENANT_ID);

        final Set<String> compoundPermissionsList = tenantProperties.getPropertyAsSet("taskListingPage");

        assertThat(compoundPermissionsList).containsOnly("TaskVisualization", "CaseVisualization");
    }

    @Test
    public void should_getProperty_return_the_right_permissions_list_with_single_value() throws Exception {

        final ConfigurationFile tenantProperties = new ConfigurationFile(COMPOUND_PERMISSIONS_MAPPING_FILE, TENANT_ID);

        final Set<String> compoundPermissionsList = tenantProperties.getPropertyAsSet("processListingPage");

        assertThat(compoundPermissionsList).containsOnly("processVisualization");
    }

}
