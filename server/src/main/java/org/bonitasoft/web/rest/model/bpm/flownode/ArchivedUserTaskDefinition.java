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
package org.bonitasoft.web.rest.model.bpm.flownode;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Séverin Moussel
 * 
 */
public class ArchivedUserTaskDefinition extends UserTaskDefinition {

    public static final String TOKEN = "archivedusertask";

    private static final String API_URL = "../API/bpm/archivedUserTask";

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
        createAttribute(ArchivedUserTaskItem.ATTRIBUTE_ARCHIVED_DATE, ItemAttribute.TYPE.DATETIME);
    }

    @Override
    public ArchivedUserTaskItem _createItem() {
        return new ArchivedUserTaskItem();
    }

    public static final ArchivedUserTaskDefinition get() {
        return (ArchivedUserTaskDefinition) Definitions.get(TOKEN);
    }

}
