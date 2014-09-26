package org.bonitasoft.web.rest.server.api.document.api.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentAttachmentException;
import org.bonitasoft.engine.bpm.document.DocumentException;
import org.bonitasoft.engine.bpm.document.DocumentNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.RetrieveException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.rest.model.document.DocumentItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DocumentDatastoreTest extends APITestWithMock {

    private DocumentDatastore documentDatastore;

    @Mock
    private WebBonitaConstantsUtils constantsValue;

    @Mock
    private APISession engineSession;

    @Mock
    private ProcessAPI processAPI;

    @Mock
    private Document mockedDocument;

    @Mock
    private SearchResult<Document> mockedEngineSearchResults;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private final DocumentItem mockedDocumentItem = new DocumentItem();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        System.setProperty("bonita.home", "target/bonita-home/bonita");
        when(engineSession.getTenantId()).thenReturn(1L);
        when(mockedDocument.getName()).thenReturn("Doc 1");
        when(mockedDocument.getId()).thenReturn(1L);
        documentDatastore = spy(new DocumentDatastore(engineSession, constantsValue, processAPI));
    }

    // ---------- GET METHOD TESTS ------------------------------//

    @Test
    public void it_should_call_engine_processAPI_getDocument() throws Exception {
        // Given
        final APIID id = APIID.makeAPIID(1l);

        // When
        documentDatastore.get(id);

        // Then
        verify(processAPI).getDocument(id.toLong());
    }

    @Test(expected = APIException.class)
    public void it_should_catch_and_throw_APIException_for_not_find_document() throws Exception {
        // Given
        final APIID id = APIID.makeAPIID(1l);
        when(processAPI.getDocument(id.toLong())).thenThrow(new DocumentNotFoundException("not found", new Exception()));

        // When
        documentDatastore.get(id);
    }

    @Test
    public void it_should_call_convertEngineToConsole_method() throws Exception {
        // Given
        final APIID id = APIID.makeAPIID(1l);

        // When
        documentDatastore.get(id);

        // Then
        verify(documentDatastore).convertEngineToConsoleItem(any(Document.class));
    }

    // ---------- CONVERT ITEM TESTS ------------------------------//

    @Test
    public void it_should_convert_item_return_item() throws Exception {
        // When
        final DocumentItem convertedEngineToConsoleItem = documentDatastore.convertEngineToConsoleItem(mockedDocument);
        // Then
        assertTrue(convertedEngineToConsoleItem != null);
    }

    @Test
    public void it_should_not_convert_null_item_return_null() {
        // When
        final DocumentItem convertedEngineToConsoleItem = documentDatastore.convertEngineToConsoleItem(null);
        // Then
        assertTrue(convertedEngineToConsoleItem == null);
    }

    // ---------- ADD METHOD TESTS ------------------------------//

    @Test
    public void it_should_add_a_document_and_call_attachDocument() throws Exception {
        // Given
        final URL docUrl = getClass().getResource("/doc.jpg");
        mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_CASE_ID, 1l);
        mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_NAME, "doc 1");
        mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_UPLOAD_PATH, docUrl.getPath());
        // byte[] fileContent = DocumentUtil.getArrayByteFromFile(new File(docUrl));
        // When
        documentDatastore.add(mockedDocumentItem);

        // Then
        // verify(processAPI).attachDocument(1l, "doc 1", "doc.jpg", "img", documentContent)
        verify(documentDatastore).attachDocument(1l, "doc 1", docUrl.getPath(), "AddNewDocument", null);
        // throw new RuntimeException("not yet implemented");
    }

    @Test
    public void it_should_add_a_document_and_call_attachDocumentFromUrl() throws Exception {
        // Given
        final URL docUrl = getClass().getResource("/doc.jpg");
        mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_CASE_ID, 1l);
        mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_NAME, "doc 1");
        mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_URL, "http://images/doc.jpg");
        // byte[] fileContent = DocumentUtil.getArrayByteFromFile(new File(docUrl));
        // When
        documentDatastore.add(mockedDocumentItem);

        // Then
        // verify(processAPI).attachDocument(1l, "doc 1", "doc.jpg", "img", documentContent)
        verify(documentDatastore).attachDocumentFromUrl(1l, "doc 1", "http://images/doc.jpg", "AddNewDocument", null);
        // throw new RuntimeException("not yet implemented");
    }

    @Test(expected = APIException.class)
    public void it_throws_an_exception_adding_a_document_with_invalid_inputs() {
        // Given
        mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_CASE_ID, -1);
        mockedDocumentItem.setAttribute(DocumentItem.PROCESSINSTANCE_ID, -1);
        mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_NAME, "");
        // byte[] fileContent = DocumentUtil.getArrayByteFromFile(new File(docUrl));
        // When
        documentDatastore.add(mockedDocumentItem);

    }

    @Test(expected = APIException.class)
    public void it_throws_an_exception_adding_a_document_with_missing_inputs() {
        // Given
        mockedDocumentItem.setAttribute(DocumentItem.ATTRIBUTE_NAME, "");
        // byte[] fileContent = DocumentUtil.getArrayByteFromFile(new File(docUrl));
        // When
        documentDatastore.add(mockedDocumentItem);

    }

    // ---------- ATTACH DOCUMENT TESTS ------------------------------//
    @Test
    public void it_should_call_processAPI_attachDocument_method_with_document_content() throws Exception, DocumentAttachmentException, InvalidSessionException,
    ProcessDefinitionNotFoundException, RetrieveException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, DocumentException,
    IOException {
        // Given
        final URL docUrl = getClass().getResource("/doc.jpg");

        // When
        documentDatastore.attachDocument(1l, "Doc 1", docUrl.getPath(), "AddNewDocument", null);

        // Then
        verify(processAPI).attachDocument(1l, "Doc 1", "doc.jpg", "image/jpeg", DocumentUtil.getArrayByteFromFile(new File(docUrl.getPath())), null);
    }

    @Test
    public void it_should_call_processAPI_attachNewDocumentVersion_method_with_document_content() {
        // Given
        final URL docUrl = getClass().getResource("/doc.jpg");

        try {
            // When
            documentDatastore.attachDocument(1l, "Doc 1", docUrl.getPath(), "AddNewVersionDocument", null);
            // Then
            verify(processAPI).attachNewDocumentVersion(1l, "Doc 1", "doc.jpg", "image/jpeg", DocumentUtil.getArrayByteFromFile(new File(docUrl.getPath())),
                    null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- UPDATE METHOD TESTS ------------------------------//

    @Test
    public void it_should_update_document_calling_attachDocument_with_new_document_version() {
        try {
            // Given
            final Map<String, String> attributes = new HashMap<String, String>();
            attributes.put(DocumentItem.ATTRIBUTE_UPLOAD_PATH, "C:\\doc.jpg");
            final APIID id = APIID.makeAPIID(1l);
            when(processAPI.getDocument(1l)).thenReturn(mockedDocument);
            // When
            documentDatastore.update(id, attributes);
            // Then
            verify(documentDatastore).attachDocument(0l, "Doc 1", "C:\\doc.jpg", "AddNewVersionDocument", null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void it_should_update_document_calling_attachDocumentFromUrl_with_new_document_version() {
        try {
            // Given
            final Map<String, String> attributes = new HashMap<String, String>();
            attributes.put(DocumentItem.ATTRIBUTE_URL, "http://images/doc.jpg");
            final APIID id = APIID.makeAPIID(1l);
            when(processAPI.getDocument(1l)).thenReturn(mockedDocument);
            // When
            documentDatastore.update(id, attributes);
            // Then
            verify(documentDatastore).attachDocumentFromUrl(0l, "Doc 1", "http://images/doc.jpg", "AddNewVersionDocument", null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = APIException.class)
    public void it_should_not_update_document_and_throws_exception_for_missing_uploadPath() {
        // Given
        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(DocumentItem.ATTRIBUTE_NAME, "Doc 1");
        final APIID id = APIID.makeAPIID(1l);
        try {
            when(processAPI.getDocument(1l)).thenReturn(mockedDocument);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        // When
        documentDatastore.update(id, attributes);
    }

    // ---------- ATTACH DOCUMENT FROM URL TESTS ------------------------------//
    @Test
    public void it_should_call_processAPI_attachDocument_method_with_external_url() throws Exception {
        // Given

        // When
        documentDatastore.attachDocumentFromUrl(1l, "Doc 1", "http://images/doc.jpg", "AddNewDocument", null);

        // Then
        verify(processAPI).attachDocument(1l, "Doc 1", "doc.jpg", "image/jpeg", "http://images/doc.jpg", null);
    }

    @Test
    public void it_should_call_processAPI_attachNewDocumentVersion_method_with_external_url() throws Exception {
        // Given

        // When
        documentDatastore.attachDocumentFromUrl(1l, "Doc 1", "http://images/doc.jpg", "AddNewVersionDocument", null);

        // Then
        verify(processAPI).attachNewDocumentVersion(1l, "Doc 1", "doc.jpg", "image/jpeg", "http://images/doc.jpg", null);
    }

    // ---------- SEARCH TESTS -------------------------------------------------//
    @Test
    public void it_should_call_buildSearchOptionCreator_method() throws SearchException {
        // Given
        when(processAPI.searchDocuments(any(SearchOptions.class))).thenReturn(mockedEngineSearchResults);
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put("submittedBy", "1");

        // When
        documentDatastore.searchDocument(0, 10, "hello", filters, "documentName ASC");

        // Then
        verify(documentDatastore).buildSearchOptionCreator(0, 10, "hello", filters, "documentName ASC");
    }

    @Test
    public void it_should_call_processAPI_searchDocuments_method() throws SearchException {
        // Given
        when(processAPI.searchDocuments(any(SearchOptions.class))).thenReturn(mockedEngineSearchResults);
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put("submittedBy", "1");

        // When
        documentDatastore.searchDocument(0, 10, "hello", filters, "documentName ASC");

        // Then
        verify(processAPI).searchDocuments(documentDatastore.searchOptionsCreator.create());
    }

    // -------------DELETE METHOD TESTS ------------------------------------------//
    @Test
    public void it_should_delete_one_document() throws DocumentNotFoundException, DeletionException {
        final List<APIID> docs = new ArrayList<APIID>();
        docs.add(APIID.makeAPIID(mockedDocument.getId()));

        // When
        documentDatastore.delete(docs);

        // Then
        verify(processAPI).removeDocument(1L);
        verify(processAPI, times(1)).removeDocument(any(Long.class));
    }

    @Test
    public void it_should_delete_two_documents() throws DocumentNotFoundException, DeletionException {
        final List<APIID> docs = new ArrayList<APIID>();
        docs.add(APIID.makeAPIID(mockedDocument.getId()));
        docs.add(APIID.makeAPIID(mockedDocument.getId()));

        // When
        documentDatastore.delete(docs);

        // Then
        verify(processAPI, times(2)).removeDocument(1L);
    }

    @Test
    public void it_should_throw_an_exception_when_input_is_null() throws DocumentNotFoundException, DeletionException {
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Error while deleting a document. Document id not specified in the request");

        final List<APIID> docs = new ArrayList<APIID>();
        docs.add(APIID.makeAPIID(mockedDocument.getId()));

        // When
        documentDatastore.delete(null);
    }

    @Test
    public void it_should_throw_an_exception_when_document_is_not_found() throws DocumentNotFoundException, DeletionException {
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Error while deleting a document. Document not found");
        // When
        when(processAPI.removeDocument(3L)).thenThrow(DocumentNotFoundException.class);

        final List<APIID> docs = new ArrayList<APIID>();
        docs.add(APIID.makeAPIID(3L));

        // When
        documentDatastore.delete(docs);
    }
}
