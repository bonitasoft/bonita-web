package org.bonitasoft.web.rest.server.api.document.api.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.File;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.document.DocumentItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentDatastoreTest {

    @Mock
    private APISession session;

    @Mock
    private ProcessAPI processAPI;

    private DocumentDatastore documentDatastore;

    @Mock
    private BonitaHomeFolderAccessor tenantFolder;

    @Test
    public void should_verify_authorisation_for_the_given_document_path() throws
    Exception {
        documentDatastore = spy(new DocumentDatastore(session));
        doReturn(1L).when(session).getTenantId();
        doReturn(processAPI).when(documentDatastore).getProcessAPI();
        doReturn(new File("doc.txt")).when(tenantFolder).getTempFile("docPath", 1L);

        final DocumentItem item = documentDatastore.createDocument(1L, "docName", "docType", "docPath", tenantFolder);
        assertNotNull(item);
    }

}
