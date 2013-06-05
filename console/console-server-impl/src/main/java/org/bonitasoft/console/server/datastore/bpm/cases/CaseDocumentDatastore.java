/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.server.datastore.bpm.cases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.model.bpm.cases.CaseDocumentItem;
import org.bonitasoft.console.common.server.datastore.CommonDatastore;
import org.bonitasoft.console.common.server.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentsSearchDescriptor;
import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APISessionInvalidException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasGet;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasSearch;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasUpdate;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Paul AMAR
 * 
 */
public class CaseDocumentDatastore extends CommonDatastore<CaseDocumentItem, Document> implements
        DatastoreHasGet<CaseDocumentItem>, DatastoreHasUpdate<CaseDocumentItem>, DatastoreHasSearch<CaseDocumentItem> {

    /**
     * Default Constructor.
     * 
     * @param engineSession
     */
    public CaseDocumentDatastore(final APISession engineSession) {
        super(engineSession);
    }

    @Override
    public CaseDocumentItem update(final APIID id, final Map<String, String> attributes) {
        final ProcessAPI processAPI = getProcessAPI();
        final String processInstanceId = attributes.get(CaseDocumentItem.ATTRIBUTE_CASE_ID);
        final String name = attributes.get(CaseDocumentItem.ATTRIBUTE_NAME);
        final String fileName = attributes.get(CaseDocumentItem.ATTRIBUTE_FILENAME);
        final String mimeType = attributes.get(CaseDocumentItem.ATTRIBUTE_CONTENT_MIME_TYPE);

        // add new version of the document
        Document newDocument = null;
        try {
            if (attributes.containsKey(CaseDocumentItem.ATTRIBUTE_URL)) {
                final String url = attributes.get(CaseDocumentItem.ATTRIBUTE_URL);
                newDocument = processAPI.attachNewDocumentVersion(Long.parseLong(processInstanceId), name, fileName, mimeType, url);
            } else if (attributes.containsKey(CaseDocumentItem.ATTRIBUTE_FILE)) {
                newDocument = processAPI.attachNewDocumentVersion(Long.parseLong(processInstanceId), name, fileName, mimeType,
                        readBytesFromFile(attributes.get(CaseDocumentItem.ATTRIBUTE_FILE)));
            } else {
                throw new APIException("Missing URL or BYTES Attributes.");
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
        return get(APIID.makeAPIID(newDocument.getId()));
    }

    private byte[] readBytesFromFile(final String fileName) {
        File file = new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            final byte[] bfile = new byte[(int) file.length()];
            try {
                fis.read(bfile);
            } catch (IOException e) {
                throw new APIException(e);
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new APIException(e);
                }
            }
            return bfile;
        } catch (FileNotFoundException e) {
            throw new APIException(e);
        }

    }

    private ProcessAPI getProcessAPI() {
        try {
            return TenantAPIAccessor.getProcessAPI(getEngineSession());
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public CaseDocumentItem get(final APIID id) {
        try {
            final Document result = TenantAPIAccessor.getProcessAPI(getEngineSession()).getDocument(id.toLong());
            final CaseDocumentItem res = convertEngineToConsoleItem(result);
            res.setId(id);

            return res;

        } catch (final InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public ItemSearchResult<CaseDocumentItem> search(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        // TODO
        // define default search order in APICaseDocument with DocumentCriterion
        final SearchOptionsBuilder builder = SearchOptionsBuilderUtil.buildSearchOptions(page, resultsByPage, orders, search);
        builder.sort(DocumentsSearchDescriptor.DOCUMENT_NAME, Order.DESC);

        addFilterToSearchBuilder(filters, builder, CaseDocumentItem.ATTRIBUTE_FILE, DocumentsSearchDescriptor.CONTENT_STORAGE_ID);
        addFilterToSearchBuilder(filters, builder, CaseDocumentItem.ATTRIBUTE_AUTHOR, DocumentsSearchDescriptor.DOCUMENT_AUTHOR);
        addFilterToSearchBuilder(filters, builder, CaseDocumentItem.ATTRIBUTE_FILENAME, DocumentsSearchDescriptor.DOCUMENT_CONTENT_FILENAME);
        addFilterToSearchBuilder(filters, builder, CaseDocumentItem.ATTRIBUTE_CONTENT_MIME_TYPE, DocumentsSearchDescriptor.DOCUMENT_CONTENT_MIMETYPE);
        addFilterToSearchBuilder(filters, builder, CaseDocumentItem.ATTRIBUTE_CREATION_DATE, DocumentsSearchDescriptor.DOCUMENT_CREATIONDATE);
        addFilterToSearchBuilder(filters, builder, CaseDocumentItem.ATTRIBUTE_HAS_CONTENT, DocumentsSearchDescriptor.DOCUMENT_HAS_CONTENT);
        addFilterToSearchBuilder(filters, builder, CaseDocumentItem.ATTRIBUTE_NAME, DocumentsSearchDescriptor.DOCUMENT_NAME);
        addFilterToSearchBuilder(filters, builder, CaseDocumentItem.ATTRIBUTE_URL, DocumentsSearchDescriptor.DOCUMENT_URL);
        addFilterToSearchBuilder(filters, builder, CaseDocumentItem.ATTRIBUTE_CASE_ID, DocumentsSearchDescriptor.PROCESSINSTANCE_ID);

        try {
            final APIID supervisorAPIID = APIID.makeAPIID(filters.get(CaseDocumentItem.FILTER_SUPERVISOR_ID));

            // call right search method
            SearchResult<Document> engineSearchResults;
            if (supervisorAPIID != null) {
                engineSearchResults = getProcessAPI().searchDocumentsSupervisedBy(supervisorAPIID.toLong(), builder.done());
            } else {
                engineSearchResults = getProcessAPI().searchDocuments(builder.done());
            }

            // parse result and convert item into console items
            final List<CaseDocumentItem> consoleSearchResults = new ArrayList<CaseDocumentItem>();
            for (final Document engineItem : engineSearchResults.getResult()) {
                consoleSearchResults.add(convertEngineToConsoleItem(engineItem));
            }
            return new ItemSearchResult<CaseDocumentItem>(page, resultsByPage, engineSearchResults.getCount(), consoleSearchResults);
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    protected CaseDocumentItem convertEngineToConsoleItem(final Document item) {
        final CaseDocumentItem caseDocument = new CaseDocumentItem();

        caseDocument.setAuthor(item.getAuthor());
        caseDocument.setId(APIID.makeAPIID(item.getId()));
        caseDocument.setContentMimeType(item.getContentMimeType());
        caseDocument.setCreationDate(item.getCreationDate());
        caseDocument.setHasContent(item.hasContent());
        caseDocument.setProcessInstanceId(item.getProcessInstanceId());
        caseDocument.setFile(item.getContentStorageId());
        caseDocument.setUrl(item.getUrl());
        caseDocument.setName(item.getName());
        caseDocument.setFileName(item.getContentFileName());

        return caseDocument;
    }
}
