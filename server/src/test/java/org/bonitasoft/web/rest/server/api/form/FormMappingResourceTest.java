package org.bonitasoft.web.rest.server.api.form;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bonitasoft.engine.api.ProcessConfigurationAPI;
import org.bonitasoft.engine.form.FormMapping;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FormMappingResourceTest extends RestletTest {

    @Mock
    protected ProcessConfigurationAPI processConfigurationAPI;

    @Override
    protected ServerResource configureResource() {
        return new FormMappingResource(processConfigurationAPI);
    }

    @Test
    public void searchFormMappingShouldReturnContentRange() throws Exception {

        final SearchResult<FormMapping> searchResult = mock(SearchResult.class);
        final FormMapping formMapping = mock(FormMapping.class);
        final List<FormMapping> formMappings = new ArrayList<>();
        formMappings.add(formMapping);
        doReturn(formMappings).when(searchResult).getResult();
        doReturn(1L).when(searchResult).getCount();
        doReturn(searchResult).when(processConfigurationAPI).searchFormMappings(any(SearchOptions.class));

        final Response response = request("/form/mapping?p=2&c=10&f=type=TASK").get();

        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
        assertThat(response.getHeaders().getFirstValue("Content-range")).isEqualTo("2-10/1");
    }

    @Test
    public void searchFormMappingShouldReturnMapping() throws Exception {

        final SearchResult<FormMapping> searchResult = mock(SearchResult.class);
        final FormMapping formMapping = new FormMapping();
        formMapping.setTask("myTask");
        final List<FormMapping> formMappings = new ArrayList<>();
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
    public void searchFormMappingShouldReturnLongIdAsString() throws Exception {

        final SearchResult<FormMapping> searchResult = mock(SearchResult.class);
        final FormMapping formMapping = new FormMapping();
        formMapping.setId(11125555888888L);
        formMapping.setTask("myTask");
        formMapping.setProcessDefinitionId(4871148324840256385L);
        formMapping.setPageId(1L);
        formMapping.setLastUpdatedBy(1L);
        final List<FormMapping> formMappings = new ArrayList<>();
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
        assertThat(content).contains("\"id\":\"11125555888888\"");
        assertThat(content).contains("\"processDefinitionId\":\"4871148324840256385\"");
        assertThat(content).contains("\"pageId\":\"1\"");
        assertThat(content).contains("\"lastUpdatedBy\":\"1\"");
        verify(processConfigurationAPI).searchFormMappings(any(SearchOptions.class));
    }

}
