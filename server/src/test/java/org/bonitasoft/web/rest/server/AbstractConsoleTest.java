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
package org.bonitasoft.web.rest.server;

import static junit.framework.Assert.assertTrue;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.AbstractJUnitWebTest;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.test.toolkit.server.MockHttpServletRequest;
import org.bonitasoft.test.toolkit.server.MockHttpServletResponse;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.FlowNodeConverter;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.data.item.Item;

/**
 * @author Vincent Elcrin
 */
public abstract class AbstractConsoleTest extends AbstractJUnitWebTest {

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.common.server.AbstractJUnitWebTest#webTestSetUp()
     */
    @Override
    public void webTestSetUp() throws Exception {
        FlowNodeConverter.setFlowNodeConverter(new FlowNodeConverter());

        new BonitaRestAPIServlet();
        consoleTestSetUp();
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    public APIServletCall getAPICaller(final APISession apiSession, final String apiPath) {

        // Get the httpSession and set attributes
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setPathInfo(apiPath);
        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        final HttpSession httpSession = mockHttpServletRequest.getSession();
        httpSession.setAttribute(USERNAME_SESSION_PARAM, apiSession.getUserName());
        httpSession.setAttribute(API_SESSION_PARAM_KEY, apiSession);

        // Initialize APIUser for HTTP requests of the API
        final APIServletCall caller = new APIServletCall(mockHttpServletRequest, mockHttpServletResponse);
        return caller;
    }

    public abstract void consoleTestSetUp() throws Exception;

    protected void assertItemEquals(Item expectedItem, Item actual) {
        assertTrue("expected { " + expectedItem + "} \n actual {" + actual + "}", areEquals(expectedItem, actual));
    }
}
