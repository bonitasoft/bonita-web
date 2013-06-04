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
package org.bonitasoft.console.server.api.bpm.cases;

import java.util.Map;

import org.bonitasoft.console.client.model.bpm.cases.CaseDocumentDefinition;
import org.bonitasoft.console.client.model.bpm.cases.CaseDocumentItem;
import org.bonitasoft.console.server.api.ConsoleAPI;
import org.bonitasoft.console.server.datastore.bpm.cases.CaseDocumentDatastore;
import org.bonitasoft.engine.bpm.document.DocumentsSearchDescriptor;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.api.APIHasDelete;
import org.bonitasoft.web.toolkit.server.api.APIHasGet;
import org.bonitasoft.web.toolkit.server.api.APIHasSearch;
import org.bonitasoft.web.toolkit.server.api.APIHasUpdate;
import org.bonitasoft.web.toolkit.server.api.Datastore;

/**
 * @author Paul AMAR
 * 
 */
public class APICaseDocument extends ConsoleAPI<CaseDocumentItem> implements
        APIHasGet<CaseDocumentItem>,
        APIHasUpdate<CaseDocumentItem>,
        APIHasDelete,
        APIHasSearch<CaseDocumentItem> {

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.toolkit.server.API#update(org.bonitasoft.web.toolkit.client.data.APIID, java.util.Map)
     */
    @Override
    public CaseDocumentItem update(final APIID id, final Map<String, String> attributes) {

        // check for mandatories parameters
        if (!attributes.containsKey(CaseDocumentItem.ATTRIBUTE_CASE_ID)
                || !attributes.containsKey(CaseDocumentItem.ATTRIBUTE_NAME)
                || !attributes.containsKey(CaseDocumentItem.ATTRIBUTE_FILENAME)
                || !attributes.containsKey(CaseDocumentItem.ATTRIBUTE_CONTENT_MIME_TYPE)
                || !attributes.containsKey(CaseDocumentItem.ATTRIBUTE_URL) && !attributes.containsKey(CaseDocumentItem.ATTRIBUTE_FILE)) {
            throw new APIException("Missing mandatory parameters <" + CaseDocumentItem.ATTRIBUTE_CASE_ID + "," + CaseDocumentItem.ATTRIBUTE_NAME
                    + "," + CaseDocumentItem.ATTRIBUTE_FILENAME + "," + CaseDocumentItem.ATTRIBUTE_CONTENT_MIME_TYPE + ">");
        }

        return super.update(id, attributes);
    }

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(CaseDocumentDefinition.TOKEN);
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new CaseDocumentDatastore(getEngineSession());
    }

    @Override
    public String defineDefaultSearchOrder() {
        return DocumentsSearchDescriptor.DOCUMENT_NAME;
    }
}
