/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.filter;

import java.io.Serializable;

import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute.TYPE;

/**
 * @author Vincent Elcrin
 * @author Emmanuel Duchastenier
 */
public class GenericFilterCreator implements FilterCreator {

    protected AttributeConverter fieldConverter;

    public GenericFilterCreator(AttributeConverter fieldConverter) {
        this.fieldConverter = fieldConverter;
    }

    @Override
    public Filter<? extends Serializable> create(String attribute, String value) {
        return new Filter<>(new Field(attribute, fieldConverter), getTypedValue(attribute, value));
    }

    private Value<? extends Serializable> getTypedValue(String attributeName, String attributeValue) {
        if (fieldConverter.getValueTypeMapping().get(attributeName) == TYPE.BOOLEAN) {
            return new BooleanValue(attributeValue);
        }
        return new StrValue(attributeValue);
    }

}
