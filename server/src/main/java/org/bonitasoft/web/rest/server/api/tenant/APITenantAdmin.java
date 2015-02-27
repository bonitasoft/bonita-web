/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.tenant;

import java.util.Map;

import org.bonitasoft.web.rest.model.system.TenantAdminDefinition;
import org.bonitasoft.web.rest.model.system.TenantAdminItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.system.TenantAdminDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasUpdate;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Julien Reboul
 *
 */
public class APITenantAdmin extends ConsoleAPI<TenantAdminItem> implements APIHasGet<TenantAdminItem>, APIHasUpdate<TenantAdminItem> {

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(TenantAdminDefinition.TOKEN);
    }

    private TenantAdminDatastore getTenantAdminDatastore() {
        return new TenantAdminDatastore(getEngineSession());
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }

    /**
     *
     * update the Tenant State to set it to maintenance mode or set it up. <br/>
     * <br/>
     *
     * This doesn't have any effect when if state doesn't have to be changed
     *
     * @see org.bonitasoft.web.rest.server.framework.API#update(org.bonitasoft.web.toolkit.client.data.APIID, java.util.Map)
     */
    @Override
    public TenantAdminItem update(final APIID id, final Map<String, String> attributes) {
        return getTenantAdminDatastore().update(id, attributes);
    }

    /**
     * get the current Tenant State
     *
     * @see org.bonitasoft.web.rest.server.framework.API#get(org.bonitasoft.web.toolkit.client.data.APIID)
     */
    @Override
    public TenantAdminItem get(final APIID id) {
        return getTenantAdminDatastore().get(id);
    }

}
