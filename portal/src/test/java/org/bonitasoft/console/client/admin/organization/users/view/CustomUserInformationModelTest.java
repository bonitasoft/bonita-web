/**
 * Copyright (C) 2014 Bonitasoft S.A.
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
package org.bonitasoft.console.client.admin.organization.users.view;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.bonitasoft.console.client.mvp.model.RequestFactory;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.APIUpdateRequest;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

@RunWith(MockitoJUnitRunner.class)
public class CustomUserInformationModelTest {

    @Mock
    private RequestFactory requestFactory;

    @Mock
    private APISearchRequest searchRequest;

    @Mock
    private APIUpdateRequest updateRequest;

    @Before
    public void setUp() throws Exception {
        given(requestFactory.createSearch(any(ItemDefinition.class))).willReturn(searchRequest);
        given(requestFactory.createUpdate(any(ItemDefinition.class))).willReturn(updateRequest);
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
    }

    @Test
    public void should_search_for_the_user_custom_information() throws Exception {
        CustomUserInformationModel model = new CustomUserInformationModel(requestFactory, "3");

        model.search(4, 1, null);

        verify(searchRequest).setPage(4);
        verify(searchRequest).setResultsPerPage(1);
        verify(searchRequest).addFilter("userId", "3");
    }

    @Test
    public void should_send_value_change_on_flush() throws Exception {
        CustomUserInformationModel model = new CustomUserInformationModel(requestFactory, "6");
        CustomUserInfoItem item = new CustomUserInfoItem();
        item.setUserId(6L);
        item.setDefinition(APIID.makeAPIID("2"));
        model.update(item, "value");

        model.flushChanges();

        verify(updateRequest).setId(APIID.makeAPIID(Arrays.asList("6", "2")));
    }
}