package org.bonitasoft.web.rest.server.api.document;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.document.DocumentItem;
import org.bonitasoft.web.rest.server.api.document.api.impl.DocumentDatastore;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.rest.server.framework.Deployer;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.server.utils.ServerDateFormater;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class APIDocumentTest {

    @Mock
    APIServletCall caller;

    @Mock
    private HttpSession session;

    @Mock
    Deployer deployer;

    @Mock
    DocumentItem item;

    APIDocument apiDocument;

    @Mock
    private ItemDefinitionFactory factory;

    @Mock
    private DocumentDatastore documentDatastore;

    @Mock
    private APISession engineSession;

    @Before
    public void setup() throws Exception {
        given(factory.defineItemDefinitions("theme")).willReturn(null);
        given(caller.getHttpSession()).willReturn(session);
        given(session.getAttribute("apiSession")).willReturn(engineSession);
        given(engineSession.getTenantId()).willReturn(1L);

        ItemDefinitionFactory.setDefaultFactory(factory);
        I18n.getInstance();
        CommonDateFormater.setDateFormater(new ServerDateFormater());
        apiDocument = spy(new APIDocument());
        apiDocument.setCaller(caller);
        doReturn(documentDatastore).when(apiDocument).getDataStore();
        doReturn("../../..").when(item).getAttributeValue(DocumentItem.DOCUMENT_UPLOAD);
        doReturn("doc").when(item).getAttributeValue(DocumentItem.DOCUMENT_NAME);
        doReturn("1").when(item).getAttributeValue(DocumentItem.PROCESSINSTANCE_ID);
        doReturn("type").when(item).getAttributeValue(DocumentItem.DOCUMENT_CREATION_TYPE);

    }

    @Test(expected = APIForbiddenException.class)
    public void should_verify_authorisation_for_the_given_document_path() throws
    Exception {

        doThrow(new UnauthorizedFolderException("error")).when(documentDatastore).createDocument(any(Long.class), any(String.class), any(String.class),
                any(String.class), any(BonitaHomeFolderAccessor.class));
        apiDocument.add(item);

    }

    @Test(expected = APIException.class)
    public void it_throws_an_exception_when_cannot_write_file_on_add() throws Exception {
        // Given
        doThrow(new IOException("error")).when(documentDatastore).createDocument(any(Long.class), any(String.class), any(String.class),
                any(String.class), any(BonitaHomeFolderAccessor.class));

        // When
        try {
            apiDocument.add(item);
        } catch (final APIForbiddenException e) {
            fail("Don't expect the APIException to be an APIForbiddenException");
        }

    }
}
