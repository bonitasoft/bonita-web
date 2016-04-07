/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.preferences.constants;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class WebBonitaConstantsUtilsTest {

    private static final String TEST_BONITA_HOME = "src/test/resources/bonita/";

    private WebBonitaConstantsUtils webBonitaConstantsUtilsWithTenantId;

    @Before
    public void setUp() throws Exception {
        // With tenantId
        webBonitaConstantsUtilsWithTenantId = spy(new WebBonitaConstantsUtils(1L));
        doReturn(TEST_BONITA_HOME).when(webBonitaConstantsUtilsWithTenantId).getBonitaHomePath();

        // Without tenantId
        WebBonitaConstantsUtils webBonitaConstantsUtils = spy(new WebBonitaConstantsUtils());
        doReturn(TEST_BONITA_HOME).when(webBonitaConstantsUtils).getBonitaHomePath();
    }

    @Test
    public void testWeCanGetFormsWorkFolder() throws Exception {
        WebBonitaConstantsTenancyImpl constantsTenants = new WebBonitaConstantsTenancyImpl(1L);
        final File expected = new File(constantsTenants.getFormsTempFolderPath());

        final File folder = webBonitaConstantsUtilsWithTenantId.getFormsWorkFolder();

        assertThat(folder.getPath()).isEqualTo(expected.getPath());
        assertThat(folder).exists();
    }

}
