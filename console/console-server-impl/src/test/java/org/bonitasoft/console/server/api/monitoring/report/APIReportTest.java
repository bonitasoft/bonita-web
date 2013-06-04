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
package org.bonitasoft.console.server.api.monitoring.report;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.client.model.monitoring.report.ReportItem;
import org.bonitasoft.console.server.api.deployer.DeployerFactory;
import org.bonitasoft.console.server.api.deployer.UserDeployer;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.APIServletCall;
import org.bonitasoft.web.toolkit.server.Deployer;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class APIReportTest {

    @Mock
    APIServletCall caller;

    @Mock
    HttpSession httpSession;

    @Mock
    APISession session;

    @Mock
    ProcessAPI processApi;

    @Mock
    Deployer deployer;

    @Mock
    DeployerFactory deployerFactory;

    @Mock
    UserDeployer userDeployer;

    APIReport api;

    // @Before
    // public void setUp() {
    // initMocks(this);
    //
    // ApplicationFactoryCommon.setDefaultFactory(new ConsoleFactoryCommon());
    //
    // api = spy(new APIReport());
    // api.setCaller(caller);
    // doReturn(processApi).when(api).getProcessApi();
    //
    // doReturn(session).when(httpSession).getAttribute("apiSession");
    // doReturn(httpSession).when(caller).getHttpSession();
    //
    // doReturn(deployerFactory).when(api).getDeployerFactory();
    // doReturn(userDeployer).when(deployerFactory).createUserDeployer(anyString());
    // }
    @Ignore("Need to set bonita home since the last changes on reports")
    @Test
    public void testDeployerCanBeDeployed() {
        List<String> deploys = Arrays.asList("aDeploy");
        doReturn("aDeploy").when(deployer).getDeployedAttribute();
        api.addDeployer(deployer);

        ReportItem report = api.runGet(APIID.makeAPIID(6L), deploys, null);

        verify(deployer).deployIn(report);
    }
    // @Test
    // public void testWeCanDeployInstalledByAttribute() {
    // List<String> deploys = Arrays.asList(ReportItem.ATTRIBUTE_INSTALLED_BY);
    // UserItem deployedUser = new UserItem();
    // doReturn(deployedUser).when(userDatastore).get(any(APIID.class));
    //
    // ReportItem report = api.runGet(APIID.makeAPIID(6L), deploys, null);
    //
    // assertEquals(deployedUser, report.getInstalledBy());
    // }
}
