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
package org.bonitasoft.web.rest.server.api.bpm.process.script;

import static org.bonitasoft.web.rest.server.utils.ResponseAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class ProcessScriptResourceTest extends RestletTest {

    private static final long PROCESS_DEFINITION_ID = 4L;

    private static final long SCRIPT_ID = 654L;

    private final String TEST_SCRIPT_API_URL = "/bpm/process/" + PROCESS_DEFINITION_ID + "/script/" + SCRIPT_ID;

    private ProcessScriptResource processScriptResource;

    @Mock
    private ProcessAPI processAPI;

    @Before
    public void initializeMocks() {
        processScriptResource = spy(new ProcessScriptResource(processAPI));
    }

    @Override
    protected ServerResource configureResource() {
        return new ProcessScriptResource(processAPI);
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_attribute_is_not_found() throws Exception {
        // given:
        doReturn(null).when(processScriptResource).getAttribute(anyString());

        // when:
        processScriptResource.updateScript(new ScriptDefinition());
    }

    @Test
    public void should_return_a_contract_for_a_given_process_definition_id() throws Exception {
        //given
        final String scriptDef = readFile("scriptDefinition.json");
        //when
        final Response response = request(TEST_SCRIPT_API_URL).put(scriptDef);

        //then
        assertThat(response).hasStatus(Status.SUCCESS_NO_CONTENT);
    }

    @Test
    @Ignore
    public void should_respond_500_when_update_failed() throws Exception {
        final Response response = request(TEST_SCRIPT_API_URL).get();
        assertThat(response).hasStatus(Status.CLIENT_ERROR_NOT_FOUND);
    }

}
