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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bonitasoft.web.toolkit.client.common.i18n.I18n;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.StringMinMaxLengthValidator;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Paul AMAR
 * 
 */
public class StringMinMaxLengthValidatorTest {

    @Before
    public void setUp() {
        I18n.getInstance();
    }

    @Test
    public void testLengthOK() {

        final StringMinMaxLengthValidator test = new StringMinMaxLengthValidator(1, 10);
        test.check("test!");
        assertEquals(0, test.getErrors().size());
    }

    @Test
    public void testErreurLengthInf() {

        final StringMinMaxLengthValidator test = new StringMinMaxLengthValidator(1, 10);
        test.check("");
        assertFalse(test.hasError());
    }

    @Test
    public void testErreurLengthSup() {

        final StringMinMaxLengthValidator test = new StringMinMaxLengthValidator(1, 10);
        test.check("Voici le test qui va planter !");
        assertTrue(test.hasError());
    }

    @Test
    public void testErreurEgalInfOK() {

        final StringMinMaxLengthValidator test = new StringMinMaxLengthValidator(2, true, 10, true);
        test.check("OK");
        assertEquals(0, test.getErrors().size());
    }

    @Test
    public void testErreurEgalInfErreur() {

        final StringMinMaxLengthValidator test = new StringMinMaxLengthValidator(2, false, 10, true);
        test.check("NO");
        assertTrue(test.hasError());
    }

    @Test
    public void testErreurEgalSupOK() {

        final StringMinMaxLengthValidator test = new StringMinMaxLengthValidator(2, true, 10, true);
        test.check("0123456789");
        assertEquals(0, test.getErrors().size());
    }

    @Test
    public void testErreurEgalSupErreur() {

        final StringMinMaxLengthValidator test = new StringMinMaxLengthValidator(2, false, 10, false);
        test.check("0123456789");
        assertTrue(test.hasError());
    }

    @Test
    public void testEmptyString() {
        final StringMinMaxLengthValidator test = new StringMinMaxLengthValidator(2, false, 10, false);
        test.check("");
        assertFalse(test.hasError());
    }

    @Test
    public void testNullString() {
        final StringMinMaxLengthValidator test = new StringMinMaxLengthValidator(2, false, 10, false);
        test.check((String) null);
        assertFalse(test.hasError());
    }

}
