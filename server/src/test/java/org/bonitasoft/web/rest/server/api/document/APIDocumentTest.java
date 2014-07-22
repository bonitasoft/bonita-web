package org.bonitasoft.web.rest.server.api.document;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

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

	@Before
    public void initializeMocks() {
        initMocks(this);
        //apiDocument = spy(new APIDocument());       
        doReturn(datastore).when(apiDocument).getDefaultDatastore();
    }
	
	@Test
	public void testGet() throws Exception {
		//Given
		APIID id = APIID.makeAPIID(1l);
		
		//When
		apiDocument.get(id);
		
		//Then
		verify(datastore).get(id);
		//throw new RuntimeException("not yet implemented");*/
	}

	@Test
	public void testAdd() throws Exception {
		//throw new RuntimeException("not yet implemented");
	}

}
