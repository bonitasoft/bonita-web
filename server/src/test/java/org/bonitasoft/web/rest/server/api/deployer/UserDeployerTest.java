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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
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
    public void testDeployableAttributeIsDeployed() {
        UserItem user = prepareGetterToReturnAUser();
        GroupItem group = aGroupInstalledBy(APIID.makeAPIID(6L));

        UserDeployer installedByDeployer = new UserDeployer(getter, GroupItem.ATTRIBUTE_CREATED_BY_USER_ID);
        installedByDeployer.deployIn(group);

        assertEquals(user, group.getCreatedByUser());
    }

    @Test
    public void testNotDeployableAttributeIsNotDeployed() {
        prepareGetterToReturnAUser();
        GroupItem group = spy(aGroupInstalledBy(null));

        UserDeployer nameDeployer = new UserDeployer(getter, GroupItem.ATTRIBUTE_CREATED_BY_USER_ID);
        nameDeployer.deployIn(group);

        verify(group, never()).setDeploy(any(), any());
    }

    private UserItem prepareGetterToReturnAUser() {
        UserItem user = new UserItem();
        doReturn(user).when(getter).get(any(APIID.class));
        return user;
    }

    private GroupItem aGroupInstalledBy(APIID userId) {
        GroupItem item = new GroupItem();
        item.setCreatedByUserId(userId);
        return item;
    }

}
