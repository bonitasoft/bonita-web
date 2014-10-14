/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
 * @author Fabio Lombardi
 *
 */
public class ArchivedCaseDocumentDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final ArchivedCaseDocumentDefinition get() {
        return (ArchivedCaseDocumentDefinition) Definitions.get(TOKEN);
    }

    /**
     * token
     */
    public static final String TOKEN = "archivedcasedocument";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/bpm/archviedCaseDocument";

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
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_VERSION, ItemAttribute.TYPE.STRING);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_CASE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_SOURCE_OBJECT_ID, ItemAttribute.TYPE.STRING);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_ARCHIVED_DATE, ItemAttribute.TYPE.DATE);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_SUBMITTED_BY_USER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_CREATION_DATE, ItemAttribute.TYPE.DATE);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_HAS_CONTENT, ItemAttribute.TYPE.BOOLEAN);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_CONTENT_FILENAME, ItemAttribute.TYPE.STRING);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_CONTENT_MIMETYPE, ItemAttribute.TYPE.STRING);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_CONTENT_STORAGE_ID, ItemAttribute.TYPE.STRING);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_URL, ItemAttribute.TYPE.URL);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_UPLOAD_PATH, ItemAttribute.TYPE.STRING);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_INDEX, ItemAttribute.TYPE.STRING);
        createAttribute(ArchivedCaseDocumentItem.ATTRIBUTE_AUTHOR, ItemAttribute.TYPE.STRING);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ArchivedCaseDocumentItem.ATTRIBUTE_ID);
    }

    @Override
    protected IItem _createItem() {
        return new ArchivedCaseDocumentItem();
    }

}
