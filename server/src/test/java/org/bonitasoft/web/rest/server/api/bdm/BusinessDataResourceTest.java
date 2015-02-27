/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.bdm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.command.CommandExecutionException;
import org.bonitasoft.engine.command.CommandNotFoundException;
import org.bonitasoft.engine.command.CommandParameterizationException;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class BusinessDataResourceTest extends RestletTest {

    @Mock
    protected CommandAPI commandAPI;

    @Override
    protected ServerResource configureResource() {
        return new BusinessDataResource(commandAPI);
    }

    @Test
    public void should_return_the_business_data_based_on_its_id() throws Exception {
        final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
        parameters.put("entityClassName", "org.bonitasoft.pojo.Employee");
        parameters.put("businessDataId", 1983L);
        parameters.put("businessDataURIPattern", "/API/bdm/businessData/{className}/{id}/{field}");
        when(commandAPI.execute("getBusinessDataById", parameters)).thenReturn("{\"name\":\"Matti\"}");

        final Response response = request("/bdm/businessData/org.bonitasoft.pojo.Employee/1983").get();

        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
        assertThat(response.getEntityAsText()).isEqualTo("{\"name\":\"Matti\"}");
    }

    @Test
    public void should_return_an_internal_server_error_status_when_command_is_not_found() throws Exception {
        when(commandAPI.execute(anyString(), anyMap())).thenThrow(new CommandNotFoundException(null));

        final Response response = request("/bdm/businessData/org.bonitasoft.pojo.Employee/1983").get();

        assertThat(response.getStatus()).isEqualTo(Status.SERVER_ERROR_INTERNAL);
    }

    @Test
    public void should_return_an_internal_server_error_status_when_command_is_not_well_parameterized() throws Exception {
        when(commandAPI.execute(anyString(), anyMap())).thenThrow(new CommandParameterizationException("id is missing"));

        final Response response = request("/bdm/businessData/org.bonitasoft.pojo.Employee/1983").get();

        assertThat(response.getStatus()).isEqualTo(Status.SERVER_ERROR_INTERNAL);
    }

    @Test
    public void should_return_a_not_found_status_when_command_fails_business_data_not_found() throws Exception {
        when(commandAPI.execute(anyString(), anyMap())).thenThrow(new CommandExecutionException(new DataNotFoundException(null)));

        final Response response = request("/bdm/businessData/org.bonitasoft.pojo.Employee/1983").get();

        assertThat(response.getStatus()).isEqualTo(Status.CLIENT_ERROR_NOT_FOUND);
    }

    @Test
    public void should_return_an_internal_server_error_status_when_command_fails_during_execution() throws Exception {
        when(commandAPI.execute(anyString(), anyMap())).thenThrow(new CommandExecutionException("server error"));

        final Response response = request("/bdm/businessData/org.bonitasoft.pojo.Employee/1983").get();

        assertThat(response.getStatus()).isEqualTo(Status.SERVER_ERROR_INTERNAL);
    }

    @Test
    public void should_fetch_business_data_child_if_it_is_specified() throws Exception {
        final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
        parameters.put("entityClassName", "org.bonitasoft.pojo.Employee");
        parameters.put("businessDataId", 1983L);
        parameters.put("businessDataChildName", "child");
        parameters.put("businessDataURIPattern", "/API/bdm/businessData/{className}/{id}/{field}");
        when(commandAPI.execute("getBusinessDataById", parameters)).thenReturn("{\"name\":\"Matti\"}");

        final Response response = request("/bdm/businessData/org.bonitasoft.pojo.Employee/1983/child").get();

        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
        assertThat(response.getEntityAsText()).isEqualTo("{\"name\":\"Matti\"}");
    }
}
