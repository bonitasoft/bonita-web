/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.framework.utils.converter.typed;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.bonitasoft.web.rest.server.framework.utils.converter.ConversionException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Nicolas Tith
 *
 */
public class DateConverterTest {

    private DateConverter converter;

    @Before
    public void initConverter() {
        converter = new DateConverter();
    }

    @Test
    public void nullIsConvertedToNull() throws Exception {

        Date converted = converter.convert(null);

        assertNull(converted);
    }

    @Test
    public void emptyIsConvertedToNull() throws Exception {

        Date converted = converter.convert("");

        assertNull(converted);
    }

    @Test
    public void shouldConvertDateStringIntoDate() throws Exception {

        Calendar c = Calendar.getInstance();
        int hourOfDay = 11;
        int minute = 43;
        int second = 30;
        int dayOfDateate = 18;
        int year = 2014;
        c.set(year, Calendar.AUGUST, dayOfDateate, hourOfDay, minute, second);
        String timeZone = "GMT";
        c.setTimeZone(TimeZone.getTimeZone(timeZone));
        c.set(Calendar.MILLISECOND, 0);
        Date date = c.getTime();

        Date converted = converter.convert("Mon Aug " + dayOfDateate + " " + hourOfDay + ":" + minute + ":" + second + " " + timeZone + " " + year);

        assertEquals(date.toString() + " is not well converted", date, converted);
    }

    @Test(expected = ConversionException.class)
    public void nonParsableStringThrowAConversionException() throws Exception {
        converter.convert("nonParsable");
    }
}
