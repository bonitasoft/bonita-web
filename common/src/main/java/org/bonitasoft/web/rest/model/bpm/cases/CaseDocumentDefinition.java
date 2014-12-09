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
package org.bonitasoft.web.rest.model.bpm.cases;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Paul AMAR
 *
 */
public class CaseDocumentDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final CaseDocumentDefinition get() {
        return (CaseDocumentDefinition) Definitions.get(TOKEN);
    }

    /**
     * token
     */
    public static final String TOKEN = "casedocument";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/bpm/caseDocument";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(CaseDocumentItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(CaseDocumentItem.ATTRIBUTE_VERSION, ItemAttribute.TYPE.STRING);
        createAttribute(CaseDocumentItem.ATTRIBUTE_CASE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(CaseDocumentItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(CaseDocumentItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(CaseDocumentItem.ATTRIBUTE_SUBMITTED_BY_USER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(CaseDocumentItem.ATTRIBUTE_CREATION_DATE, ItemAttribute.TYPE.DATE);
        createAttribute(CaseDocumentItem.ATTRIBUTE_HAS_CONTENT, ItemAttribute.TYPE.BOOLEAN);
        createAttribute(CaseDocumentItem.ATTRIBUTE_CONTENT_FILENAME, ItemAttribute.TYPE.STRING);
        createAttribute(CaseDocumentItem.ATTRIBUTE_CONTENT_MIMETYPE, ItemAttribute.TYPE.STRING);
        createAttribute(CaseDocumentItem.ATTRIBUTE_CONTENT_STORAGE_ID, ItemAttribute.TYPE.STRING);
        createAttribute(CaseDocumentItem.ATTRIBUTE_URL, ItemAttribute.TYPE.URL);
        createAttribute(CaseDocumentItem.ATTRIBUTE_UPLOAD_PATH, ItemAttribute.TYPE.STRING);
        createAttribute(CaseDocumentItem.ATTRIBUTE_INDEX, ItemAttribute.TYPE.STRING);
        createAttribute(CaseDocumentItem.ATTRIBUTE_AUTHOR, ItemAttribute.TYPE.STRING);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(CaseDocumentItem.ATTRIBUTE_ID);
    }

    @Override
    protected IItem _createItem() {
        return new CaseDocumentItem();
    }
}
