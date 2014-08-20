package org.bonitasoft.web.server.rest.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.web.server.rest.model.Input;


@Path("tasks")
public class TaskResource {

    private ProcessAPI processAPI;

    @Inject
    public TaskResource(ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @GET
    @Path("{taskId}/contract")
    @Produces(MediaType.APPLICATION_JSON)
    public ContractDefinition getContract(@PathParam("taskId") long taskId) throws UserTaskNotFoundException {
        return processAPI.getUserTaskContract(taskId);
    }
    
    @PUT
    @Path("{taskId}/execute")
    @Consumes(MediaType.APPLICATION_JSON)
    public void executeTask(@PathParam("taskId") long taskId, List<Input> inputs) throws FlowNodeExecutionException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        for (Input input : inputs) {
            parameters.put(input.getName(), input.getValue());
        }
        processAPI.executeFlowNode(taskId, parameters);
    }
}
