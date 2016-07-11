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
package org.bonitasoft.web.rest.server.api.deployer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.model.monitoring.report.ReportItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class UserDeployerTest extends APITestWithMock {

    @Mock
    private DatastoreHasGet<UserItem> getter;

    @Before
    public void setUp() {
        initMocks(this);
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
    }

    @Test
    public void testDeployableAttributeIsDeployed() throws Exception {
        UserItem user = prepareGetterToReturnAUser();
        ReportItem report = aReportInstalledBy(APIID.makeAPIID(6L));

        UserDeployer installedByDeployer = new UserDeployer(getter, ReportItem.ATTRIBUTE_INSTALLED_BY);
        installedByDeployer.deployIn(report);

        assertEquals(user, report.getInstalledBy());
    }

    @Test
    public void testNotDeployableAttributeIsNotDeployed() throws Exception {
        prepareGetterToReturnAUser();
        ReportItem report = aReportInstalledBy(null);

        UserDeployer nameDeployer = new UserDeployer(getter, ReportItem.ATTRIBUTE_INSTALLED_BY);
        nameDeployer.deployIn(report);

        assertNull(report.getInstalledBy());
    }

    private UserItem prepareGetterToReturnAUser() {
        UserItem user = new UserItem();
        doReturn(user).when(getter).get(any(APIID.class));
        return user;
    }

    private ReportItem aReportInstalledBy(APIID userId) {
        ReportItem item = new ReportItem();
        item.setInstalledBy(userId);
        return item;
    }

}
