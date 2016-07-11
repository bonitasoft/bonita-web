/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.document;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Gai Cuisha
 * 
 */
public class DocumentDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final DocumentDefinition get() {
        return (DocumentDefinition) Definitions.get(TOKEN);
    }

    /**
     * token
     */
    public static final String TOKEN = "document";

    /**
     * the URL of document
     */
    private static final String API_URL = "../API/bpm/document";

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
        createAttribute(DocumentItem.DOCUMENT_ID, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_VERSION, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.PROCESSINSTANCE_ID, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.PROCESSINSTANCE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.PROCESS_DISPLAY_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.PROCESS_VERSION, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_AUTHOR, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_CREATIONDATE, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_HAS_CONTENT, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_CONTENT_FILENAME, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_CONTENT_MIMETYPE, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.CONTENT_STORAGE_ID, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_URL, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_CREATION_TYPE, ItemAttribute.TYPE.STRING);
        createAttribute(DocumentItem.DOCUMENT_UPLOAD, ItemAttribute.TYPE.STRING);

    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(DocumentItem.DOCUMENT_ID);
    }

    @Override
    protected IItem _createItem() {
        return new DocumentItem();
    }
}
