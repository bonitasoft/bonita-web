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
import static org.mockito.Mockito.spy;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class WebBonitaConstantsUtilsTest {

    private static final String TEST_BONITA_HOME = "src/test/resources/bonita/";

    private WebBonitaConstantsTenancyImpl constants;

    private WebBonitaConstantsUtils webBonitaConstantsUtils;

    @Before
    public void setUp() throws Exception {
        webBonitaConstantsUtils = spy(new WebBonitaConstantsUtils(1L));
        doReturn(TEST_BONITA_HOME).when(webBonitaConstantsUtils).getBonitaHomePath();
        
        // Those tests depends on files in test resources!
        constants = new WebBonitaConstantsTenancyImpl(1L);
    }


    @Test
    public void testWeCanGetFormsWorkFolder() throws Exception {
        File expected = new File(TEST_BONITA_HOME, constants.getFormsWorkFolderPath());

        File folder = webBonitaConstantsUtils.getFormsWorkFolder();

        assertThat(folder.getPath(), equalTo(expected.getPath()));
    }
}
