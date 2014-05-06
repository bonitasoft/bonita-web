/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.bpm.cases;

import java.io.Serializable;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Colin PUY
 * 
 */
public class CaseVariableItem extends Item {

    public static final String ATTRIBUTE_CASE_ID = "case_id";

    public static final String ATTRIBUTE_NAME = "name";

    public static final String ATTRIBUTE_TYPE = "type";

    public static final String ATTRIBUTE_VALUE = "value";

    public static final String ATTRIBUTE_DESCRIPTION = "description";

    public CaseVariableItem() {
    }

    public CaseVariableItem(long caseId, String name, Serializable value, String type, String description) {
        setAttribute(ATTRIBUTE_CASE_ID, String.valueOf(caseId));
        setAttribute(ATTRIBUTE_NAME, name);
        setAttribute(ATTRIBUTE_VALUE, String.valueOf(value));
        setAttribute(ATTRIBUTE_TYPE, type);
        setAttribute(ATTRIBUTE_DESCRIPTION, description);
    }

    public static CaseVariableItem fromIdAndAttributes(APIID apiid, Map<String, String> attributes) {
        return new CaseVariableItem(apiid.getPartAsLong(ATTRIBUTE_CASE_ID),
                apiid.getPart(ATTRIBUTE_NAME), attributes.get(ATTRIBUTE_VALUE),
                attributes.get(ATTRIBUTE_TYPE), attributes.get(ATTRIBUTE_DESCRIPTION));
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return CaseVariableDefinition.get();
    }

    public long getCaseId() {
        return getAttributeValueAsLong(ATTRIBUTE_CASE_ID);
    }

    public String getName() {
        return getAttributeValue(ATTRIBUTE_NAME);
    }

    public String getType() {
        return getAttributeValue(ATTRIBUTE_TYPE);
    }

    public String getValue() {
        return getAttributeValue(ATTRIBUTE_VALUE);
    }

    public String getDescription() {
        return getAttributeValue(ATTRIBUTE_DESCRIPTION);
    }

    public void setValue(String value) {
        setAttribute(ATTRIBUTE_VALUE, value);
    }

}
