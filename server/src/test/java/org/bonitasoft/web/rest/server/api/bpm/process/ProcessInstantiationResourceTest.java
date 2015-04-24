package org.bonitasoft.web.rest.server.api.bpm.process;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.web.rest.server.utils.ResponseAssert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.contract.InputDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessExecutionException;
import org.bonitasoft.engine.bpm.process.impl.internal.ProcessInstanceImpl;
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

@RunWith(MockitoJUnitRunner.class)
public class ProcessInstantiationResourceTest extends RestletTest {

    private static final long PROCESS_DEFINITION_ID = 2L;

    private static final String ID_PROCESS_DEFINITION = "2";

    private static final String URL_ADI_PROCESS_INSTANTIATION_TEST = "/bpm/process/" + ID_PROCESS_DEFINITION + "/instantiation";

    private static final String URL_ADI_PROCESS_INSTANTIATION_TEST_WITH_USER = URL_ADI_PROCESS_INSTANTIATION_TEST + "?user=1";

    private static final String VALID_COMPLEX_POST_BODY = "{\"aBoolean\":true, \"aString\":\"hello world\", \"a_complex_type\":{\"aNumber\":2, \"aBoolean\":false}}";

    private static final String VALID_POST_BODY = "{ \"key\": \"value\", \"key2\": \"value2\" }";

    @Mock
    ProcessAPI processAPI;

    @Mock
    Logger logger;

    ProcessInstantiationResource processInstantiationResource;

    @Mock
    Response response;

    @Mock
    ContractDefinition contractDefinition;

    @Before
    public void initializeMocks() {
        processInstantiationResource = spy(new ProcessInstantiationResource(processAPI));
        when(contractDefinition.getInputs()).thenReturn(Collections.<InputDefinition> emptyList());
    }

    @Override
    protected ServerResource configureResource() {
        return new ProcessInstantiationResource(processAPI);
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
    public void should_instanciate_a_process_with_given_inputs() throws Exception {
        final Map<String, Serializable> expectedComplexInput = aComplexInput();
        when(processAPI.startProcessWithInputs(PROCESS_DEFINITION_ID, expectedComplexInput)).thenReturn(new ProcessInstanceImpl("complexProcessInstance"));
        when(processAPI.getProcessContract(PROCESS_DEFINITION_ID)).thenReturn(contractDefinition);

        final Response response = request(URL_ADI_PROCESS_INSTANTIATION_TEST).post(VALID_COMPLEX_POST_BODY);

        assertThat(response).hasStatus(Status.SUCCESS_OK);
        assertThat(response.getEntityAsText())
        .isEqualTo(
                "{\"id\":0,\"name\":\"complexProcessInstance\",\"state\":null,\"startDate\":null,\"startedBy\":0,"
                        + "\"startedBySubstitute\":0,\"endDate\":null,\"lastUpdate\":null,\"processDefinitionId\":0,"
                        + "\"description\":null,\"rootProcessInstanceId\":0,\"callerId\":0,\"stringIndex1\":null,"
                        + "\"stringIndex2\":null,\"stringIndex3\":null,\"stringIndex4\":null,\"stringIndex5\":null,"
                        + "\"stringIndexLabel1\":null,\"stringIndexLabel2\":null,\"stringIndexLabel3\":null,\"stringIndexLabel4\":null,"
                        + "\"stringIndexLabel5\":null,\"startedByDelegate\":0}");
        verify(processAPI).startProcessWithInputs(PROCESS_DEFINITION_ID, expectedComplexInput);

    }

    @Test
    public void should_instanciate_a_process_with_given_inputs_for_a_specific_user() throws Exception {
        final Map<String, Serializable> expectedComplexInput = aComplexInput();
        when(processAPI.startProcessWithInputs(1L, PROCESS_DEFINITION_ID, expectedComplexInput)).thenReturn(new ProcessInstanceImpl("complexProcessInstance"));
        when(processAPI.getProcessContract(PROCESS_DEFINITION_ID)).thenReturn(contractDefinition);

        final Response response = request(URL_ADI_PROCESS_INSTANTIATION_TEST_WITH_USER).post(VALID_COMPLEX_POST_BODY);

        assertThat(response).hasStatus(Status.SUCCESS_OK);
        assertThat(response.getEntityAsText())
                .isEqualTo(
                        "{\"id\":0,\"name\":\"complexProcessInstance\",\"state\":null,\"startDate\":null,\"startedBy\":0,"
                        + "\"startedBySubstitute\":0,\"endDate\":null,\"lastUpdate\":null,\"processDefinitionId\":0,"
                        + "\"description\":null,\"rootProcessInstanceId\":0,\"callerId\":0,\"stringIndex1\":null,"
                        + "\"stringIndex2\":null,\"stringIndex3\":null,\"stringIndex4\":null,\"stringIndex5\":null,"
                        + "\"stringIndexLabel1\":null,\"stringIndexLabel2\":null,\"stringIndexLabel3\":null,\"stringIndexLabel4\":null,"
                        + "\"stringIndexLabel5\":null,\"startedByDelegate\":0}");
        verify(processAPI).startProcessWithInputs(1L, PROCESS_DEFINITION_ID, expectedComplexInput);
        verify(processAPI, times(0)).startProcessWithInputs(PROCESS_DEFINITION_ID, expectedComplexInput);
    }

    @Test
    public void should_respond_400_Bad_request_when_contract_is_not_validated_when_instanciate_a_process() throws Exception {
        doThrow(new ContractViolationException("aMessage", asList("first explanation", "second explanation")))
        .when(processAPI).startProcessWithInputs(anyLong(), anyMapOf(String.class, Serializable.class));
        when(processAPI.getProcessContract(PROCESS_DEFINITION_ID)).thenReturn(contractDefinition);

        final Response response = request(URL_ADI_PROCESS_INSTANTIATION_TEST).post(VALID_POST_BODY);

        assertThat(response).hasStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        assertThat(response)
        .hasJsonEntityEqualTo(
                "{\"exception\":\"class org.bonitasoft.engine.bpm.contract.ContractViolationException\",\"message\":\"aMessage\",\"explanations\":[\"first explanation\",\"second explanation\"]}");
    }

    @Test
    public void should_respond_500_Internal_server_error_when_error_occurs_on_process_instanciation() throws Exception {
        doThrow(new ProcessExecutionException("aMessage"))
        .when(processAPI).startProcessWithInputs(anyLong(), anyMapOf(String.class, Serializable.class));

        final Response response = request(URL_ADI_PROCESS_INSTANTIATION_TEST).post(VALID_POST_BODY);

        assertThat(response).hasStatus(Status.SERVER_ERROR_INTERNAL);
    }

    @Test
    public void should_respond_400_Bad_request_when_trying_to_execute_with_not_json_payload() throws Exception {
        final Response response = request(URL_ADI_PROCESS_INSTANTIATION_TEST).post("invalid json string");

        assertThat(response).hasStatus(Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Test
    public void should_respond_404_Not_found_when_task_is_not_found_when_trying_to_execute_it() throws Exception {
        doThrow(new ProcessDefinitionNotFoundException("process not found")).when(processAPI)
        .startProcessWithInputs(anyLong(), anyMapOf(String.class, Serializable.class));
        when(processAPI.getProcessContract(PROCESS_DEFINITION_ID)).thenReturn(contractDefinition);

        final Response response = request(URL_ADI_PROCESS_INSTANTIATION_TEST).post(VALID_POST_BODY);

        assertThat(response).hasStatus(Status.CLIENT_ERROR_NOT_FOUND);
    }

    @Test
    public void should_contract_violation_exception_log_explanations_when_logger_is_info() throws Exception {
        // given
        final String message = "contract violation !!!!";
        final List<String> explanations = Arrays.asList("explanation1", "explanation2");
        doThrow(new ContractViolationException(message, explanations)).when(processAPI)
        .startProcessWithInputs(anyLong(), anyMapOf(String.class, Serializable.class));
        doReturn(logger).when(processInstantiationResource).getLogger();
        doReturn(Long.toString(PROCESS_DEFINITION_ID)).when(processInstantiationResource).getAttribute(ProcessInstantiationResource.PROCESS_DEFINITION_ID);
        doReturn(contractDefinition).when(processAPI).getProcessContract(PROCESS_DEFINITION_ID);
        doReturn(true).when(logger).isLoggable(Level.INFO);
        doReturn(response).when(processInstantiationResource).getResponse();
        final Map<String, Serializable> inputs = new HashMap<>();
        inputs.put("testKey", "testValue");

        // when
        processInstantiationResource.instanciateProcess(inputs);

        // then
        verify(logger, times(1)).log(Level.INFO, message + "\nExplanations:\nexplanation1explanation2");

    }

    @Test
    public void should_getProcessDefinitionIdParameter_throws_an_exception_when_task_id_parameter_is_null() throws Exception {
        // given
        doReturn(null).when(processInstantiationResource).getAttribute(ProcessInstantiationResource.PROCESS_DEFINITION_ID);

        try {
            // when
            processInstantiationResource.getProcessDefinitionIdParameter();
        } catch (final Exception e) {
            // then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getMessage()).isEqualTo("Attribute '" + ProcessInstantiationResource.PROCESS_DEFINITION_ID + "' is mandatory");
        }

    }
}
