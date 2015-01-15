/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.preferences.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Vincent Elcrin
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WebBonitaConstantsUtilsTest {

    private static final String TEST_BONITA_HOME = "src/test/resources/bonita/";

    @Mock
    private WebBonitaConstantsTenancyImpl constantsTenants;

    @Mock
    private File folder;

    private WebBonitaConstantsUtils webBonitaConstantsUtilsWithTenantId;

    private WebBonitaConstantsUtils webBonitaConstantsUtils;

    @Before
    public void setUp() throws Exception {
        // With tenantId
        webBonitaConstantsUtilsWithTenantId = spy(new WebBonitaConstantsUtils(1L));
        doReturn(TEST_BONITA_HOME).when(webBonitaConstantsUtilsWithTenantId).getBonitaHomePath();

        // Without tenantId
        webBonitaConstantsUtils = spy(new WebBonitaConstantsUtils());
        doReturn(TEST_BONITA_HOME).when(webBonitaConstantsUtils).getBonitaHomePath();
    }

    @Test
    public void testWeCanGetFormsWorkFolder() throws Exception {
        constantsTenants = new WebBonitaConstantsTenancyImpl(1L);
        final File expected = new File(TEST_BONITA_HOME, constantsTenants.getFormsWorkFolderPath());

        final File folder = webBonitaConstantsUtilsWithTenantId.getFormsWorkFolder();

        assertThat(folder.getPath(), equalTo(expected.getPath()));
    }

    @Test
    public void getFolder_should_call_createFolderIfNecessary() throws Exception {
        // Given
        when(folder.exists()).thenReturn(false);

        // When
        webBonitaConstantsUtils.getFolder(folder);

        // Then
        verify(webBonitaConstantsUtils).createFolderIfNecessary(folder);
    }

    @Test
    public void getFolder_should_not_call_createFolderIfNecessary() throws Exception {
        // Given
        when(folder.exists()).thenReturn(true);

        // When
        webBonitaConstantsUtils.getFolder(folder);

        // Then
        verify(webBonitaConstantsUtils, never()).createFolderIfNecessary(folder);
    }

    @Test
    public void createFolderIfNecessary_should_create_a_folder_for_the_platform() throws Exception {
        // When
        webBonitaConstantsUtils.createFolderIfNecessary(folder);

        // Then
        verify(folder).mkdirs();
        verify(webBonitaConstantsUtils, never()).tenantFolderExists();
    }

    @Test
    public void createFolderIfNecessary_should_create_a_folder_for_the_tenant() throws Exception {
        //Given
        when(webBonitaConstantsUtilsWithTenantId.tenantFolderExists()).thenReturn(true);

        // When
        webBonitaConstantsUtilsWithTenantId.createFolderIfNecessary(folder);

        // Then
        verify(folder).mkdirs();
        verify(webBonitaConstantsUtilsWithTenantId).tenantFolderExists();
    }

    @Test
    public void createFolderIfNecessary_should_not_create_a_folder() throws Exception {
        // Given
        when(webBonitaConstantsUtilsWithTenantId.tenantFolderExists()).thenReturn(false);

        // When
        webBonitaConstantsUtilsWithTenantId.createFolderIfNecessary(folder);

        // Then
        verify(folder, never()).mkdirs();
        verify(webBonitaConstantsUtilsWithTenantId).tenantFolderExists();
    }

}
