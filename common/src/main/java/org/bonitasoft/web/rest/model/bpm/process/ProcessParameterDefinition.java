/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.bpm.process;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.StringMaxLengthValidator;

/**
 * @author Yongtao Guo, Haojie Yuan, Anthony Birembaut
 * 
 */
public class ProcessParameterDefinition extends ItemDefinition<ProcessParameterItem> {

    /**
     * Singleton
     */
    public static final ProcessParameterDefinition get() {
        return (ProcessParameterDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "processParameter";

    /**
     * the URL of users resource
     */
    private static final String API_URL = "../API/bpm/processParameter";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ProcessParameterItem.ATTRIBUTE_PROCESS_ID, ProcessParameterItem.ATTRIBUTE_NAME);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    /**
     * categoryList
     */
    @Override
    protected void defineAttributes() {

        createAttribute(ProcessParameterItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(ProcessParameterItem.ATTRIBUTE_TYPE, ItemAttribute.TYPE.STRING);
        createAttribute(ProcessParameterItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(ProcessParameterItem.ATTRIBUTE_VALUE, ItemAttribute.TYPE.STRING)
            .removeValidator(StringMaxLengthValidator.class.getName());

    }

    @Override
    public ProcessParameterItem _createItem() {
        return new ProcessParameterItem();
    }
}
