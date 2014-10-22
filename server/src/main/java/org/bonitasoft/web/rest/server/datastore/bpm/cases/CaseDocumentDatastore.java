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
package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentException;
import org.bonitasoft.engine.bpm.document.DocumentNotFoundException;
import org.bonitasoft.engine.bpm.document.DocumentValue;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDocumentItem;
import org.bonitasoft.web.rest.server.api.document.api.impl.DocumentUtil;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasAdd;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasDelete;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasUpdate;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Fabio Lombardi
 *
 */
public class CaseDocumentDatastore extends CommonDatastore<CaseDocumentItem, Document> implements DatastoreHasAdd<CaseDocumentItem>,
DatastoreHasGet<CaseDocumentItem>,
DatastoreHasUpdate<CaseDocumentItem>, DatastoreHasDelete {

    protected final WebBonitaConstantsUtils constants;

    protected final ProcessAPI processAPI;

    final long maxSizeForTenant;

    final FileTypeMap mimetypesFileTypeMap;

    protected SearchOptionsCreator searchOptionsCreator;

    /**
     * Default constructor.
     */
    public CaseDocumentDatastore(final APISession engineSession, final WebBonitaConstantsUtils constantsValue, final ProcessAPI processAPI) {
        super(engineSession);
        constants = constantsValue;
        this.processAPI = processAPI;
        maxSizeForTenant = PropertiesFactory.getConsoleProperties(engineSession.getTenantId()).getMaxSize();
        mimetypesFileTypeMap = new MimetypesFileTypeMap();
    }

    // GET Method
    @Override
    public CaseDocumentItem get(final APIID id) {
        try {
            final Document documentItem = processAPI.getDocument(id.toLong());
            return convertEngineToConsoleItem(documentItem);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    // POST Method
    @Override
    public CaseDocumentItem add(final CaseDocumentItem item) {

        long caseId = -1;
        int index = -1;
        String documentDescription = "";
        DocumentValue documentValue = null;

        try {
            if (item.getAttributeValue(CaseDocumentItem.ATTRIBUTE_CASE_ID) != null) {
                caseId = Long.valueOf(item.getAttributeValue(CaseDocumentItem.ATTRIBUTE_CASE_ID));
            }

        } catch (final NumberFormatException e) {
            throw new APIException("Error while attaching a new document. Request with bad case id value.");
        }

        final String documentName = item.getAttributeValue(CaseDocumentItem.ATTRIBUTE_NAME);
        final String uploadPath = item.getAttributeValue(CaseDocumentItem.ATTRIBUTE_UPLOAD_PATH);
        final String urlPath = item.getAttributeValue(CaseDocumentItem.ATTRIBUTE_URL);

        if (item.getAttributeValue(CaseDocumentItem.ATTRIBUTE_DESCRIPTION) != null) {
            documentDescription = item.getAttributeValue(CaseDocumentItem.ATTRIBUTE_DESCRIPTION);
        }

        if (item.getAttributeValue(CaseDocumentItem.ATTRIBUTE_INDEX) != null) {
            index = Integer.parseInt(item.getAttributeValue(CaseDocumentItem.ATTRIBUTE_INDEX));
        }

        try {

            if (caseId != -1 && documentName != null) {

                if (uploadPath != null && !uploadPath.isEmpty()) {
                    documentValue = buildDocumentValueFromUploadPath(uploadPath, index);
                } else if (urlPath != null && !urlPath.isEmpty()) {
                    documentValue = buildDocumentValueFromUrl(urlPath, index);
                }

                final Document document = processAPI.addDocument(caseId, documentName, documentDescription, documentValue);
                return convertEngineToConsoleItem(document);

            } else {
                throw new APIException("Error while attaching a new document. Request with bad param value.");
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    // PUT Method
    @Override
    public CaseDocumentItem update(final APIID id, final Map<String, String> attributes) {
        DocumentValue documentValue = null;

        try {
            final String urlPath;

            if (attributes.containsKey(CaseDocumentItem.ATTRIBUTE_UPLOAD_PATH) || attributes.containsKey(CaseDocumentItem.ATTRIBUTE_URL)) {

                if (attributes.containsKey(CaseDocumentItem.ATTRIBUTE_UPLOAD_PATH)) {
                    urlPath = attributes.get(CaseDocumentItem.ATTRIBUTE_UPLOAD_PATH);
                    documentValue = buildDocumentValueFromUploadPath(urlPath, -1);
                } else {
                    urlPath = attributes.get(CaseDocumentItem.ATTRIBUTE_URL);
                    documentValue = buildDocumentValueFromUrl(urlPath, -1);
                }

                final Document document = processAPI.updateDocument(id.toLong(), documentValue);
                return convertEngineToConsoleItem(document);

            } else {
                throw new APIException("Error while attaching a new document. Request with bad param value.");
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected DocumentValue buildDocumentValueFromUploadPath(final String uploadPath, final int index) throws DocumentException, IOException {
        String fileName = null;
        String mimeType = null;
        byte[] fileContent = null;

        final File theSourceFile = new File(uploadPath);
        if (theSourceFile.exists()) {
            if (theSourceFile.length() > maxSizeForTenant * 1048576) {
                final String errorMessage = "This document is exceeded " + maxSizeForTenant + "Mb";
                throw new DocumentException(errorMessage);
            }
            fileContent = DocumentUtil.getArrayByteFromFile(theSourceFile);
            if (theSourceFile.isFile()) {
                fileName = theSourceFile.getName();
                mimeType = mimetypesFileTypeMap.getContentType(theSourceFile);
            }
        }

        final DocumentValue documentValue = new DocumentValue(fileContent, mimeType, fileName);
        if (index != -1) {
            documentValue.setIndex(index);
        }
        return documentValue;
    }

    protected DocumentValue buildDocumentValueFromUrl(final String urlPath, final int index) {

        final DocumentValue documentValue = new DocumentValue(urlPath);
        if (index != -1) {
            documentValue.setIndex(index);
        }
        return documentValue;

    }

    // GET Method for SEARCH
    public ItemSearchResult<CaseDocumentItem> search(final int page, final int resultsByPage,
            final String search, final Map<String, String> filters, final String orders) {
        return searchDocument(page, resultsByPage, search, filters, orders);
    }

    protected ItemSearchResult<CaseDocumentItem> searchDocument(final int page, final int resultsByPage, final String search,
            final Map<String, String> filters, final String orders) {

        try {
            final APIID supervisorAPIID = APIID.makeAPIID(filters.get(CaseDocumentItem.FILTER_SUPERVISOR_ID));

            if (supervisorAPIID != null) {
                filters.remove(CaseDocumentItem.FILTER_SUPERVISOR_ID);
            }
            searchOptionsCreator = buildSearchOptionCreator(page, resultsByPage, search, filters, orders);

            final SearchResult<Document> engineSearchResults;
            if (supervisorAPIID != null) {
                engineSearchResults = processAPI.searchDocumentsSupervisedBy(supervisorAPIID.toLong(), searchOptionsCreator.create());
            } else {
                engineSearchResults = processAPI.searchDocuments(searchOptionsCreator.create());
            }
            return new ItemSearchResult<CaseDocumentItem>(page, resultsByPage, engineSearchResults.getCount(),
                    convertEngineToConsoleItem(engineSearchResults.getResult()));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected SearchOptionsCreator buildSearchOptionCreator(final int page, final int resultsByPage, final String search, final Map<String, String> filters,
            final String orders) {
        final SearchOptionsCreator searchOptionsCreator = new SearchOptionsCreator(page, resultsByPage, search, new Sorts(orders,
                getDocumentSearchAttributeConverter()), new Filters(filters, new CaseDocumentFilterCreator(getDocumentSearchAttributeConverter())));
        return searchOptionsCreator;
    }

    private CaseDocumentSearchAttributeConverter getDocumentSearchAttributeConverter() {
        return new CaseDocumentSearchAttributeConverter();
    }

    // DELETE Method
    @Override
    public void delete(final List<APIID> ids) {
        if (ids != null) {

            try {
                for (final APIID id : ids) {
                    processAPI.removeDocument(id.toLong());
                }
            } catch (final DocumentNotFoundException e) {
                throw new APIException("Error while deleting a document. Document not found");
            } catch (final DeletionException e) {
                throw new APIException(e);
            }

        } else {
            throw new APIException("Error while deleting a document. Document id not specified in the request");
        }
    }

    @Override
    protected CaseDocumentItem convertEngineToConsoleItem(final Document item) {
        if (item != null) {
            return new CaseDocumentItemConverter().convert(item);
        }
        return null;
    }

    private List<CaseDocumentItem> convertEngineToConsoleItem(final List<Document> result) {
        if (result != null) {
            return new CaseDocumentItemConverter().convert(result);
        }
        return null;
    }
}