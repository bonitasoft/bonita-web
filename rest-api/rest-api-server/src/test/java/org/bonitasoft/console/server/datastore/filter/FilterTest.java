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

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * @author Vincent Elcrin
 * 
 */
public class FilterTest {

    @Mock
    Field field = Mockito.mock(Field.class);

    @Mock
    @SuppressWarnings("unchecked")
    Value<String> value = Mockito.mock(Value.class);

    @Test
    public void testFilterField() throws Exception {
        Mockito.doReturn("field").when(field).toString();
        Filter<String> filter = new Filter<String>(field, value);

        Assert.assertEquals("field", filter.getField());
    }

    @Test
    public void testFilterValue() throws Exception {
        Mockito.doReturn("value").when(value).cast();
        Filter<String> filter = new Filter<String>(field, value);

        Assert.assertEquals("value", filter.getValue());
    }
}
