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
package org.bonitasoft.web.rest.server.datastore.utils;

import org.bonitasoft.engine.search.Order;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.converter.EmptyAttributeConverter;

/**
 * Convenient object to deal with datastore sort options
 * 
 * Default sort order is ASCENDING
 * 
 * @author Colin PUY
 */
public class Sort {

    public static final Order DEFAULT_ORDER = Order.ASC;

    private static final String SEPARATOR = " ";

    private String field;

    private Order order;

    private AttributeConverter converter;

    public Sort(String sortValue, AttributeConverter converter) {
        this.converter = converter;
        field = getSortedFieldValue(sortValue);
        order = getOrder(sortValue);
    }

    public Sort(String sortValue) {
        this(sortValue, new EmptyAttributeConverter());
    }

    private Order getOrder(String sortValue) {
        String[] split = sortValue.split(SEPARATOR);
        if (split.length > 1) {
            return Order.valueOf(split[1].toUpperCase());
        }
        return DEFAULT_ORDER;
    }

    private String getSortedFieldValue(String sortValue) {
        return converter.convert(sortValue.split(SEPARATOR)[0]);
    }

    public String getField() {
        return field;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Sort other = (Sort) obj;
        if (field == null) {
            if (other.field != null)
                return false;
        } else if (!field.equals(other.field))
            return false;
        if (order != other.order)
            return false;
        return true;
    }

}
