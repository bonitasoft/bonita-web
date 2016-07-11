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
package org.bonitasoft.console.common.server;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.console.common.server.utils.PlatformManagementUtils;
import org.bonitasoft.test.toolkit.AbstractJUnitTest;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.web.toolkit.client.data.item.Item;

/**
 * @author Vincent Elcrin
 */
public abstract class AbstractJUnitWebTest extends AbstractJUnitTest {

    /**
     * the request param for the username
     */
    public static final String USERNAME_SESSION_PARAM = "username";

    public static final String API_SESSION_PARAM_KEY = "apiSession";

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.AbstractJUnitTest#testSetUp()
     */
    @Override
    protected void testSetUp() throws Exception {
        // toolkit initialization
        I18n.getInstance();
        new PlatformManagementUtils().initializePlatformConfiguration();
        webTestSetUp();
    }

    @Override
    protected TestToolkitCtx getContext() {
        return TestToolkitCtx.getInstance();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.AbstractJUnitTest#testTearDown()
     */
    @Override
    protected void testTearDown() throws Exception {
        webTestTearDown();
    }

    public abstract void webTestSetUp() throws Exception;

    protected void webTestTearDown() throws Exception {

    }

    protected boolean areEquals(Item item1, Item item2) {
        return item1.getAttributes().equals(item2.getAttributes());
    }
}
