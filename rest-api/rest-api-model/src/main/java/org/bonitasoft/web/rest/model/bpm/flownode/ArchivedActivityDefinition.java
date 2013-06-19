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
package org.bonitasoft.web.rest.model.bpm.flownode;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Séverin Moussel
 * 
 */
public class ArchivedActivityDefinition extends ActivityDefinition {

    public static final String TOKEN = "archivedActivity";

    protected static final String API_URL = "../API/bpm/archivedActivity";

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
        super.defineAttributes();
        createAttribute(ArchivedActivityItem.ATTRIBUTE_ARCHIVED_DATE, ItemAttribute.TYPE.DATETIME);
    }

    @Override
    public ArchivedActivityItem _createItem() {
        return new ArchivedActivityItem();
    }

    @Override
    public APICaller<? extends ArchivedActivityItem> getAPICaller() {
        return new APICaller<ArchivedActivityItem>(this);
    }

    public static final ArchivedActivityDefinition get() {
        return (ArchivedActivityDefinition) Definitions.get(TOKEN);
    }

}
