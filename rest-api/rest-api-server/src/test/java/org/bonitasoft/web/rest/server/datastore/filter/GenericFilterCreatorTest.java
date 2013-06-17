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
package org.bonitasoft.web.rest.server.datastore.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.bonitasoft.web.rest.server.datastore.filter.GenericFilterCreator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class GenericFilterCreatorTest {

    @Mock
    AttributeConverter converter;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testGenericFilterCreator() throws Exception {
        when(converter.convert("attribute"))
                .thenReturn("attribute");
        GenericFilterCreator creator = new GenericFilterCreator(converter);

        Filter<String> filter = creator.create("attribute", "value");

        assertEquals("attribute", filter.getField());
        assertEquals("value", filter.getValue());
    }
}
