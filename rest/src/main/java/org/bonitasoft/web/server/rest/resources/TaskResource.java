package org.bonitasoft.web.server.rest.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.web.server.rest.exception.BonitaWebApplicationException;
import org.bonitasoft.web.server.rest.model.Input;


@Path("tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    private ProcessAPI processAPI;

    @Inject
    public TaskResource(ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @GET
    @Path("{taskId}/contract")
    public ContractDefinition getContract(@PathParam("taskId") long taskId)  {
        try {
            return processAPI.getUserTaskContract(taskId);
        } catch (UserTaskNotFoundException e) {
            throw new BonitaWebApplicationException(Status.NOT_FOUND, e);
        }
    }
    
    @POST
    @Path("{taskId}/execute")
    public void executeTask(@PathParam("taskId") long taskId, List<Input> inputs) {
        try {
            processAPI.executeFlowNode(taskId, buildMap(inputs));
        } catch (ContractViolationException e) {
            throw new BonitaWebApplicationException(Status.BAD_REQUEST, e);
        } catch (FlowNodeExecutionException e) {
            throw new BonitaWebApplicationException(Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    private Map<String, Object> buildMap(List<Input> inputs) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        for (Input input : inputs) {
            parameters.put(input.getName(), input.getValue());
        }
        return parameters;
    }
}
