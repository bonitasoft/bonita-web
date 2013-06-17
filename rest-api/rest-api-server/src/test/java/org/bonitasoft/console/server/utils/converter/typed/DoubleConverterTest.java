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
package org.bonitasoft.console.server.utils.converter.typed;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.bonitasoft.console.server.utils.converter.ConversionException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class DoubleConverterTest {

    private DoubleConverter converter;

    @Before
    public void initConverter() {
        converter = new DoubleConverter();
    }

    @Test
    public void nullIsConvertedToNull() throws Exception {

        Double converted = converter.convert(null);

        assertNull(converted);
    }

    @Test
    public void emptyIsConvertedToNull() throws Exception {

        Double converted = converter.convert("");

        assertNull(converted);
    }

    @Test
    public void doubleNumberIsParsedToDouble() throws Exception {

        double converted = converter.convert("1.23");

        assertEquals(1.23d,  converted, 0d);
    }

    @Test(expected = ConversionException.class)
    public void nonParsableStringThrowAConversionException() throws Exception {
        converter.convert("nonParsable");
    }
}
