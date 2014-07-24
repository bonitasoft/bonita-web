package org.bonitasoft.web.rest.server.api.document;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.web.rest.model.document.DocumentItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.api.document.api.impl.DocumentDatastore;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

public class APIDocumentTest  extends APITestWithMock {
	
	@Spy
	private APIDocument apiDocument;

    @Mock
    private DocumentDatastore datastore;
    
	@Mock
	private APIServletCall caller;

	@Mock
	private DocumentItem documentItemMock;
	
	@Before
    public void initializeMocks() {
        initMocks(this);
        //apiDocument = spy(new APIDocument());       
        doReturn(datastore).when(apiDocument).getDocumentDatastore();
    }
	
	@Test
	public void it_should_call_the_datastore_get_method() throws Exception {
		//Given
		APIID id = APIID.makeAPIID(1l);
		
		//When
		apiDocument.get(id);
		
		//Then
		verify(datastore).get(id);
	}

	@Test
	public void it_should_call_the_datastore_add_method() throws Exception {
		//Given		
		
		//When
		apiDocument.add(documentItemMock);
		
		//Then
		verify(datastore).add(documentItemMock);
	}

}
