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

import java.util.List;

import org.bonitasoft.engine.search.Order;
import org.bonitasoft.web.rest.server.datastore.utils.Sort;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class SortsTest {

    @Test
    public void testNullSortListing() throws Exception {
        Sorts sorts = new Sorts(null);

        List<Sort> sortList = sorts.asList();

        Assert.assertTrue(sortList.isEmpty());
    }

    @Test
    public void testEmptySortListing() throws Exception {
        Sorts sorts = new Sorts("");

        List<Sort> sortList = sorts.asList();

        Assert.assertTrue(sortList.isEmpty());
    }

    @Test
    public void testSingleSortListing() throws Exception {
        Sorts sorts = new Sorts("attribute " + Order.ASC);

        List<Sort> sortList = sorts.asList();

        Assert.assertEquals(new Sort("attribute " + Order.ASC), sortList.get(0));
    }

    @Test
    public void testMultipleSortListing() throws Exception {
        String attribute1Order = "attribute1 " + Order.ASC;
        String attribute2Order = "attribute2 " + Order.DESC;
        Sorts sorts = new Sorts(attribute1Order + "," + attribute2Order);

        List<Sort> sortList = sorts.asList();

        Assert.assertEquals(new Sort(attribute1Order), sortList.get(0));
        Assert.assertEquals(new Sort(attribute2Order), sortList.get(1));
    }
}
