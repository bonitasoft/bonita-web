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
package org.bonitasoft.web.rest.model.monitoring.report;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.FileIsImageValidator;

/**
 * @author Vincent Elcrin
 * 
 */
public class ReportDefinition extends ItemDefinition<ReportItem> {

    public static final String TOKEN = "report";

    protected static final String API_URL = "../API/monitoring/report";

    public static final ReportDefinition get() {
        return (ReportDefinition) Definitions.get(TOKEN);
    }

    @Override
    protected String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(ReportItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ReportItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(ReportItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(ReportItem.ATTRIBUTE_INSTALLED_ON, ItemAttribute.TYPE.DATE);
        createAttribute(ReportItem.ATTRIBUTE_INSTALLED_BY, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ReportItem.ATTRIBUTE_IS_PROVIDED, ItemAttribute.TYPE.BOOLEAN);
        createAttribute(ReportItem.ATTRIBUTE_ICON, ItemAttribute.TYPE.IMAGE).addValidator(new FileIsImageValidator());
        createAttribute(ReportItem.ATTRIBUTE_LAST_UPDATE_DATE, ItemAttribute.TYPE.DATETIME);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ReportItem.ATTRIBUTE_ID);
    }

    @Override
    protected ReportItem _createItem() {
        return new ReportItem();
    }
}
