/*
 *
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
 *
 */

package org.bonitasoft.console.common.server.preferences.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ResourcesPermissionsMappingTest {

    @Test
    public void testGetResourcePermission() throws Exception {
        //given
        String fileContent =  "GET|bpm/process [Process visualization, Process categories, Process actor mapping visualization, Connector visualization]\n" +
                "POST|bpm/process [Process Deploy]\n" +
                "POST|bpm/process/6 [Custom permission]\n" +
                "PUT|bpm/process []";
        ResourcesPermissionsMapping resourcesPermissionsMapping = getResourcesPermissionsMapping(fileContent);

        //when
        List<String> getPermissions = resourcesPermissionsMapping.getResourcePermissions("GET", "bpm", "process");
        List<String> postPermission = resourcesPermissionsMapping.getResourcePermissions("POST", "bpm", "process");
        List<String> postOnSinglePermission = resourcesPermissionsMapping.getResourcePermissions("POST", "bpm", "process", "6");
        List<String> putPermissions = resourcesPermissionsMapping.getResourcePermissions("PUT", "bpm", "process");
        List<String> unknown = resourcesPermissionsMapping.getResourcePermissions("unknown", "unknown", "unknown", "unknown");

        //then
        Assertions.assertThat(getPermissions).isEqualTo(Arrays.asList("Process visualization","Process categories","Process actor mapping visualization","Connector visualization"));
        Assertions.assertThat(postPermission).isEqualTo(Arrays.asList("Process Deploy"));
        Assertions.assertThat(postOnSinglePermission).isEqualTo(Arrays.asList("Custom permission"));
        Assertions.assertThat(putPermissions).isEmpty();
        Assertions.assertThat(unknown).isEmpty();


    }

    public static ResourcesPermissionsMapping getResourcesPermissionsMapping(String fileContent) throws IOException {
        File resourceMappingFile = File.createTempFile("resourceMapping", ".tmp");
        IOUtils.write(fileContent.getBytes(), new FileOutputStream(resourceMappingFile));
        ResourcesPermissionsMapping resourcesPermissionsMapping = new ResourcesPermissionsMapping(resourceMappingFile);
        resourceMappingFile.delete();
        return resourcesPermissionsMapping;
    }
}
