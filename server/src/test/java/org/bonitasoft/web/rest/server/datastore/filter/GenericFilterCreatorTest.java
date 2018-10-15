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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * @author Emmanuel Duchastenier
 */
public class GenericFilterCreatorTest {

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void method_create_should_handle_value_as_String_by_default() {
        GenericFilterCreator creator = new GenericFilterCreator(new AttributeConverter() {

            @Override
            public String convert(String attribute) {
                return attribute;
            }

            @Override
            public Map<String, ItemAttribute.TYPE> getValueTypeMapping() {
                return Collections.emptyMap();
            }
        });

        Filter<? extends Serializable> filter = creator.create("attribute", "value");

        assertEquals("attribute", filter.getField());
        assertEquals("value", filter.getValue());
    }

    @Test
    public void method_create_should_handle_boolean_values() {
        AttributeConverter converter = new AttributeConverter() {

            @Override
            public String convert(String attribute) {
                return attribute;
            }

            @Override
            public Map<String, ItemAttribute.TYPE> getValueTypeMapping() {
                return Collections.singletonMap("myAttribute", ItemAttribute.TYPE.BOOLEAN);
            }
        };
        GenericFilterCreator creator = new GenericFilterCreator(converter);

        Filter<? extends Serializable> filter = creator.create("myAttribute", "true");

        assertThat(filter.getValue()).isEqualTo(true);
    }

}
