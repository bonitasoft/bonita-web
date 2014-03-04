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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class WebBonitaConstantsUtilsTest {

    private WebBonitaConstantsTenancyImpl constants;

    @Before
    public void setUp() throws Exception {
        // Those tests depends on files in test resources!
        constants = new WebBonitaConstantsTenancyImpl(1L);
    }

    @Test
    public void testWeCanGetFormsWorkFolder() throws Exception {
        File expected = new File(System.getProperty("bonita.home") + "/" + constants.getFormsWorkFolderPath());

        File folder = WebBonitaConstantsUtils.getInstance(1L).getFormsWorkFolder();

        assertTrue(folder.exists());
        assertEquals(expected.getPath(), folder.getPath());
    }

    @Ignore("Screw next test up. Need to be rewritten")
    @Test(expected = RuntimeException.class)
    public void testWeGetAnExceptionWhenBonitaHomeIsntSet() throws Exception {
        System.clearProperty(WebBonitaConstants.BONITA_HOME);

        WebBonitaConstantsUtils.getInstance(1L).getFormsWorkFolder();
    }
}
