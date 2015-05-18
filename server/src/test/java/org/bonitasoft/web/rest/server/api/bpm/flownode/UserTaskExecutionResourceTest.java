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
package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.web.rest.server.utils.ResponseAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.contract.InputDefinition;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessExecutionException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskExecutionResourceTest extends RestletTest {

    private static final String VALID_COMPLEX_POST_BODY = "{\"aBoolean\":true, \"aString\":\"hello world\", \"a_complex_type\":{\"aNumber\":2, \"aBoolean\":false}}";

    private static final String VALID_POST_BODY = "{ \"key\": \"value\", \"key2\": \"value2\" }";

    @Mock
    private ProcessAPI processAPI;

    @Mock
    private Logger logger;

    private UserTaskExecutionResource userTaskExecutionResource;

    @Mock
    private Response response;

    @Mock
    private APISession apiSession;

    @Mock
    private ContractDefinition contractDefinition;

    @Before
    public void initializeMocks() {
        userTaskExecutionResource = spy(new UserTaskExecutionResource(processAPI, apiSession));
        when(contractDefinition.getInputs()).thenReturn(Collections.<InputDefinition> emptyList());
    }

    @Override
    protected ServerResource configureResource() {
        return new UserTaskExecutionResource(processAPI, apiSession);
    }

    private Map<String, Serializable> aComplexInput() {
        final HashMap<String, Serializable> aComplexInput = new HashMap<>();
        aComplexInput.put("aBoolean", true);
        aComplexInput.put("aString", "hello world");

        final HashMap<String, Serializable> childMap = new HashMap<>();
        childMap.put("aNumber", 2);
        childMap.put("aBoolean", false);

        aComplexInput.put("a_complex_type", childMap);

        return aComplexInput;
    }

    @Test
    public void should_execute_a_task_with_given_inputs() throws Exception {
        final Map<String, Serializable> expectedComplexInput = aComplexInput();
        when(processAPI.getUserTaskContract(2)).thenReturn(contractDefinition);

        final Response response = request("/bpm/userTask/2/execution").post(VALID_COMPLEX_POST_BODY);

        assertThat(response).hasStatus(Status.SUCCESS_NO_CONTENT);
        verify(processAPI).executeUserTask(2L, expectedComplexInput);
    }

    @Test
    public void should_execute_a_task_with_given_inputs_for_a_specific_user() throws Exception {
        final Map<String, Serializable> expectedComplexInput = aComplexInput();
        when(processAPI.getUserTaskContract(2)).thenReturn(contractDefinition);

        final Response response = request("/bpm/userTask/2/execution?user=1").post(VALID_COMPLEX_POST_BODY);

        assertThat(response).hasStatus(Status.SUCCESS_NO_CONTENT);
        verify(processAPI).executeUserTask(1L, 2L, expectedComplexInput);
        verify(processAPI, times(0)).executeUserTask(2L, expectedComplexInput);
    }

    @Test
    public void should_respond_400_Bad_request_when_contract_is_not_validated_when_executing_a_task() throws Exception {
        when(processAPI.getUserTaskContract(2)).thenReturn(contractDefinition);
        doThrow(new ContractViolationException("aMessage", "aMessage", asList("first explanation", "second explanation"), null))
                .when(processAPI).executeUserTask(anyLong(), anyMapOf(String.class, Serializable.class));

        final Response response = request("/bpm/userTask/2/execution").post(VALID_POST_BODY);

        assertThat(response).hasStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        assertThat(response)
                .hasJsonEntityEqualTo(
                        "{\"exception\":\"class org.bonitasoft.engine.bpm.contract.ContractViolationException\",\"message\":\"aMessage\",\"explanations\":[\"first explanation\",\"second explanation\"]}");
        verify(userTaskExecutionResource, times(0)).deleteFiles(any(ContractDefinition.class),anyMap(),anyLong(),anyLong());
    }

    @Test
    public void should_respond_500_Internal_server_error_when_error_occurs_on_task_execution() throws Exception {
        doThrow(new FlowNodeExecutionException("aMessage"))
                .when(processAPI).executeUserTask(anyLong(), anyMapOf(String.class, Serializable.class));

        final Response response = request("/bpm/userTask/2/execution").post(VALID_POST_BODY);

        assertThat(response).hasStatus(Status.SERVER_ERROR_INTERNAL);
        verify(userTaskExecutionResource, times(0)).deleteFiles(any(ContractDefinition.class),anyMap(),anyLong(),anyLong());
    }

    @Test
    public void should_respond_400_Bad_request_when_trying_to_execute_with_not_json_payload() throws Exception {
        final Response response = request("/bpm/userTask/2/execution").post("invalid json string");

        assertThat(response).hasStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        verify(userTaskExecutionResource, times(0)).deleteFiles(any(ContractDefinition.class),anyMap(),anyLong(),anyLong());
    }

    @Test
    public void should_respond_404_Not_found_when_task_is_not_found_when_trying_to_execute_it() throws Exception {
        when(processAPI.getUserTaskContract(2)).thenReturn(contractDefinition);
        doThrow(new UserTaskNotFoundException("task not found")).when(processAPI)
                .executeUserTask(anyLong(), anyMapOf(String.class, Serializable.class));

        final Response response = request("/bpm/userTask/2/execution").post(VALID_POST_BODY);

        assertThat(response).hasStatus(Status.CLIENT_ERROR_NOT_FOUND);
        verify(userTaskExecutionResource, times(0)).deleteFiles(any(ContractDefinition.class),anyMap(),anyLong(),anyLong());
    }

    @Test
    public void should_contract_violation_exception_log_explanations_when_logger_is_info() throws UserTaskNotFoundException, FlowNodeExecutionException,
            ProcessDefinitionNotFoundException, ProcessActivationException, ProcessExecutionException, ContractViolationException, FileNotFoundException {
        //given
        final String message = "contract violation !!!!";
        final List<String> explanations = Arrays.asList("explanation1", "explanation2");
        doThrow(new ContractViolationException(message, message, explanations, null)).when(processAPI)
                .executeUserTask(anyLong(), anyMapOf(String.class, Serializable.class));
        when(processAPI.getUserTaskContract(1L)).thenReturn(contractDefinition);
        doReturn(logger).when(userTaskExecutionResource).getLogger();
        doReturn(1L).when(userTaskExecutionResource).getTaskIdParameter();
        doReturn(true).when(logger).isLoggable(Level.INFO);
        doReturn(response).when(userTaskExecutionResource).getResponse();
        final Map<String, Serializable> inputs = new HashMap<>();
        inputs.put("testKey", "testValue");

        //when
        userTaskExecutionResource.executeTask(inputs);

        //then
        verify(logger, times(1)).log(Level.INFO, message + "\nExplanations:\nexplanation1explanation2");
        verify(userTaskExecutionResource, times(0)).deleteFiles(any(ContractDefinition.class),anyMap(),anyLong(),anyLong());
    }

    @Test
    public void should_call_deleteFiles() throws UserTaskNotFoundException, FileNotFoundException, FlowNodeExecutionException {
        //given
        when(processAPI.getUserTaskContract(1L)).thenReturn(contractDefinition);
        doReturn(1L).when(userTaskExecutionResource).getTaskIdParameter();
        doReturn(response).when(userTaskExecutionResource).getResponse();
        final Map<String, Serializable> inputs = new HashMap<>();
        inputs.put("testKey", "testValue");

        //when
        userTaskExecutionResource.executeTask(inputs);

        verify(userTaskExecutionResource, times(1)).deleteFiles(any(ContractDefinition.class),anyMap(),anyLong(),anyLong());
    }

    @Test
    public void should_getProcessDefinitionIdParameter_throws_an_exception_when_task_id_parameter_is_null() throws Exception {
        //given
        doReturn(null).when(userTaskExecutionResource).getAttribute(UserTaskExecutionResource.TASK_ID);

        try {
            //when
            userTaskExecutionResource.getTaskIdParameter();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getMessage()).isEqualTo("Attribute '" + UserTaskExecutionResource.TASK_ID + "' is mandatory");
        }

    }

}
