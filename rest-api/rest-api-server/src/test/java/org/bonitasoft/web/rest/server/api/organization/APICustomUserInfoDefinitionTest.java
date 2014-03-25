/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.organization;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.identity.CustomUserInfoDefinitionCreator;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClient;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class APICustomUserInfoDefinitionTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    private APIServletCall caller;

    @Mock
    private HttpSession httpSession;

    @Mock
    private APISession apiSession;

    @Mock
    private CustomUserInfoEngineClient engine;

    @Mock(answer = Answers.RETURNS_MOCKS)
    private CustomUserInfoEngineClientCreator engineClientCreator;

    @Mock
    private IdentityAPI identityApi;

    @InjectMocks
    private APICustomUserInfoDefinition api;

    @Before
    public void setUp() throws Exception {
        api.setCaller(caller);
        given(caller.getHttpSession()).willReturn(httpSession);
        given(httpSession.getAttribute("apiSession")).willReturn(apiSession);
        given(engineClientCreator.create(apiSession)).willReturn(engine);
    }

    @Test
    public void add_should_return_the_added_item() throws Exception {
        given(engine.createDefinition(any(CustomUserInfoDefinitionCreator.class)))
                .willReturn(new EngineCustomUserInfoDefinition(2L));

        CustomUserInfoDefinitionItem added = api.add(new CustomUserInfoDefinitionItem());

        assertThat(added.getId()).isEqualTo(APIID.makeAPIID(2L));
    }

    @Test
    public void add_should_call_engine_client_with_a_correct_item_creator() throws Exception {
        given(engine.createDefinition(any(CustomUserInfoDefinitionCreator.class)))
                .willReturn(new EngineCustomUserInfoDefinition(1L));
        ArgumentCaptor<CustomUserInfoDefinitionCreator> argument = ArgumentCaptor.forClass(CustomUserInfoDefinitionCreator.class);
        CustomUserInfoDefinitionItem item = new CustomUserInfoDefinitionItem();
        item.setName("foo");
        item.setDescription("bar");

        api.add(item);
        verify(engine).createDefinition(argument.capture());

        assertThat(argument.getValue().getName()).isEqualTo("foo");
        assertThat(argument.getValue().getDescription()).isEqualTo("bar");
    }

    @Test
    public void delete_should_call_engine_client_with_a_correct_item_creator() throws Exception {
        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);

        api.delete(Arrays.asList(
                APIID.makeAPIID(1L),
                APIID.makeAPIID(2L)));
        verify(engine, times(2)).deleteDefinition(argument.capture());

        assertThat(argument.getAllValues()).containsOnly(1L, 2L);
    }
}
