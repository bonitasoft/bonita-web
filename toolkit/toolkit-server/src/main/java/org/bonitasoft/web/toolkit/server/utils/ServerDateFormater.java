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
package org.bonitasoft.web.toolkit.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;

/**
 * @author Paul AMAR
 * 
 */
public class ServerDateFormater extends CommonDateFormater {

    /**
     * Default Constructor.
     */
    public ServerDateFormater() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.toolkit.client.common.CommonDateFormater#_parse(java.lang.String, java.lang.String)
     */
    @Override
    public Date _parse(final String value, final String format) {
        Date d = null;
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            d = formatter.parse(value);
        } catch (final ParseException e) {
            // Exception thrown by parse method
            e.printStackTrace();
        }
        return d;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.toolkit.client.common.CommonDateFormater#_toString(java.util.Date, java.lang.String)
     */
    @Override
    public String _toString(final Date value, final String format) {
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(value);
    }

}
