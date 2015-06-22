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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.converter.EmptyAttributeConverter;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

/**
 * @author Vincent Elcrin
 * 
 *         Build list of sorts from a string order following the pattern
 *         <attribute asc>,<attibute desc>,...
 * 
 */
public class Sorts {

    private List<Sort> sorts;

    public Sorts(String orders, AttributeConverter converter) {
        sorts = parseOrders(orders, converter);
    }

    public Sorts(String orders) {
        this(orders, new EmptyAttributeConverter());
    }

    private List<Sort> parseOrders(final String orders, final AttributeConverter converter) {
        if (StringUtil.isBlank(orders)) {
            return new ArrayList<Sort>();
        } else {
            return buildSortList(orders, converter);
        }
    }

    /**
     * Convert orders and build list of sort adding items to sorts parameter
     * 
     * @param sorts
     * @param orders
     * @param converter
     */
    private List<Sort> buildSortList(final String orders, final AttributeConverter converter) {
        final List<Sort> sorts = new ArrayList<Sort>();
        for (String order : Arrays.asList(orders.split(","))) {
            sorts.add(new Sort(order, converter));
        }
        return sorts;
    }

    public List<Sort> asList() {
        return sorts;
    }

}
