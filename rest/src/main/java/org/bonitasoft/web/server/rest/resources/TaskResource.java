package org.bonitasoft.web.server.rest.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;

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
}
