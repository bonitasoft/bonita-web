/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.api.model.bpm.cases;

import static org.bonitasoft.web.rest.api.model.bpm.cases.CaseVariableItem.ATTRIBUTE_CASE_ID;
import static org.bonitasoft.web.rest.api.model.bpm.cases.CaseVariableItem.ATTRIBUTE_NAME;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.StringMaxLengthValidator;

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

    @Override
    public APICaller<CaseVariableItem> getAPICaller() {
        return new APICaller<CaseVariableItem>(this);
    }
}
