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
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

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
        CustomUserInfoItem item = createInformation(6L, 2L);
        model.update(item, "value");

        model.flushChanges();

        verify(updateRequest).setId(APIID.makeAPIID(Arrays.asList("6", "2")));
    }

    @Test
    public void should_send_all_changed_values_on_flush() throws Exception {
        CustomUserInformationModel model = new CustomUserInformationModel(requestFactory, "6");
        CustomUserInfoItem information1 = createInformation(5L, 8L);
        CustomUserInfoItem information2 = createInformation(7L, 4L);
        model.update(information1, "value");
        model.update(information2, "value");

        model.flushChanges();

        verify(updateRequest, atLeastOnce()).setId(APIID.makeAPIID(Arrays.asList("5", "8")));
        verify(updateRequest, atLeastOnce()).setId(APIID.makeAPIID(Arrays.asList("7", "4")));
    }

    @Test
    public void should_send_an_information_value_change_only_once() throws Exception {
        CustomUserInformationModel model = new CustomUserInformationModel(requestFactory, "6");
        CustomUserInfoItem information = createInformation(4L, 1L);
        model.update(information, "value 1");
        model.update(information, "value 2");

        model.flushChanges();

        verify(updateRequest, times(1)).setId(APIID.makeAPIID(Arrays.asList("4", "1")));
        // didn't manage to verify the value. Mockito#verify throw some weird error.
    }

    private CustomUserInfoItem createInformation(long userId, long definitionId) {
        CustomUserInfoItem item = new CustomUserInfoItem();
        item.setUserId(userId);
        item.setDefinition(APIID.makeAPIID(definitionId));
        return item;
    }
}