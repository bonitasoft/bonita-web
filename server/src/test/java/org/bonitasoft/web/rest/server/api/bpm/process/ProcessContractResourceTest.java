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
package org.bonitasoft.web.rest.server.api.bpm.process;

import static org.bonitasoft.web.rest.server.utils.ResponseAssert.assertThat;
import static org.mockito.Mockito.when;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.Type;
import org.bonitasoft.engine.bpm.contract.impl.ComplexInputDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.ConstraintDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.ContractDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.SimpleInputDefinitionImpl;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class ProcessContractResourceTest extends RestletTest {

    private static final long PROCESS_DEFINITION_ID = 4L;

    private final String TEST_CONTRACT_API_URL = "/bpm/process/" + PROCESS_DEFINITION_ID + "/contract";

    private static final String VALID_COMPLEX_POST_BODY = "{\"aBoolean\":true, \"aString\":\"hello world\", \"a_complex_type\":{\"aNumber\":2, \"aBoolean\":false}}";

    private static final String VALID_POST_BODY = "{ \"key\": \"value\", \"key2\": \"value2\" }";

    @Mock
    private ProcessAPI processAPI;


    @Override
    protected ServerResource configureResource() {
        return new ProcessContractResource(processAPI);
    }


    @Test
    public void should_return_a_contract_for_a_given_process_definition_id() throws Exception {
        //given
        final ContractDefinitionImpl contract = new ContractDefinitionImpl();
        contract.addSimpleInput(new SimpleInputDefinitionImpl("anInput", Type.TEXT, "aDescription"));
        final ComplexInputDefinitionImpl complexInputDefinitionImpl = new ComplexInputDefinitionImpl("complexInput", "description", true);
        complexInputDefinitionImpl.getSimpleInputs().add(new SimpleInputDefinitionImpl("anInput", Type.TEXT, "aDescription"));

        contract.addComplexInput(complexInputDefinitionImpl);
        contract.addConstraint(new ConstraintDefinitionImpl("aRule", "an expression", "an explanation"));

        when(processAPI.getProcessContract(PROCESS_DEFINITION_ID)).thenReturn(contract);

        //when
        final Response response = request(TEST_CONTRACT_API_URL).get();

        //then
        assertThat(response).hasStatus(Status.SUCCESS_OK);
        assertThat(response).hasJsonEntityEqualTo(readFile("contract.json"));
    }

    @Test
    public void should_respond_404_Not_found_when_process_definition_is_not_found_when_getting_contract() throws Exception {
        when(processAPI.getProcessContract(PROCESS_DEFINITION_ID)).thenThrow(new ProcessDefinitionNotFoundException("process definition not found"));
        final Response response = request(TEST_CONTRACT_API_URL).get();
        assertThat(response).hasStatus(Status.CLIENT_ERROR_NOT_FOUND);
    }

}
