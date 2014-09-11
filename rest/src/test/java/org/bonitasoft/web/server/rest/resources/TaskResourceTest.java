/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.server.rest.resources;

import static java.util.Arrays.asList;
import static javax.ws.rs.client.Entity.json;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.web.server.rest.assertions.ResponseAssert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.contract.Input;
import org.bonitasoft.engine.bpm.contract.Type;
import org.bonitasoft.engine.bpm.contract.impl.ContractDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.InputDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.RuleDefinitionImpl;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.web.server.rest.utils.BonitaJerseyTest;
import org.junit.Test;
import org.mockito.Mock;

public class TaskResourceTest extends BonitaJerseyTest {

    private static final int HTTP_CODE_200 = 200;
    private static final int HTTP_CODE_400 = 400;
    private static final int HTTP_CODE_404 = 404;
    private static final int HTTP_CODE_500 = 500;

    @Mock
    private ProcessAPI processAPI;

    @Override
    protected Application configure() {
        return super.config().register(new TaskResource(processAPI));
    }

    private Entity<Map<String, Object>> someJson() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");
        return json(map);
    }

    @Test
    public void should_return_a_contract_for_a_given_task_instance() throws Exception {
        ContractDefinitionImpl contract = new ContractDefinitionImpl();
        contract.addInput(new InputDefinitionImpl("anInput", Type.TEXT, "aDescription"));
        contract.addRule(new RuleDefinitionImpl("aRule", "an expression", "an explanation"));
        when(processAPI.getUserTaskContract(2L)).thenReturn(contract);

        Response response = target("tasks/2/contract").request().get();

        assertThat(response).hasStatus(HTTP_CODE_200);
        assertThat(response).hasJsonBodyEqual(readFile("contract.json"));
    }

    @Test
    public void should_respond_404_Not_found_when_task_is_not_found_when_getting_contract() throws Exception {
        when(processAPI.getUserTaskContract(2L)).thenThrow(new UserTaskNotFoundException("task 2 not found"));

        Response response = target("tasks/2/contract").request().get();

        assertThat(response).hasStatus(HTTP_CODE_404);
    }

    @Test
    public void should_execute_a_task_with_given_inputs() throws Exception {
        //given
        String payload = readFile("executeTask.json");

        List<Input> expectedInputs = new ArrayList<Input>();
        expectedInputs.add(new Input("aBoolean", true));
        expectedInputs.add(new Input("aString", "hello world"));

        //when
        target("tasks/2/execute").request().post(Entity.json(payload));

        //then
        verify(processAPI).executeUserTask(2L, expectedInputs);
    }

    @Test
    public void should_respond_400_Bad_request_when_contract_is_not_validated_when_executing_a_task() throws Exception {
        doThrow(new ContractViolationException("aMessage", asList("first explanation", "second explanation"))).when(processAPI).executeUserTask(anyLong(),
                anyListOf(Input.class));

        Response response = target("tasks/2/execute").request().post(someJson());

        assertThat(response).hasStatus(HTTP_CODE_400);
    }

    @Test
    public void should_respond_500_Internal_server_error_when_error_occurs_on_task_execution() throws Exception {
        doThrow(new FlowNodeExecutionException("aMessage")).when(processAPI).executeUserTask(anyLong(), anyListOf(Input.class));

        Response response = target("tasks/2/execute").request().post(someJson());

        assertThat(response.getStatus()).isEqualTo(HTTP_CODE_500);
    }

    @Test
    public void should_respond_500_Internal_server_error_when_null_parameters() throws Exception {
        doThrow(new FlowNodeExecutionException("aMessage")).when(processAPI).executeUserTask(anyLong(), anyListOf(Input.class));

        Response response = target("tasks/2/execute").request().post(null);

        assertThat(response.getStatus()).isEqualTo(HTTP_CODE_500);
    }

    @Test
    public void should_respond_400_Bad_request_when_not_json() throws Exception {
        doThrow(new FlowNodeExecutionException("aMessage")).when(processAPI).executeUserTask(anyLong(), anyListOf(Input.class));

        Response response = target("tasks/2/execute").request().post(json("not json"));

        assertThat(response.getStatus()).isEqualTo(HTTP_CODE_400);
    }

    @Test
    public void should_respond_404_Not_found_when_task_is_not_found_when_trying_to_execute_it() throws Exception {
        doThrow(new UserTaskNotFoundException("task not found")).when(processAPI).executeUserTask(anyLong(), anyListOf(Input.class));

        Response response = target("tasks/2/execute").request().post(someJson());

        assertThat(response.getStatus()).isEqualTo(HTTP_CODE_404);
    }
}
