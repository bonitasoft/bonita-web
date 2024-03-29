package org.bonitasoft.web.rest.server.api.bpm.cases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.web.rest.server.api.bpm.cases.ArchivedDataInstanceBuilder.anArchivedDataInstance;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseVariable;
import org.bonitasoft.web.rest.server.BonitaRestletApplication;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ArchivedCaseVariableResourceTest extends RestletTest {

    @Mock
    private ProcessAPI processApi;
    private ArchivedCaseVariableResource restResource;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected ServerResource configureResource() {
        return new ArchivedCaseVariableResource(processApi);
    }

    @Before
    public void initializeMocks() {
        restResource = new ArchivedCaseVariableResource(processApi);
    }

    @Test
    public void should_throw_illegal_argument_exception_when_caseId_not_set() throws Exception {
        Request request = new Request();
        request.setAttributes(Map.of("variableName", "myVar"));
        restResource.setRequest(request);

        assertThrows(IllegalArgumentException.class, () -> restResource.getArchivedCaseVariable());
    }

    @Test
    public void should_throw_illegal_argument_exception_when_variableName_not_set() throws Exception {
        Request request = new Request();
        request.setAttributes(Map.of("caseId", "12"));
        restResource.setRequest(request);

        assertThrows(IllegalArgumentException.class, () -> restResource.getArchivedCaseVariable());
    }

    @Test
    public void should_throw_illegal_argument_exception_when_caseId_is_not_a_long() throws Exception {
        Request request = new Request();
        request.setAttributes(Map.of("caseId", "hello"));
        restResource.setRequest(request);

        assertThrows(IllegalArgumentException.class, () -> restResource.getArchivedCaseVariable());
    }

    @Test
    public void should_retrieve_archived_variable_from_processApi() throws Exception {
        Request request = new Request();
        request.setAttributes(Map.of("caseId", "12", "variableName", "myVar"));
        restResource.setRequest(request);

        Date archivedDate = new Date();
        when(processApi.getArchivedProcessDataInstance("myVar", 12L))
                .thenReturn(anArchivedDataInstance("myVar").withContainerId(12).withType(String.class.getName())
                        .withValue("Hello World").withArchivedDate(archivedDate).build());

        ArchivedCaseVariable archivedVariable = restResource.getArchivedCaseVariable();

        assertThat(archivedVariable.getName()).isEqualTo("myVar");
        assertThat(archivedVariable.getCaseId()).isEqualTo("12");
        assertThat(archivedVariable.getType()).isEqualTo(String.class.getName());
        assertThat(archivedVariable.getValue()).isEqualTo("Hello World");
        assertThat(archivedVariable.getArchivedDate()).isEqualTo(archivedDate);
    }

    @Test
    public void should_return_archived_variable_with_200_status() throws Exception {
        Date archivedDate = new Date();
        when(processApi.getArchivedProcessDataInstance("myVar", 12L))
                .thenReturn(anArchivedDataInstance("myVar").withContainerId(12).withType(String.class.getName())
                        .withValue("Hello World").withArchivedDate(archivedDate).build());

        Response response = request(BonitaRestletApplication.BPM_ARCHIVED_CASE_VARIABLE_URL + "/12/myVar").get();

        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);

        ArchivedCaseVariable archivedVariable = objectMapper.readValue(response.getEntityAsText(), ArchivedCaseVariable.class);
        assertThat(archivedVariable.getName()).isEqualTo("myVar");
        assertThat(archivedVariable.getCaseId()).isEqualTo("12");
        assertThat(archivedVariable.getType()).isEqualTo(String.class.getName());
        assertThat(archivedVariable.getValue()).isEqualTo("Hello World");
        assertThat(archivedVariable.getArchivedDate()).isEqualTo(archivedDate);
    }

}
