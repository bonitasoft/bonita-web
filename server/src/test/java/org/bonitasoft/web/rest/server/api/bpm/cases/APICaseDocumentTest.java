package org.bonitasoft.web.rest.server.api.bpm.cases;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.CaseDocumentItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CaseDocumentDatastore;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

public class APICaseDocumentTest extends APITestWithMock {

    @Spy
    private APICaseDocument apiDocument;

    @Mock
    private CaseDocumentDatastore datastore;

    @Mock
    private APIServletCall caller;

    @Mock
    private CaseDocumentItem documentItemMock;

    @Before
    public void initializeMocks() {
        initMocks(this);
        // apiDocument = spy(new APIDocument());
        doReturn(datastore).when(apiDocument).getCaseDocumentDatastore();
    }

    @Test
    public void it_should_call_the_datastore_get_method() {
        // Given
        final APIID id = APIID.makeAPIID(1l);

        // When
        apiDocument.get(id);

        // Then
        verify(datastore).get(id);
    }

    @Test
    public void it_should_call_the_datastore_add_method() {
        // Given

        // When
        apiDocument.add(documentItemMock);

        // Then
        verify(datastore).add(documentItemMock);
    }

    @Test
    public void it_should_call_the_datastore_update_method() {
        // Given
        final APIID id = APIID.makeAPIID(1l);
        final Map<String, String> attributes = null;

        // When
        apiDocument.update(id, attributes);

        // Then
        verify(datastore).update(id, attributes);
    }

    @Test
    public void it_should_call_the_datastore_search_method() {
        // When
        apiDocument.search(0, 10, "hello", "documentName ASC", null);

        // Then
        verify(datastore).search(0, 10, "hello", null, "documentName ASC");
    }

}
