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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.bonitasoft.web.rest.server.framework.utils.converter.typed.BooleanConverter;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class BooleanConverterTest {

    private BooleanConverter converter;

    @Before
    public void initConverter() {
        converter = new BooleanConverter();
    }
    
    @Test
    public void trueIsConvertedToTrue() throws Exception {

        Boolean converted = converter.convert("true");

        assertTrue(converted);
    }

    @Test
    public void falseIsConvertedToFalse() throws Exception {

        Boolean converted = converter.convert("false");

        assertFalse(converted);
    }

    @Test
    public void nullIsConvertedToNull() throws Exception {

        Boolean converted = converter.convert(null);

        assertNull(converted);
    }
    
    @Test
    public void emptyIsConvertedToNull() throws Exception {

        Boolean converted = converter.convert("");

        assertNull(converted);
    }
    
    @Test
    public void anythingElseIsConvertedToFalse() throws Exception {

        Boolean converted = converter.convert("something");

        assertFalse(converted);
    }
}
