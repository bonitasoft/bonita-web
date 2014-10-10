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

import java.util.Date;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Fabio Lombardi
 *
 */
public class ArchivedCaseDocumentItem extends CaseDocumentItem {

    public static final String ATTRIBUTE_SOURCE_OBJECT_ID = "sourceObjectId";

    public static final String ATTRIBUTE_ARCHIVED_DATE = "archivedDate";

    public static final String FILTER_ARCHIVED_CASE_ID = "archivedCaseId";


    public ArchivedCaseDocumentItem() {
        super();
    }

    public ArchivedCaseDocumentItem(final IItem item) {
        super(item);
    }


    // SETTER

    public void setArchivedDate(final Date date) {
        setAttribute(ATTRIBUTE_ARCHIVED_DATE, date);
    }

    public void setArchivedDate(final String date) {
        setAttribute(ATTRIBUTE_ARCHIVED_DATE, date);
    }

    public void setSourceObjectId(final Long id) {
        setAttribute(ATTRIBUTE_SOURCE_OBJECT_ID, id);
    }

    public void setSourceObjectId(final APIID id) {
        setAttribute(ATTRIBUTE_SOURCE_OBJECT_ID, id);
    }

    public void setSourceObjectId(final String id) {
        setAttribute(ATTRIBUTE_SOURCE_OBJECT_ID, id);
    }

    // GETTER

    @Override
    public ItemDefinition getItemDefinition() {
        return new ArchivedCaseDefinition();
    }

    public APIID getSourceObjectId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_SOURCE_OBJECT_ID);
    }

    public Date getArchivedDate() {
        return getAttributeValueAsDate(ATTRIBUTE_ARCHIVED_DATE);
    }

}
