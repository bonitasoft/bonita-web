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
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.Map;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.bpm.businessdata.impl.BusinessDataQueryMetadataImpl;
import org.bonitasoft.engine.bpm.businessdata.impl.BusinessDataQueryResultImpl;
import org.bonitasoft.engine.command.CommandExecutionException;
import org.bonitasoft.engine.command.CommandNotFoundException;
import org.bonitasoft.web.rest.server.utils.ResponseAssert;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class BusinessDataQueryResourceTest extends RestletTest {

    public static final String VALID_BDM_REQUEST = "/bdm/businessData/org.bonitasoft.pojo.Employee?q=findByName&c=5&p=3&f=name=John&f=country=US";
    public static final String JSON_RESPONSE = "{\"name\":\"Matti\"}";
    @Mock
    protected CommandAPI commandAPI;

    @Override
    protected ServerResource configureResource() {
        return new BusinessDataQueryResource(commandAPI);
    }

    @Test
    public void should_call_custom_query() throws Exception {
        final Answer<Serializable> answer = new Answer<Serializable>() {

            @Override
            public Serializable answer(final InvocationOnMock invocation) throws Throwable {
                assertThat(invocation.getArguments()).as("should have 2 parameters").hasSize(2);
                assertThat(invocation.getArguments()[0]).as("should call command").isEqualTo(BusinessDataQueryResource.COMMAND_NAME);
                final Map<String, Serializable> parameters = (Map<String, Serializable>) invocation.getArguments()[1];
                assertThat(parameters).as("should have required  parameters").hasSize(6);
                assertThat(parameters).as("should compute start index").containsEntry("startIndex", 3 * 5);
                assertThat(parameters).containsEntry("maxResults", 5);
                assertThat(parameters).containsEntry("queryName", "findByName");
                assertThat(parameters).containsEntry("businessDataURIPattern", BusinessDataFieldValue.URI_PATTERN);
                assertThat(parameters).containsEntry("entityClassName", "org.bonitasoft.pojo.Employee");
                assertThat(parameters).containsKey("queryParameters");

                final Map<String, Serializable> queryParameters = (Map<String, Serializable>) parameters.get("queryParameters");
                assertThat(queryParameters).as("should compute search filters").hasSize(2);
                assertThat(queryParameters).containsEntry("name", "John");
                assertThat(queryParameters).containsEntry("country", "US");

                return new BusinessDataQueryResultImpl( JSON_RESPONSE, new BusinessDataQueryMetadataImpl(1,2,4L));
            }
        };
        when(commandAPI.execute(anyString(), anyMapOf(String.class, Serializable.class))).then(answer);

        //then
        final Response response = request(VALID_BDM_REQUEST).get();
        Header expectedHeader=new Header("Content-range", "1-2/4");
        ResponseAssert.assertThat(response)
                .hasJsonEntityEqualTo(JSON_RESPONSE)
                .hasStatus(Status.SUCCESS_OK)
                .hasHeader(expectedHeader);
    }

    @Test
    public void should_throw_exception_when_missing_count() throws Exception {
        // when
        final Response response = request("/bdm/businessData/org.bonitasoft.pojo.Employee?q=findByName&p=0").get();

        // then
        assertThat(response.getEntityAsText()).isEqualTo(
                "{\"exception\":\"class java.lang.IllegalArgumentException\",\"message\":\"query parameter c (count) is mandatory\"}");
        assertThat(response.getStatus()).isEqualTo(Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Test
    public void should_throw_exception_when_missing_page() throws Exception {

        // when
        final Response response = request("/bdm/businessData/org.bonitasoft.pojo.Employee?q=findByName&c=0").get();

        //then
        assertThat(response.getEntityAsText()).isEqualTo(
                "{\"exception\":\"class java.lang.IllegalArgumentException\",\"message\":\"query parameter p (page) is mandatory\"}");
        assertThat(response.getStatus()).isEqualTo(Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Test
    public void should_throw_not_found_when_engine_NotFoundException() throws Exception {
        //given
        when(commandAPI.execute(anyString(), anyMap())).thenThrow(new CommandNotFoundException(null));

        // when
        final Response response = request(VALID_BDM_REQUEST).get();

        //then
        assertThat(response.getEntityAsText()).isEqualTo(
                "{\"exception\":\"class org.bonitasoft.engine.command.CommandNotFoundException\",\"message\":\"null\"}");
        assertThat(response.getStatus()).isEqualTo(Status.CLIENT_ERROR_NOT_FOUND);
    }

    @Test
    public void should_return_an_internal_server_error_status_when_command_fails_during_execution() throws Exception {
        //given
        when(commandAPI.execute(anyString(), anyMap())).thenThrow(new CommandExecutionException("server error"));

        // when
        final Response response = request(VALID_BDM_REQUEST).get();

        //then
        assertThat(response.getEntityAsText()).isEqualTo(
                "{\"exception\":\"class org.bonitasoft.engine.command.CommandExecutionException\",\"message\":\"server error\"}");
        assertThat(response.getStatus()).isEqualTo(Status.SERVER_ERROR_INTERNAL);
    }

}
