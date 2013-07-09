/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.portal.profile;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;


/**
 * @author Fabio Lombardi
 *
 */
public class BonitaPageDefinition extends ItemDefinition<BonitaPageItem> {

   
    /**
     * Singleton
     */
    public static final BonitaPageDefinition get() {
        return (BonitaPageDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "bonitaPage";
    
    /**
     * the URL of profile resource
     */
    protected static final String API_URL = "../API/userXP/bonitaPage";
    
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
        createAttribute(BonitaPageItem.ATTRIBUTE_TOKEN, ItemAttribute.TYPE.STRING);
        createAttribute(BonitaPageItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(BonitaPageItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.STRING);
        createAttribute(BonitaPageItem.ATTRIBUTE_MENU_NAME, ItemAttribute.TYPE.STRING);
    }


    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(BonitaPageItem.ATTRIBUTE_TOKEN);
    }

    @Override
    protected BonitaPageItem _createItem() {
        return new BonitaPageItem();
    }

    @Override
    public APICaller<BonitaPageItem> getAPICaller() {
        return new APICaller<BonitaPageItem>(this);
    }

}
