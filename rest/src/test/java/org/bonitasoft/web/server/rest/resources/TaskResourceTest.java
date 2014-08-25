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
package org.bonitasoft.web.server.rest.resources;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.web.server.rest.assertions.ResponseAssert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.contract.impl.ContractDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.InputDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.RuleDefinitionImpl;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.web.server.rest.BonitaResourceConfig;
import org.bonitasoft.web.server.rest.model.Input;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mock;

public class TaskResourceTest extends JerseyTest {

    @Mock
    private ProcessAPI processAPI;
    
    @Override
    protected Application configure() {
        initMocks(this);
        return new BonitaResourceConfig().register(new TaskResource(processAPI));
    }

    @Test
    public void should_return_a_contract_for_a_given_task_instance() throws Exception {
        ContractDefinitionImpl contract = new ContractDefinitionImpl();
        contract.addInput(new InputDefinitionImpl("anInput", "aType", "aDescription"));
        contract.addRule(new RuleDefinitionImpl("aRule", "an expression", "an explanation"));
        when(processAPI.getUserTaskContract(2L)).thenReturn(contract);
        
        Response response = target("tasks/2/contract").request().get();
        
        assertThat(response).hasStatus(200);
        assertThat(response).hasBodyEqual(readFile("contract.json"));
    }
    
    @Test
    public void should_respond_404_Not_found_when_task_is_not_found_when_getting_contract() throws Exception {
        when(processAPI.getUserTaskContract(2L)).thenThrow(new UserTaskNotFoundException("task 2 not found"));
        
        Response response = target("tasks/2/contract").request().get();
        
        assertThat(response.getStatus()).isEqualTo(404);
    }
    
    @Test
    public void should_execute_a_task_with_given_inputs() throws Exception {
        Input input = new Input("aBoolean", true);
        Input input2 = new Input("aString", "hello world");
        
        target("tasks/2/execute").request().post(Entity.json(asList(input, input2)));
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("aBoolean", true);
        map.put("aString", "hello world");
        verify(processAPI).executeFlowNode(2L, map);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void should_respond_400_Bad_request_when_contract_is_not_validated_when_executing_a_task() throws Exception {
        Input input = new Input("aBoolean", true);
        doThrow(new ContractViolationException("aMessage", asList("first explanation", "second explanation"))).when(processAPI).executeFlowNode(anyLong(), anyMap());
        
        Response response = target("tasks/2/execute").request().post(Entity.json(asList(input)));
        
        assertThat(response).hasStatus(400);
        assertThat(response).hasBodyEqual(readFile("contractViolationError.json"));
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void should_respond_500_Internal_server_error_when_error_occurs_on_task_execution() throws Exception {
        Input input = new Input("aBoolean", true);
        doThrow(new FlowNodeExecutionException("aMessage")).when(processAPI).executeFlowNode(anyLong(), anyMap());
        
        Response response = target("tasks/2/execute").request().post(Entity.json(asList(input)));
        
        assertThat(response.getStatus()).isEqualTo(500);
    }
    
    private String readFile(String fileName) throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);
        return IOUtils.toString(resourceAsStream);
    }
}
