/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.client.document.model.ArchivedDocumentDefinition;
import org.bonitasoft.console.common.client.document.model.ArchivedDocumentItem;
import org.bonitasoft.console.common.client.document.model.DocumentItem;
import org.bonitasoft.console.common.server.CommonAPI;
import org.bonitasoft.console.common.server.document.api.impl.DocumentDatastore;
import org.bonitasoft.console.common.server.utils.SearchOptionsBuilderUtil;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.document.ArchivedDocument;
import org.bonitasoft.engine.bpm.document.ArchivedDocumentsSearchDescriptor;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Julien Mege
 */
public class APIArchivedDocument extends CommonAPI<ArchivedDocumentItem> {

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(ArchivedDocumentDefinition.TOKEN);
    }

    @Override
    public ArchivedDocumentItem get(final APIID id) {
        final APISession apiSession = getEngineSession();
        ArchivedDocumentItem item = new ArchivedDocumentItem();
        try {
            final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(apiSession);
            final DocumentDatastore dataStore = new DocumentDatastore(apiSession);
            final ArchivedDocument document = processAPI.getArchivedProcessDocument(id.toLong());
            item = dataStore.mapToArchivedDocumentItem(document);
        } catch (final Exception e) {
            throw new APIException(e);
        }
        return item;
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }

    @Override
    public ItemSearchResult<ArchivedDocumentItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        final APISession apiSession = getEngineSession();
        final List<ArchivedDocumentItem> items = new ArrayList<ArchivedDocumentItem>();
        long nbOfDocument = 0;
        String caseId = null;
        String viewType = null;
        String documentName = null;
        long userId = -1;
        try {
            if (filters.containsKey(ArchivedDocumentItem.FILTER_CASE_ID)) {
                caseId = filters.get(ArchivedDocumentItem.FILTER_CASE_ID);
            }
            if (filters.containsKey(ArchivedDocumentItem.FILTER_VIEW_TYPE)) {
                viewType = filters.get(ArchivedDocumentItem.FILTER_VIEW_TYPE);
            }
            if (filters.containsKey(ArchivedDocumentItem.FILTER_USER_ID)) {
                final String user = filters.get(ArchivedDocumentItem.FILTER_USER_ID);
                if (user != null) {
                    userId = Long.valueOf(user);
                } else {
                    userId = apiSession.getUserId();
                }
            }
            if (filters.containsKey(DocumentItem.DOCUMENT_NAME)) {
                documentName = filters.get(DocumentItem.DOCUMENT_NAME);
            }

            final SearchOptionsBuilder builder = SearchOptionsBuilderUtil.buildSearchOptions(page, resultsByPage, orders, search);
            if (caseId != null) {
                builder.filter(ArchivedDocumentsSearchDescriptor.PROCESSINSTANCE_ID, caseId);
            }
            if (documentName != null) {
                builder.filter(ArchivedDocumentsSearchDescriptor.DOCUMENT_NAME, documentName);
            }

            SearchResult<ArchivedDocument> result = null;
            final DocumentDatastore dataStore = new DocumentDatastore(apiSession);
            if (userId != -1 && viewType != null) {
                result = dataStore.searchArchivedDocuments(userId, viewType, builder);
            }
            if (result != null) {
                nbOfDocument = result.getCount();
                for (final ArchivedDocument document : result.getResult()) {
                    items.add(dataStore.mapToArchivedDocumentItem(document));
                }
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
        return new ItemSearchResult<ArchivedDocumentItem>(page, resultsByPage, nbOfDocument, items);

    }

    @Override
    protected void fillDeploys(final ArchivedDocumentItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final ArchivedDocumentItem item, final List<String> counters) {
    }

}
