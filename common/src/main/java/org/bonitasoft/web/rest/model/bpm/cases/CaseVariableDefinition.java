/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
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
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.StringMaxLengthValidator;

import static org.bonitasoft.web.rest.model.bpm.cases.CaseVariableItem.ATTRIBUTE_CASE_ID;
import static org.bonitasoft.web.rest.model.bpm.cases.CaseVariableItem.ATTRIBUTE_NAME;

/**
 * @author Colin PUY
 * 
 */
public class CaseVariableDefinition extends ItemDefinition<CaseVariableItem> {

    private static final String API_URL = "../API/bpm/caseVariable";

    public static final String TOKEN = "caseVariable";

    public static final CaseVariableDefinition get() {
        return (CaseVariableDefinition) Definitions.get(TOKEN);
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
        createAttribute(CaseVariableItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(CaseVariableItem.ATTRIBUTE_TYPE, ItemAttribute.TYPE.STRING);
        createAttribute(CaseVariableItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(CaseVariableItem.ATTRIBUTE_VALUE, ItemAttribute.TYPE.STRING)
                .removeValidator(StringMaxLengthValidator.class.getName());
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ATTRIBUTE_CASE_ID, ATTRIBUTE_NAME);
    }

    @Override
    protected CaseVariableItem _createItem() {
        return new CaseVariableItem();
    }

}
