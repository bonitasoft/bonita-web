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
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.web.server.rest.exception.BonitaWebApplicationException;

@Path("tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    private final ProcessAPI processAPI;

    @Inject
    public TaskResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @GET
    @Path("{taskId}/contract")
    public ContractDefinition getContract(@PathParam("taskId") final long taskId) throws UserTaskNotFoundException {
        return processAPI.getUserTaskContract(taskId);
    }

    @POST
    @Path("{taskId}/execute")
    public void executeTask(@PathParam("taskId") final long taskId, final Map<String, Object> inputs) throws UserTaskNotFoundException,
            FlowNodeExecutionException, ContractViolationException {
        if (inputs == null) {
            throw new BonitaWebApplicationException(Status.BAD_REQUEST, new BonitaException("Contract cannot be null"));
        }
        processAPI.executeUserTask(taskId, inputs);
    }

}
