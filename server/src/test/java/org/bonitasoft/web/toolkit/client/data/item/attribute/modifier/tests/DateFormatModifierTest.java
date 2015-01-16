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
package org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.tests;

import static org.junit.Assert.assertTrue;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.DateFormatModifier;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT;
import org.bonitasoft.web.toolkit.server.utils.ServerDateFormater;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Paul AMAR
 * 
 */
public class DateFormatModifierTest {

    @Before
    public void setUp() {
        I18n.getInstance();
        CommonDateFormater.setDateFormater(new ServerDateFormater());
    }

    // FIXME: Need to catch the Exception
    // @Test
    // public void testDateFormatModifierException() {
    // final DateFormatModifier test = new DateFormatModifier(FORMAT.FORM.DISPLAY_SHORT, FORMAT.FORM.SQL);
    // final String result = test.clean("2012-10-01");
    // }

    @Test
    public void testDateFormatModifierFormToSql() {
        final DateFormatModifier test = new DateFormatModifier(FORMAT.FORM, FORMAT.SQL);
        final String result = test.clean("12/31/2012");
        assertTrue(result.equals("2012-12-31 00:00:00.000"));
    }

    @Test
    public void testDateFormatModifierFormToDisplay() {
        final DateFormatModifier test = new DateFormatModifier(FORMAT.FORM, FORMAT.DISPLAY);
        final String result = test.clean("12/31/2012");
        assertTrue(result.equals("12/31/2012 12:00 AM"));
    }

    @Test
    public void testDateFormatModifierSqlToDisplay() {
        final DateFormatModifier test = new DateFormatModifier(FORMAT.SQL, FORMAT.DISPLAY);
        final String result = test.clean("2012-12-31 00:00:00.000");
        assertTrue(result.equals("12/31/2012 12:00 AM"));
    }

    @Test
    public void testDateFormatModifierSqlToForm() {
        final DateFormatModifier test = new DateFormatModifier(FORMAT.SQL, FORMAT.FORM);
        final String result = test.clean("2012-12-31 00:00:00.000");
        assertTrue(result.equals("12/31/2012"));
    }

}
