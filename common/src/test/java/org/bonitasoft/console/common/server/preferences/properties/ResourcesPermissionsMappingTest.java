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

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.console.common.server.preferences.properties.ConfigurationFilesManager.getProperties;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ResourcesPermissionsMappingTest {

    private static final String TEST_FILE_PROPERTIES = "TEST_FILE.properties";

    @Test
    public void testGetResourcePermission() {
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
        final Set<String> postOnSinglePermission = resourcesPermissionsMapping.getResourcePermissions("POST", "bpm", "process", singletonList("6"));
        final Set<String> putPermissions = resourcesPermissionsMapping.getResourcePermissions("PUT", "bpm", "process");
        final Set<String> unknown = resourcesPermissionsMapping.getResourcePermissions("unknown", "unknown", "unknown", singletonList("unknown"));

        //then
        assertThat(getPermissions).containsOnly("Process visualization", "Process categories", "Process actor mapping visualization",
                "Connector visualization");
        assertThat(postPermission).containsOnly("Process Deploy");
        assertThat(postOnSinglePermission).containsOnly("Custom permission");
        assertThat(putPermissions).isEmpty();
        assertThat(unknown).isEmpty();
    }

    @Test
    public void testGetResourcePermissionWithWildCard() {
        //given
        final String fileContent = "POST|bpm/process/* [Process Deploy]\n" +
                "POST|bpm/process/*/instantiation [Custom permission]\n" +
                "PUT|bpm/process/*/expression [Expression update]";

        final ResourcesPermissionsMapping resourcesPermissionsMapping = getResourcesPermissionsMapping(fileContent);

        //when
        final Set<String> getWithResourcesQualifier = resourcesPermissionsMapping.getResourcePermissionsWithWildCard("GET", "bpm", "process",
                singletonList("6"));
        final Set<String> postWithResourcesQualifier = resourcesPermissionsMapping.getResourcePermissionsWithWildCard("POST", "bpm", "process",
                singletonList("6"));
        final Set<String> postWithResourcesQualifiers = resourcesPermissionsMapping.getResourcePermissionsWithWildCard("POST", "bpm", "process",
                Arrays.asList("6", "instantiation"));
        final Set<String> putWithResourcesQualifiers = resourcesPermissionsMapping.getResourcePermissionsWithWildCard("PUT", "bpm", "process",
                Arrays.asList("6", "expression", "10"));

        //then
        assertThat(getWithResourcesQualifier).isEmpty();
        assertThat(postWithResourcesQualifier).containsOnly("Process Deploy");
        assertThat(postWithResourcesQualifiers).containsOnly("Custom permission");
        assertThat(putWithResourcesQualifiers).containsOnly("Expression update");
    }

    public static ResourcesPermissionsMapping getResourcesPermissionsMapping(final String fileContent) {
        final ResourcesPermissionsMapping resourcesPermissionsMapping = spy(new ResourcesPermissionsMapping(TEST_FILE_PROPERTIES, 423L));
        doReturn(getProperties(fileContent.getBytes())).when(resourcesPermissionsMapping).getPropertiesOfScope();
        return resourcesPermissionsMapping;
    }
}
