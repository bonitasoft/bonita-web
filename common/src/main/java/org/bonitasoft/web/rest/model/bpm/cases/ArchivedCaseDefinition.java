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
package org.bonitasoft.web.rest.model.bpm.cases;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * Archived process instance definition
 *
 * @author Séverin Moussel
 */
public class ArchivedCaseDefinition extends ItemDefinition<ArchivedCaseItem> {

    /**
     * Singleton
     */
    public static ArchivedCaseDefinition get() {
        return (ArchivedCaseDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "archivedcases";

    /**
     * the URL of user resource
     */
    protected static final String API_URL = "../API/bpm/archivedCase";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(CaseItem.ATTRIBUTE_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(ArchivedCaseItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ArchivedCaseItem.ATTRIBUTE_STATE, ItemAttribute.TYPE.ENUM);
        createAttribute(ArchivedCaseItem.ATTRIBUTE_START_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(ArchivedCaseItem.ATTRIBUTE_LAST_UPDATE_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(ArchivedCaseItem.ATTRIBUTE_END_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(ArchivedCaseItem.ATTRIBUTE_PROCESS_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ArchivedCaseItem.ATTRIBUTE_ARCHIVED_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(ArchivedCaseItem.ATTRIBUTE_STARTED_BY_USER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ArchivedCaseItem.ATTRIBUTE_STARTED_BY_SUBSTITUTE_USER_ID, ItemAttribute.TYPE.ITEM_ID);
    }

    @Override
    public ArchivedCaseItem _createItem() {
        return new ArchivedCaseItem();
    }

}
