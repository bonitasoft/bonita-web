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
package org.bonitasoft.web.rest.server.framework.utils.converter.typed;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.bonitasoft.web.rest.server.framework.utils.converter.ConversionException;
import org.bonitasoft.web.rest.server.framework.utils.converter.Converter;

/**
 * @author Nicolas Tith
 * 
 */
public class DateConverter implements Converter<Date> {

    @Override
    public Date convert(String toBeConverted) throws ConversionException {
        if (toBeConverted == null || toBeConverted.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", DateFormatSymbols.getInstance(Locale.ENGLISH));
            return formatter.parse(toBeConverted);
        } catch (ParseException e) {
            throw new ConversionException(toBeConverted + " cannot be converted to Date", e);
        }
    }
}
