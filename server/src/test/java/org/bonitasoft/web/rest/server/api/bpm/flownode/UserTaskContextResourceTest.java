package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.web.rest.server.utils.ResponseAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.web.rest.server.FinderFactory;
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskContextResourceTest extends RestletTest {

    @Mock
    private ProcessAPI processAPI;
    @Mock
    private FinderFactory finderFactory;
    UserTaskContextResource taskContextResource;

    @Override
    protected ServerResource configureResource() {
        return new UserTaskContextResource(processAPI, finderFactory);
    }

    @Before
    public void initializeMocks() {
        taskContextResource = spy(new UserTaskContextResource(processAPI, finderFactory));
    }

    @Test
    public void should_return_a_context_of_type_SingleBusinessDataRef_for_a_given_task_instance() throws Exception {
        //given
        final Map<String, Serializable> context = new HashMap<String, Serializable>();
        String engineResult = "object returned by engine";

        context.put("Ticket", engineResult);
        when(processAPI.getUserTaskExecutionContext(2L)).thenReturn(context);
        doReturn("clientResult").when(finderFactory).getContextResultElement(engineResult);

        //when
        final Response response = request("/bpm/userTask/2/context").get();

        //then
        assertThat(response).hasStatus(Status.SUCCESS_OK);
        assertThat(response).hasJsonEntityEqualTo("{\"Ticket\":\"clientResult\"}");
    }

    @Test
    public void should_getTaskIDParameter_throws_an_exception_when_task_id_parameter_is_null() throws Exception {
        //given
        doReturn(null).when(taskContextResource).getAttribute(UserTaskContextResource.TASK_ID);

        try {
            //when
            taskContextResource.getTaskIdParameter();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getMessage()).isEqualTo("Attribute '" + UserTaskContextResource.TASK_ID + "' is mandatory in order to get the task context");
        }

    }

    @Test
    public void should_respond_404_Not_found_when_task_is_not_found_when_getting_contract() throws Exception {
        when(processAPI.getUserTaskExecutionContext(2)).thenThrow(new UserTaskNotFoundException("task 2 not found"));

        final Response response = request("/bpm/userTask/2/context").get();

        assertThat(response).hasStatus(Status.CLIENT_ERROR_NOT_FOUND);
    }
}
