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

import java.sql.Date;

import org.bonitasoft.web.toolkit.client.common.i18n.I18n;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.DateMinMaxValidator;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Paul AMAR
 * 
 */
public class DateMinMaxValidatorTest {

    @Before
    public void setUp() {
        I18n.getInstance();
    }

    @Test
    public void testErreurNoDate() {
        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-01-10"), Date.valueOf("2012-08-18"));
        test.check("");
        assertFalse(test.hasError());
    }

    @Test
    public void testErreurDateNull() {
        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-01-10"), Date.valueOf("2012-08-18"));
        test.check((String) null);
        assertFalse(test.hasError());
    }

    @Test
    public void testDateOK() {

        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-01-10"), Date.valueOf("2012-08-18"));
        test.check("2012-07-07");
        assertEquals(0, test.getErrors().size());
    }

    @Test
    public void testErreurDateInf() {

        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-01-10"), Date.valueOf("2012-08-18"));
        test.check("2000-11-01");
        assertTrue(test.hasError());
    }

    @Test
    public void testErreurDateSup() {

        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-01-10"), Date.valueOf("2012-08-18"));
        test.check("2013-11-01");
        assertTrue(test.hasError());
    }

    @Test
    public void testErreurEgalInfOK() {

        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-01-10"), true, Date.valueOf("2012-08-18"), true);
        test.check("2012-01-10");
        assertEquals(0, test.getErrors().size());
    }

    @Test
    public void testErreurEgalInfErreur() {

        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-01-10"), false, Date.valueOf("2012-08-18"), true);
        test.check("2012-01-10");
        assertTrue(test.hasError());
    }

    @Test
    public void testErreurEgalSupOK() {

        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-01-10"), true, Date.valueOf("2012-08-18"), true);
        test.check("2012-08-18");
        assertEquals(0, test.getErrors().size());
    }

    @Test
    public void testErreurEgalSupErreur() {

        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-01-10"), false, Date.valueOf("2012-08-18"), false);
        test.check("2012-08-18");
        assertTrue(test.hasError());
    }

    // ////////////////////////////////////////////////
    // FUZZING
    // ////////////////////////////////////////////////

    @Test
    public void testErreurVerif1() {

        final DateMinMaxValidator test = new DateMinMaxValidator(Date.valueOf("2012-07-18"), false, Date.valueOf("2012-08-18"), false);
        test.check("badSyntax");
        assertTrue(test.hasError());

    }
}
