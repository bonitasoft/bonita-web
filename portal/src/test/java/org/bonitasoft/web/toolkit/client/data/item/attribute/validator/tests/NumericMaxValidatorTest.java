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
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.NumericMaxValidator;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Paul AMAR
 * 
 */
public class NumericMaxValidatorTest {

    @Before
    public void setUp() {
        I18n.getInstance();
    }

    @Test
    public void testOK() {

        final NumericMaxValidator test = new NumericMaxValidator(10D);
        test.check("5D");
        assertEquals(0, test.getErrors().size());
    }

    @Test
    public void testErreurSup() {

        final NumericMaxValidator test = new NumericMaxValidator(10D);
        test.check("11D");
        assertTrue(test.hasError());
    }

    @Test
    public void testEgalSupOK() {

        final NumericMaxValidator test = new NumericMaxValidator(10D, true);
        test.check("10D");
        assertEquals(0, test.getErrors().size());
    }

    @Test
    public void testErreurEgalSupErreur() {

        final NumericMaxValidator test = new NumericMaxValidator(10D, false);
        test.check("10D");
        assertTrue(test.hasError());
    }

    @Test
    public void testErreurEmptyString() {

        final NumericMaxValidator test = new NumericMaxValidator(10D);
        test.check("");
        assertFalse(test.hasError());
    }

    @Test
    public void testErreurNullString() {

        final NumericMaxValidator test = new NumericMaxValidator(10D);
        test.check((String) null);
        assertFalse(test.hasError());
    }

}
