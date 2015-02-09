/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.system;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Julien Reboul
 * 
 */
public class TenantAdminDefinition extends ItemDefinition<TenantAdminItem> {

    public static final String TOKEN = "tenantAdmin";

    public static final String UNUSED_ID = "1";

    protected static final String API_URL = "../API/system/tenant";

    public static final TenantAdminDefinition get() {
        return (TenantAdminDefinition) Definitions.get(TOKEN);
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
        createAttribute(TenantAdminItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(TenantAdminItem.ATTRIBUTE_IS_PAUSED, ItemAttribute.TYPE.BOOLEAN);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(TenantAdminItem.ATTRIBUTE_ID);
    }

    @Override
    protected TenantAdminItem _createItem() {
        return new TenantAdminItem();
    }
}
