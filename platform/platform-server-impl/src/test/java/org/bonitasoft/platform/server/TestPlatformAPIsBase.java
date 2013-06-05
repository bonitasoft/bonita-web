/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.platform.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.test.toolkit.server.MockHttpServletRequest;
import org.bonitasoft.test.toolkit.server.MockServletContext;
import org.junit.After;
import org.junit.Before;

/**
 * @author Ruiheng Fan
 * 
 */
public class TestPlatformAPIsBase {

    /**
     * The default tenant.
     */
    protected static final String DEFAULT_TENANT = "default";

    protected static final String PLATFORM_API_SESSION = PlatformLoginServlet.PLATFORMSESSION;

    protected final String USERNAME = "platformAdmin";

    protected final String PASSWORD = "platform";

    protected final static String DATASTORE_USERNAME = "admin";

    protected final static String DATASTORE_PASSWORD = "bpm";

    protected PlatformSession platformSession = null;

    protected PlatformLoginAPI platformLoginAPI;

    private PlatformAPI platformAPI = null;

    protected HttpServletRequest myRequest = new MockHttpServletRequest();

    protected ServletContext myServletContext = new MockServletContext();

    protected HttpSession session = null;

    // protected long tenantId = -1L;

    static {
        final String bonitaHome = System.getProperty("bonita.home");
        if (bonitaHome == null) {
            System.err.println("\n\n*** Forcing bonita.home to target/bonita \n\n\n");
            System.setProperty("bonita.home", "target/bonita/home");
        } else {
            System.err.println("\n\n*** bonita.home already set to: " + bonitaHome + " \n\n\n");
        }
    }

    @Before
    public void setUp() throws Exception {
        // Before run the test case, need set the bonia.home first,
        // for example, add "-Dbonita.home=D:\BOSbuilds\BONITA_HOME_6.0" for the debug configurations argument
        this.platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
        this.platformSession = this.platformLoginAPI.login(this.USERNAME, this.PASSWORD);
        this.session = this.myRequest.getSession();
        this.session.setAttribute(PLATFORM_API_SESSION, this.platformSession);
        this.platformAPI = PlatformAPIAccessor.getPlatformAPI(this.platformSession);
        try {
            if (!this.platformAPI.isPlatformCreated()) {
                this.platformAPI.createAndInitializePlatform();
            }
        } catch (final Exception e) {
            this.platformAPI.createAndInitializePlatform();
        };
        this.platformAPI.startNode();
    }

    @After
    public void tearDown() throws Exception {
        this.platformAPI.stopNode();
        this.platformAPI.cleanAndDeletePlaftorm();
        if (this.platformSession != null) {
            this.platformLoginAPI.logout(this.platformSession);
        }
        this.session.removeAttribute(PLATFORM_API_SESSION);
        this.session.invalidate();
    }

}
