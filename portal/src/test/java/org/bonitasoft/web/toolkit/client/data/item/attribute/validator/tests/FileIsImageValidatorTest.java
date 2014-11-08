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
package org.bonitasoft.web.toolkit.client.data.item.attribute.validator.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bonitasoft.web.toolkit.client.common.i18n.I18n;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.FileIsImageValidator;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Paul AMAR
 * 
 */
public class FileIsImageValidatorTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        I18n.getInstance();
    }

    @Test
    public void testVide() {
        final FileIsImageValidator fileTest = new FileIsImageValidator();
        fileTest.check("");
        assertFalse(fileTest.hasError());
    }

    @Test
    public void testNull() {
        final FileIsImageValidator fileTest = new FileIsImageValidator();
        fileTest.check((String) null);
        assertFalse(fileTest.hasError());
    }

    @Test
    public void testDualPoint() {
        final FileIsImageValidator fileTest = new FileIsImageValidator();
        fileTest.check("picture.img.jpeg");
        assertFalse(fileTest.hasError());
    }

    @Test
    public void testJpeg() {
        final FileIsImageValidator fileTest = new FileIsImageValidator();
        fileTest.check("picture.jpeg");
        assertFalse(fileTest.hasError());
    }

    @Test
    public void testBmp() {
        final FileIsImageValidator fileTest = new FileIsImageValidator();
        fileTest.check("picture.bmp");
        assertFalse(fileTest.hasError());
    }

    @Test
    public void testFalseFormat() {
        final FileIsImageValidator fileTest = new FileIsImageValidator();
        fileTest.check("picture.jar");
        assertTrue(fileTest.hasError());
    }
}
