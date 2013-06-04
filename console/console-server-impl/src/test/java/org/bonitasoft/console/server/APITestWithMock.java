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
package org.bonitasoft.console.server;

import static junit.framework.Assert.assertTrue;

import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.server.utils.I18n;
import org.junit.BeforeClass;


/**
 * Base test class for API and Datastore tests classes using mocks
 * 
 * @author Colin PUY
 */
public class APITestWithMock {

    /**
     * Initialise minimal environment for {@link Item}
     */
    @BeforeClass
    public static void initEnvironement() {
        new ConsoleAPIServlet();
        I18n.getInstance();
    }
    
    protected boolean areEquals(Item item1, Item item2) {
        return item1.getAttributes().equals(item2.getAttributes());
    }
    
    protected void assertItemEquals(Item expectedItem, Item actual) {
        assertTrue("expected { " + expectedItem + "} \n actual {" + actual + "}" , areEquals(expectedItem, actual));
    }
}
