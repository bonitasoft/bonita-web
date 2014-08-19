package org.bonitasoft.web.server.rest.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.impl.ContractDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.InputDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.RuleDefinitionImpl;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.web.server.rest.exception.NotFoundExceptionMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mock;

public class TaskResourceTest extends JerseyTest {

    @Mock
    private ProcessAPI processAPI;
    
    @Override
    protected Application configure() {
        initMocks(this);
        return new ResourceConfig()
            .register(new TaskResource(processAPI))
            .register(JacksonFeature.class)
            .register(NotFoundExceptionMapper.class);
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
    public void should_throw_error_404_if_task_is_not_found() throws Exception {
        when(processAPI.getUserTaskContract(2L)).thenThrow(new UserTaskNotFoundException("task 2 not found"));
        
        Response response = target("tasks/2/contract").request().get();
        
        assertThat(response.getStatus()).isEqualTo(404);
    }
}
