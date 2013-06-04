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
package org.bonitasoft.console.server.datastore.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.bonitasoft.console.server.datastore.converter.ValueConverter;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class ValueTest {

    @Test
    public void testName() throws Exception {
        @SuppressWarnings("unchecked")
        ValueConverter<Long> converter = mock(ValueConverter.class);
        doReturn(5L).when(converter).convert("12");

        Value<Long> value = new Value<Long>("12", converter);

        assertEquals(Long.valueOf(5), value.cast());
    }
}
