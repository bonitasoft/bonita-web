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

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * Document item
 * 
 * @author Gai Cuisha
 * 
 */
public class ArchivedDocumentItem extends DocumentItem {

    public static final String SOURCEOBJECT_ID = "sourceObjectId";

    public static final String ARCHIVED_DATE = "archivedDate";

    public ArchivedDocumentItem() {
        super();
    }

    public ArchivedDocumentItem(final IItem item) {
        super(item);
    }

    public void setDocumentSourceObjectId(final String sourceObjectId) {
        this.setAttribute(SOURCEOBJECT_ID, sourceObjectId);
    }

    public void setArchivedDate(final String archivedDate) {
        this.setAttribute(ARCHIVED_DATE, archivedDate);
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new ArchivedDocumentDefinition();
    }
}
