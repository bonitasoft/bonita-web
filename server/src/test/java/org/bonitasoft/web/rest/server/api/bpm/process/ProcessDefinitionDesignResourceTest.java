/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.bpm.process;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.impl.internal.DesignProcessDefinitionImpl;
import org.bonitasoft.web.rest.server.utils.ResponseAssert;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class ProcessDefinitionDesignResourceTest extends RestletTest {

    private static final long PROCESS_DEFINITION_ID = 4L;

    private final String TEST_DESIGN_API_URL = "/bpm/process/" + PROCESS_DEFINITION_ID + "/design";

    private ProcessDefinitionDesignResource processDefinitionDesignResource;

    @Mock
    private ProcessAPI processAPI;

    @Before
    public void initializeMocks() {
        processDefinitionDesignResource = spy(new ProcessDefinitionDesignResource(processAPI));
    }

    @Override
    protected ServerResource configureResource() {
        return new ProcessDefinitionDesignResource(processAPI);
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_attribute_is_not_found() throws Exception {
        // given:
        doReturn(null).when(processDefinitionDesignResource).getAttribute(anyString());

        // when:
        processDefinitionDesignResource.getDesign();
    }

    @Test
    public void should_return_a_contract_for_a_given_process_definition_id() throws Exception {
        //given
        final DesignProcessDefinitionImpl design = new DesignProcessDefinitionImpl("test", "1.0");

        when(processAPI.getDesignProcessDefinition(PROCESS_DEFINITION_ID)).thenReturn(design);

        //when
        final Response response = request(TEST_DESIGN_API_URL).get();

        //then
        ResponseAssert.assertThat(response).hasStatus(Status.SUCCESS_OK);
        ResponseAssert.assertThat(response).hasJsonEntityEqualTo(readFile("design.json"));
    }

    @Test
    public void should_respond_404_Not_found_when_process_definition_is_not_found_when_getting_contract() throws Exception {
        when(processAPI.getDesignProcessDefinition(PROCESS_DEFINITION_ID)).thenThrow(new ProcessDefinitionNotFoundException("process definition not found"));
        final Response response = request(TEST_DESIGN_API_URL).get();
        ResponseAssert.assertThat(response).hasStatus(Status.CLIENT_ERROR_NOT_FOUND);
    }

    @Test
    public void testReplaceLongIdToString() throws Exception {
        assertThat(processDefinitionDesignResource.replaceLongIdToString("{ \"id\": 123}")).isEqualToIgnoringCase("{ \"id\": \"123\"}");
        assertThat(processDefinitionDesignResource.replaceLongIdToString("{ \"id\":123, \"test\": [ otherid: \"zerze\"]}")).isEqualToIgnoringCase(
                "{ \"id\":\"123\", \"test\": [ otherid: \"zerze\"]}");
        assertThat(processDefinitionDesignResource.replaceLongIdToString("{ \"iaed\": 123}")).isEqualToIgnoringCase("{ \"iaed\": 123}");
        assertThat(processDefinitionDesignResource.replaceLongIdToString("{ \"name\": \"\\\"id\\\": 123\"}")).isEqualToIgnoringCase(
                "{ \"name\": \"\\\"id\\\": 123\"}");
        assertThat(processDefinitionDesignResource.replaceLongIdToString("{ \"name\": \"\\\"id\\\": 123\"}")).isEqualToIgnoringCase(
                "{ \"name\": \"\\\"id\\\": 123\"}");
    }

}
