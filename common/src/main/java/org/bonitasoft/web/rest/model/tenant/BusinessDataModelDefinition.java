/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.tenant;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

public class BusinessDataModelDefinition extends ItemDefinition<BusinessDataModelItem> {

    public static final String TOKEN = "bdm";
    private static final String API_URL = "../API/tenant/" + TOKEN;

    public static final BusinessDataModelDefinition get() {
        return (BusinessDataModelDefinition) Definitions.get(TOKEN);
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
        
    }

    @Override
    protected void definePrimaryKeys() {

    }

    @Override
    protected BusinessDataModelItem _createItem() {
        return new BusinessDataModelItem();
    }

}
