package org.bonitasoft.web.rest.server.api.form;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.api.ProcessConfigurationAPI;
import org.bonitasoft.engine.exception.FormMappingNotFoundException;
import org.bonitasoft.engine.form.FormMapping;
import org.bonitasoft.engine.form.FormMappingTarget;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class FormMappingResourceTest extends RestletTest {

    @Mock
    protected ProcessConfigurationAPI processConfigurationAPI;

    @Override
    protected ServerResource configureResource() {
        return new FormMappingResource(processConfigurationAPI);
    }

    @Test
    public void updateShouldHandleNullId() throws Exception {

        final Response response = request("/form/mapping").put("{\"form\":\"myPage\",\"target\":\"INTERNAL\"}");

        assertThat(response.getStatus()).isEqualTo(Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Test
    public void updateShouldHandleNotFound() throws Exception {

        doThrow(FormMappingNotFoundException.class).when(processConfigurationAPI).updateFormMapping(1L, "myPage", FormMappingTarget.INTERNAL);

        final Response response = request("/form/mapping/1").put("{\"form\":\"myPage\",\"target\":\"INTERNAL\" }");

        assertThat(response.getStatus()).isEqualTo(Status.CLIENT_ERROR_NOT_FOUND);
    }

    @Test
    public void searchFormMappingShouldReturnContentRange() throws Exception {

        final SearchResult<FormMapping> searchResult = mock(SearchResult.class);
        final FormMapping formMapping = mock(FormMapping.class);
        final List<FormMapping> formMappings = new ArrayList<FormMapping>();
        formMappings.add(formMapping);
        doReturn(formMappings).when(searchResult).getResult();
        doReturn(1L).when(searchResult).getCount();
        doReturn(searchResult).when(processConfigurationAPI).searchFormMappings(any(SearchOptions.class));

        final Response response = request("/form/mapping?p=2&c=10").get();

        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
        assertThat(response.getHeaders().getFirstValue("Content-range")).isEqualTo("20-29/1");
    }

    @Test
    public void searchFormMappingShouldReturnMapping() throws Exception {

        final SearchResult<FormMapping> searchResult = mock(SearchResult.class);
        final FormMapping formMapping = new FormMapping();
        formMapping.setTask("myTask");
        final List<FormMapping> formMappings = new ArrayList<FormMapping>();
        formMappings.add(formMapping);
        doReturn(formMappings).when(searchResult).getResult();
        doReturn(1L).when(searchResult).getCount();
        doReturn(searchResult).when(processConfigurationAPI).searchFormMappings(any(SearchOptions.class));

        final Response response = request("/form/mapping?p=0&c=10").get();

        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response.getEntity().write(outputStream);
        final String content = outputStream.toString();
        outputStream.close();
        assertThat(content).isNotNull();
        assertThat(content).contains("\"task\":\"myTask\"");
        verify(processConfigurationAPI).searchFormMappings(any(SearchOptions.class));
    }

    @Test
    public void updateShouldCallEngine() throws Exception {

        doReturn(mock(FormMapping.class)).when(processConfigurationAPI).updateFormMapping(2L, "myPage", FormMappingTarget.INTERNAL);

        final Response response = request("/form/mapping/2").put("{\"form\":\"myPage\",\"target\":\"INTERNAL\"}");

        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_NO_CONTENT);
        verify(processConfigurationAPI).updateFormMapping(2L, "myPage", FormMappingTarget.INTERNAL);
    }
}
