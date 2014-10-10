package org.bonitasoft.console.common.server.preferences.properties;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TenantPropertiesTest {

    private static final File COMPOUND_PERMISSIONS_MAPPING_FILE = new File("src/test/resources/compound-permissions-mapping.properties");
    private static final File RESOURCES_PERMISSIONS_MAPPING_FILE = new File("src/test/resources/resources-permissions-mapping.properties");
    private static final File CUSTOM_PERMISSIONS_MAPPING_FILE = new File("src/test/resources/custom-permissions-mapping.properties");

    @Test
    public void should_getProperty_return_the_right_compound_permissions() throws Exception {

        final TenantProperties tenantProperties = new TenantProperties(COMPOUND_PERMISSIONS_MAPPING_FILE);

        final String compoundValue = tenantProperties.getProperty("taskListingPage");
        Assert.assertEquals("[TaskVisualization, CaseVisualization]", compoundValue);
    }

    @Test
    public void should_getProperty_return_the_right_resource_permissions() throws Exception {

        final TenantProperties tenantProperties = new TenantProperties(RESOURCES_PERMISSIONS_MAPPING_FILE);

        final String resourcesValue = tenantProperties.getProperty("GET|bpm/identity");
        Assert.assertEquals("[UserVisualization, groupVisualization]", resourcesValue);
    }

    @Test
    public void should_getProperty_return_the_right_custom_permissions() throws Exception {

        final TenantProperties tenantProperties = new TenantProperties(CUSTOM_PERMISSIONS_MAPPING_FILE);

        final String customValue = tenantProperties.getProperty("profile|User");
        Assert.assertEquals("[ManageLooknFeel, ManageProfiles]", customValue);
    }

    @Test
    public void should_setProperty_add_the_right_compound_permissions_and_remove_remove_it() throws Exception {

        final File compoundPermissionMappingWorkFile = File.createTempFile("compound-permissions-mapping", ".properties");
        compoundPermissionMappingWorkFile.deleteOnExit();
        FileUtils.copyFile(COMPOUND_PERMISSIONS_MAPPING_FILE, compoundPermissionMappingWorkFile);

        final TenantProperties tenantProperties = new TenantProperties(compoundPermissionMappingWorkFile);

        try {
            tenantProperties.setProperty("caseListingPage", "[CaseVisualization]");

            final String compoundValue = tenantProperties.getProperty("caseListingPage");
            Assert.assertEquals("[CaseVisualization]", compoundValue);
        } finally {
            tenantProperties.removeProperty("caseListingPage");
        }
        Assert.assertNull(tenantProperties.getProperty("caseListingPage"));
    }

    @Test
    public void should_getProperty_return_the_right_permissions_list() throws Exception {

        final TenantProperties tenantProperties = new TenantProperties(COMPOUND_PERMISSIONS_MAPPING_FILE);

        final List<String> compoundPermissionsList = tenantProperties.getPropertyAsList("taskListingPage");
        Assert.assertTrue(compoundPermissionsList.contains("TaskVisualization"));
        Assert.assertTrue(compoundPermissionsList.contains("CaseVisualization"));
    }

    @Test
    public void should_getProperty_return_the_right_permissions_list_with_single_value() throws Exception {

        final TenantProperties tenantProperties = new TenantProperties(COMPOUND_PERMISSIONS_MAPPING_FILE);

        final List<String> compoundPermissionsList = tenantProperties.getPropertyAsList("processListingPage");
        Assert.assertTrue(compoundPermissionsList.contains("processVisualization"));
    }
}
