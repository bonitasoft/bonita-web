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
package org.bonitasoft.web.toolkit.client.common;

import java.util.Date;

/**
 * @author Paul AMAR
 * 
 */
public abstract class CommonDateFormater {

    private static CommonDateFormater formater = null;

    abstract public Date _parse(final String value, final String format);

    abstract public String _toString(final Date value, final String format);

    public static void setDateFormater(final CommonDateFormater formater) {
        CommonDateFormater.formater = formater;
    }

    public static Date parse(final String value, final String format) {
        return formater._parse(value, format);
    }

    public static String toString(final Date value, final String format) {
        return formater._toString(value, format);
    }

}
