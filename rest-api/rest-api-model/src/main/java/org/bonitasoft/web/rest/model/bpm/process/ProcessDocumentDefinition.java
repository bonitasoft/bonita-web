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
package org.bonitasoft.web.rest.model.bpm.process;

import org.bonitasoft.web.rest.model.bpm.AbstractDocumentDefinition;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Paul AMAR
 * 
 */
public class ProcessDocumentDefinition extends AbstractDocumentDefinition {

    public static final String TOKEN = "process";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/document/process";

    @Override
    protected void definePrimaryKeys() {
    }

    @Override
    protected IItem _createItem() {
        return new ProcessDocumentItem();
    }

    @Override
    public APICaller<ProcessDocumentItem> getAPICaller() {
        return new APICaller<ProcessDocumentItem>(this);
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
        // TODO Auto-generated method stub

    }

}
