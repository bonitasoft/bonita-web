package org.bonitasoft.web.server.rest.resources;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.HashMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

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
        contract.addRule(new RuleDefinitionImpl("aRule", "an expression", "an explenation"));
        when(processAPI.getUserTaskContract(2L)).thenReturn(contract);
        
        String responseMsg = target("tasks/2/contract").request().get(String.class);
        
        assertThat(responseMsg).isEqualTo("{\"inputs\":[{\"id\":0,\"name\":\"anInput\",\"description\":\"aDescription\",\"type\":\"aType\"}],\"rules\":[{\"id\":0,\"name\":\"aRule\",\"expression\":\"an expression\",\"explanation\":\"an explenation\",\"inputNames\":[]}]}");
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
        doThrow(new ContractViolationException("aMessage", Arrays.asList("first explanation", "second explanation"))).when(processAPI).executeFlowNode(anyLong(), anyMap());
        
        Response response = target("tasks/2/execute").request().post(Entity.json(asList(input)));
        
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.readEntity(String.class)).isEqualTo("{\"status\":400,\"type\":\"ContractViolationException\",\"message\":\"aMessage\",\"explanations\":[\"first explanation\",\"second explanation\"]}");
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void should_respond_500_Internal_server_error_when_error_occurs_on_task_execution() throws Exception {
        Input input = new Input("aBoolean", true);
        doThrow(new FlowNodeExecutionException("aMessage")).when(processAPI).executeFlowNode(anyLong(), anyMap());
        
        Response response = target("tasks/2/execute").request().post(Entity.json(asList(input)));
        
        assertThat(response.getStatus()).isEqualTo(500);
    }
}
