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
import org.bonitasoft.web.rest.server.datastore.utils.Sort;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class SortTest {

    @Test
    public void testAscSortCreation() {
        String order = "attribute " + Order.ASC;

        Sort sort = new Sort(order);

        Assert.assertEquals(sort.getField(), "attribute");
        Assert.assertEquals(sort.getOrder(), Order.ASC);
    }

    @Test
    public void testDescSortCreation() {
        String order = "attribute " + Order.DESC;

        Sort sort = new Sort(order);

        Assert.assertEquals(sort.getField(), "attribute");
        Assert.assertEquals(sort.getOrder(), Order.DESC);
    }

    @Test
    public void testDefaultSortOrder() {
        String order = "attribute";

        Sort sort = new Sort(order);

        Assert.assertEquals(sort.getField(), "attribute");
        Assert.assertEquals(sort.getOrder(), Sort.DEFAULT_ORDER);
    }

}
