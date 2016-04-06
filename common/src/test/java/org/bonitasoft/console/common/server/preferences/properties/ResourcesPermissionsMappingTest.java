/*
 * Copyright (C) 2014 BonitaSoft S.A.
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ResourcesPermissionsMappingTest {

    @Test
    public void testGetResourcePermission() throws Exception {
        //given
        final String fileContent = "GET|bpm/process [Process visualization, Process categories, Process actor mapping visualization, Connector visualization]\n"
                +
                "POST|bpm/process [Process Deploy]\n" +
                "POST|bpm/process/6 [Custom permission]\n" +
                "PUT|bpm/process []";
        final ResourcesPermissionsMapping resourcesPermissionsMapping = getResourcesPermissionsMapping(fileContent);

        //when
        final Set<String> getPermissions = resourcesPermissionsMapping.getResourcePermissions("GET", "bpm", "process");
        final Set<String> postPermission = resourcesPermissionsMapping.getResourcePermissions("POST", "bpm", "process");
        final Set<String> postOnSinglePermission = resourcesPermissionsMapping.getResourcePermissions("POST", "bpm", "process", Arrays.asList("6"));
        final Set<String> putPermissions = resourcesPermissionsMapping.getResourcePermissions("PUT", "bpm", "process");
        final Set<String> unknown = resourcesPermissionsMapping.getResourcePermissions("unknown", "unknown", "unknown", Arrays.asList("unknown"));

        //then
        Assertions.assertThat(getPermissions).containsOnly("Process visualization", "Process categories", "Process actor mapping visualization",
                "Connector visualization");
        Assertions.assertThat(postPermission).containsOnly("Process Deploy");
        Assertions.assertThat(postOnSinglePermission).containsOnly("Custom permission");
        Assertions.assertThat(putPermissions).isEmpty();
        Assertions.assertThat(unknown).isEmpty();
    }

    @Test
    public void testGetResourcePermissionWithWildCard() throws Exception {
        //given
        final String fileContent = "POST|bpm/process [Process Deploy]\n" +
                "POST|bpm/process/*/instantiation [Custom permission]";
        final ResourcesPermissionsMapping resourcesPermissionsMapping = getResourcesPermissionsMapping(fileContent);

        //when
        final Set<String> postWithResourcesQualifiers = resourcesPermissionsMapping.getResourcePermissionsWithWildCard("POST", "bpm", "process",
                Arrays.asList("6", "instantiation"));

        //then
        Assertions.assertThat(postWithResourcesQualifiers).containsOnly("Custom permission");
    }

    public static ResourcesPermissionsMapping getResourcesPermissionsMapping(final String fileContent) throws IOException {
        ConfigurationFilesManager.getInstance().setTenantConfigurations(Collections.singletonMap("TEST_FILE.properties", fileContent.getBytes()), 423L);
        final File resourceMappingFile = File.createTempFile("resourceMapping", ".tmp");
        IOUtils.write(fileContent.getBytes(), new FileOutputStream(resourceMappingFile));
        final ResourcesPermissionsMapping resourcesPermissionsMapping = new ResourcesPermissionsMapping("TEST_FILE.properties", 423L);
        resourceMappingFile.delete();
        return resourcesPermissionsMapping;
    }
}
