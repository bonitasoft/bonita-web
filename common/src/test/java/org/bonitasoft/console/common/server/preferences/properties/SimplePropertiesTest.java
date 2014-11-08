package org.bonitasoft.console.common.server.preferences.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimplePropertiesTest {

    private static final File COMPOUND_PERMISSIONS_MAPPING_FILE = new File("src/test/resources/compound-permissions-mapping.properties");
    private static final File RESOURCES_PERMISSIONS_MAPPING_FILE = new File("src/test/resources/resources-permissions-mapping.properties");
    private static final File CUSTOM_PERMISSIONS_MAPPING_FILE = new File("src/test/resources/custom-permissions-mapping.properties");

    @Test
    public void should_getProperty_return_the_right_compound_permissions() throws Exception {

        final SimpleProperties tenantProperties = new SimpleProperties(COMPOUND_PERMISSIONS_MAPPING_FILE);

        final String compoundValue = tenantProperties.getProperty("taskListingPage");

        Assert.assertEquals("[TaskVisualization, CaseVisualization]", compoundValue);
    }

    @Test
    public void should_getProperty_return_the_right_resource_permissions() throws Exception {

        final SimpleProperties tenantProperties = new SimpleProperties(RESOURCES_PERMISSIONS_MAPPING_FILE);

        final String resourcesValue = tenantProperties.getProperty("GET|bpm/identity");

        Assert.assertEquals("[UserVisualization, groupVisualization]", resourcesValue);
    }

    @Test
    public void should_getProperty_return_the_right_custom_permissions() throws Exception {

        final SimpleProperties tenantProperties = new SimpleProperties(CUSTOM_PERMISSIONS_MAPPING_FILE);

        final String customValue = tenantProperties.getProperty("profile|User");

        Assert.assertEquals("[ManageLooknFeel, ManageProfiles]", customValue);
    }

    @Test
    public void should_setProperty_add_the_right_compound_permissions_and_remove_remove_it() throws Exception {

        final File compoundPermissionMappingWorkFile = File.createTempFile("compound-permissions-mapping", ".properties");
        compoundPermissionMappingWorkFile.deleteOnExit();
        FileUtils.copyFile(COMPOUND_PERMISSIONS_MAPPING_FILE, compoundPermissionMappingWorkFile);

        final SimpleProperties tenantProperties = new SimpleProperties(compoundPermissionMappingWorkFile);

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

        final SimpleProperties tenantProperties = new SimpleProperties(COMPOUND_PERMISSIONS_MAPPING_FILE);

        final Set<String> compoundPermissionsList = tenantProperties.getPropertyAsSet("taskListingPage");

        assertThat(compoundPermissionsList).containsOnly("TaskVisualization", "CaseVisualization");
    }

    @Test
    public void should_getProperty_return_the_right_permissions_list_with_single_value() throws Exception {

        final SimpleProperties tenantProperties = new SimpleProperties(COMPOUND_PERMISSIONS_MAPPING_FILE);

        final Set<String> compoundPermissionsList = tenantProperties.getPropertyAsSet("processListingPage");

        assertThat(compoundPermissionsList).containsOnly("processVisualization");
    }

    @Test
    public void should_setPropertyAsSet_add_the_right_compound_permissions() throws Exception {

        final File compoundPermissionMappingWorkFile = File.createTempFile("compound-permissions-mapping", ".properties");
        compoundPermissionMappingWorkFile.deleteOnExit();
        FileUtils.copyFile(COMPOUND_PERMISSIONS_MAPPING_FILE, compoundPermissionMappingWorkFile);

        final SimpleProperties tenantProperties = new SimpleProperties(compoundPermissionMappingWorkFile);
        final Set<String> permissions = new HashSet<String>();
        permissions.add("Case Visualization");
        permissions.add("Task Visualization");
        try {
            tenantProperties.setPropertyAsSet("customPage1", permissions);

            final String compoundValue = tenantProperties.getProperty("customPage1");

            assertThat(compoundValue).isIn("[Case Visualization, Task Visualization]", "[Task Visualization, Case Visualization]");
            Assert.assertEquals("[Case Visualization, Task Visualization]", compoundValue);
        } finally {
            tenantProperties.removeProperty("customPage1");
        }

        Assert.assertNull(tenantProperties.getProperty("customPage1"));
    }

    @Test
    public void should_setPropertyAsSet_add_the_right_compound_permissions_with_an_empty_list() throws Exception {

        final File compoundPermissionMappingWorkFile = File.createTempFile("compound-permissions-mapping", ".properties");
        compoundPermissionMappingWorkFile.deleteOnExit();
        FileUtils.copyFile(COMPOUND_PERMISSIONS_MAPPING_FILE, compoundPermissionMappingWorkFile);

        final SimpleProperties tenantProperties = new SimpleProperties(compoundPermissionMappingWorkFile);
        final Set<String> permissions = new HashSet<String>();
        try {
            tenantProperties.setPropertyAsSet("customPage1", permissions);

            final String compoundValue = tenantProperties.getProperty("customPage1");

            Assert.assertEquals("[]", compoundValue);
        } finally {
            tenantProperties.removeProperty("customPage1");
        }

        Assert.assertNull(tenantProperties.getProperty("customPage1"));

    }
}
