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
package org.bonitasoft.web.server.rest.exception.mapper;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.web.server.rest.exception.ErrorMessage;

@Provider
public class ContractViolationExceptionMapper implements ExceptionMapper<ContractViolationException> {

    @Override
    public Response toResponse(ContractViolationException exception) {
        return Response.status(Status.BAD_REQUEST).entity(new ContractViolationErrorMessage(exception).withStatus(Status.BAD_REQUEST)).build();
    }
    
    /**
     * Specific error message for {@link ContractViolationException}, adding explanations to printed json
     */
    public class ContractViolationErrorMessage extends ErrorMessage {

        private List<String> explanations;
        
        public ContractViolationErrorMessage(ContractViolationException exception) {
            super(exception);
            this.explanations = exception.getExplanations();
        }

        public List<String> getExplanations() {
            return explanations;
        }
    }
}
