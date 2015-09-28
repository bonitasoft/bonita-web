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
package org.bonitasoft.web.toolkit.client.ui.utils.dateformat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.bonitasoft.web.toolkit.client.common.i18n.I18n;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.UNIT;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Colin PUY
 *
 */
public class RelativeStringDateFormatterTest {

    private RelativeStringDateFormatter relativeStringDateFormatter;

    @BeforeClass
    public static void setUpI18n() {
        I18n.getInstance();
    }

    @Before
    public void initFormatter() {
        relativeStringDateFormatter = new RelativeStringDateFormatter();
    }

    @Test
    public void formatInSeconds() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 32);

        final String formatedDate = relativeStringDateFormatter.format(calendar.getTimeInMillis());

        /* depending to the milliseconds when test is launched, result can be 32 or 31*/
        assertThat(formatedDate, anyOf(
                equalTo(relativeStringDateFormatter.makeRelativeString(32L, UNIT.SECOND, false)),
                equalTo(relativeStringDateFormatter.makeRelativeString(31L, UNIT.SECOND, false))));
    }

    @Test
    public void formatInMinutes() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 10);
        calendar.add(Calendar.MINUTE, 22);

        final String formatedDate = relativeStringDateFormatter.format(calendar.getTimeInMillis());

        assertEquals(relativeStringDateFormatter.makeRelativeString(22L, UNIT.MINUTE, false), formatedDate);
    }

    @Test
    public void formatInHours() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 1);
        calendar.add(Calendar.HOUR, 4);

        final String formatedDate = relativeStringDateFormatter.format(calendar.getTimeInMillis());

        assertThat(formatedDate,  equalTo(relativeStringDateFormatter.makeRelativeString(4L, UNIT.HOUR, false)));
    }

    @Test
    public void formatInDays() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);
        calendar.add(Calendar.DAY_OF_YEAR, 9);

        final String formatedDate = relativeStringDateFormatter.format(calendar.getTimeInMillis());

        assertThat(formatedDate,  equalTo(relativeStringDateFormatter.makeRelativeString(9L, UNIT.DAY, false)));
    }

    public void formatInMonth() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 4);

        final String formatedDate = relativeStringDateFormatter.format(calendar.getTimeInMillis());

        assertThat(formatedDate,  equalTo(relativeStringDateFormatter.makeRelativeString(4L, UNIT.MONTH, false)));
    }

    @Test
    public void formatInYear() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 4);
        calendar.add(Calendar.YEAR, 2);

        final String formatedDate = relativeStringDateFormatter.format(calendar.getTimeInMillis());

        assertThat(formatedDate,  equalTo(relativeStringDateFormatter.makeRelativeString(28L, UNIT.MONTH, false)));
    }

}
