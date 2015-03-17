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
package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.rest.server.utils.ResponseAssert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskExecutionResourceTest extends RestletTest {

    private static final String VALID_COMPLEX_POST_BODY = "{\"aBoolean\":true, \"aString\":\"hello world\", \"a_complex_type\":{\"aNumber\":2, \"aBoolean\":false}}";

    private static final String VALID_POST_BODY = "{ \"key\": \"value\", \"key2\": \"value2\" }";

    @Mock
    private ProcessAPI processAPI;

    @Override
    protected ServerResource configureResource() {
        return new UserTaskExecutionResource(processAPI);
    }

    private Map<String, Serializable> aComplexInput() {
        final HashMap<String, Serializable> aComplexInput = new HashMap<>();
        aComplexInput.put("aBoolean", true);
        aComplexInput.put("aString", "hello world");

        final HashMap<String, Serializable> childMap = new HashMap<>();
        childMap.put("aNumber", 2);
        childMap.put("aBoolean", false);

        aComplexInput.put("a_complex_type", childMap);

        return aComplexInput;
    }

    @Test
    public void should_execute_a_task_with_given_inputs() throws Exception {
        final Map<String, Serializable> expectedComplexInput = aComplexInput();

        final Response response = request("/bpm/userTask/2/execution").post(VALID_COMPLEX_POST_BODY);

        assertThat(response).hasStatus(Status.SUCCESS_NO_CONTENT);
        verify(processAPI).executeUserTask(2L, expectedComplexInput);
    }

    @Test
    public void should_respond_400_Bad_request_when_contract_is_not_validated_when_executing_a_task() throws Exception {
        doThrow(new ContractViolationException("aMessage", asList("first explanation", "second explanation")))
        .when(processAPI).executeUserTask(anyLong(), anyMapOf(String.class, Serializable.class));

        final Response response = request("/bpm/userTask/2/execution").post(VALID_POST_BODY);

        assertThat(response).hasStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        assertThat(response)
                .hasJsonEntityEqualTo(
                        "{\"exception\":\"class org.bonitasoft.engine.bpm.contract.ContractViolationException\",\"message\":\"aMessage\",\"explanations\":[\"first explanation\",\"second explanation\"]}");
    }

    @Test
    public void should_respond_500_Internal_server_error_when_error_occurs_on_task_execution() throws Exception {
        doThrow(new FlowNodeExecutionException("aMessage"))
        .when(processAPI).executeUserTask(anyLong(), anyMapOf(String.class, Serializable.class));

        final Response response = request("/bpm/userTask/2/execution").post(VALID_POST_BODY);

        assertThat(response).hasStatus(Status.SERVER_ERROR_INTERNAL);
    }

    @Test
    public void should_respond_400_Bad_request_when_trying_to_execute_with_not_json_payload() throws Exception {
        final Response response = request("/bpm/userTask/2/execution").post("invalid json string");

        assertThat(response).hasStatus(Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Test
    public void should_respond_404_Not_found_when_task_is_not_found_when_trying_to_execute_it() throws Exception {
        doThrow(new UserTaskNotFoundException("task not found")).when(processAPI)
        .executeUserTask(anyLong(), anyMapOf(String.class, Serializable.class));

        final Response response = request("/bpm/userTask/2/execution").post(VALID_POST_BODY);

        assertThat(response).hasStatus(Status.CLIENT_ERROR_NOT_FOUND);
    }
}
