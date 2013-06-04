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
package org.bonitasoft.console.client.model.monitoring.report;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute.TYPE;

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
        createAttribute(ReportItem.ATTRIBUTE_ID, TYPE.ITEM_ID);
        createAttribute(ReportItem.ATTRIBUTE_NAME, TYPE.STRING);
        createAttribute(ReportItem.ATTRIBUTE_DESCRIPTION, TYPE.TEXT);
        createAttribute(ReportItem.ATTRIBUTE_INSTALLED_ON, TYPE.DATE);
        createAttribute(ReportItem.ATTRIBUTE_INSTALLED_BY, TYPE.ITEM_ID);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ReportItem.ATTRIBUTE_ID);
    }

    @Override
    protected ReportItem _createItem() {
        return new ReportItem();
    }

    @Override
    public APICaller<ReportItem> getAPICaller() {
        return new APICaller<ReportItem>(this);
    }

}
