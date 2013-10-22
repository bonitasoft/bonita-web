/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.server;

import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.AbstractJUnitTest;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;

/**
 * @author Zhiheng Yang
 * 
 */
public abstract class FormsTestCase extends AbstractJUnitTest {

    private final static String PLATFORM_ADMIN = "platformAdmin";

    private final static String PLATFORM_PASSWORD = "platform";

    // protected static long tenantID = -1;

    // static {
    // final String bonitaHome = System.getProperty("bonita.home");
    // if (bonitaHome == null) {
    // System.err.println("\n\n*** Forcing bonita.home to target/bonita \n\n\n");
    // System.setProperty("bonita.home", "target/bonita/home");
    // } else {
    // System.err.println("\n\n*** bonita.home already set to: " + bonitaHome + " \n\n\n");
    // }
    // setupJNDI();
    // }
    //
    // @Before
    // public void createPlatformAndTenant() throws Exception {
    //
    // final PlatformLoginAPI platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
    // final PlatformSession platformSession = platformLoginAPI.login(PLATFORM_ADMIN, PLATFORM_PASSWORD);
    // final PlatformAPI platformAPI = PlatformAPIAccessor.getPlatformAPI(platformSession);
    // platformAPI.createAndInitializePlatform();
    // platformAPI.startNode();
    // TenantsManagementUtils.addDirectoryForTenant(TenantsManagementUtils.getDefaultTenantId(), platformSession);
    //
    // loginAPI = TenantAPIAccessor.getLoginAPI();
    // apiSession = loginAPI.login(DATASTORE_USERNAME, DATASTORE_PASSWORD);
    // Assert.assertNotNull(apiSession);
    // }
    //
    // @After
    // public void removePlatformAndTenant() throws Exception {
    //
    // final PlatformLoginAPI platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
    // PlatformSession platformSession = null;
    // platformSession = platformLoginAPI.login(PLATFORM_ADMIN, PLATFORM_PASSWORD);
    // final PlatformAPI platformAPI = PlatformAPIAccessor.getPlatformAPI(platformSession);
    // // TODO remove default tenant folder
    // platformAPI.stopNode();
    // platformAPI.cleanAndDeletePlaftorm();
    // if (platformSession != null) {
    // platformLoginAPI.logout(platformSession);
    // }
    //
    // loginAPI.logout(apiSession);
    // }

    // @Before
    // public void setUp() throws Exception {
    // final PlatformLoginAPI platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
    // final PlatformSession platformSession = platformLoginAPI.login(PLATFORM_ADMIN, PLATFORM_PASSWORD);
    // TenantsManagementUtils.addDirectoryForTenant(TenantsManagementUtils.getDefaultTenantId(), platformSession);
    // }

    // @After
    // public void tearDown() throws Exception {
    // final PlatformLoginAPI platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
    // PlatformSession platformSession = null;
    // platformSession = platformLoginAPI.login(PLATFORM_ADMIN, PLATFORM_PASSWORD);
    // final PlatformAPI platformAPI = PlatformAPIAccessor.getPlatformAPI(platformSession);
    // // TODO remove default tenant folder
    // platformAPI.stopNode();
    // platformAPI.cleanAndDeletePlaftorm();
    // if (platformSession != null) {
    // platformLoginAPI.logout(platformSession);
    // }
    //
    // loginAPI.logout(apiSession);
    // }

    protected APISession getSession() {
        return getInitiator().getSession();
    }

    @Override
    protected TestToolkitCtx getContext() {
        return TestToolkitCtx.getInstance();
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Override
    protected void testSetUp() throws Exception {
        TenantsManagementUtils.addDirectoryForTenant(TenantsManagementUtils.getDefaultTenantId());
    }

    @Override
    protected void testTearDown() throws Exception {
        // to be ovrriden
    }
}
