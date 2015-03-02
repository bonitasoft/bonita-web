/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.system;

import java.util.Map;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.system.TenantAdminItem;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.rest.server.engineclient.EngineClientFactory;
import org.bonitasoft.web.rest.server.engineclient.TenantManagementEngineClient;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasUpdate;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Julien Mege
 *
 */
public class TenantAdminDatastore extends Datastore implements DatastoreHasUpdate<TenantAdminItem>, DatastoreHasGet<TenantAdminItem> {

    protected APISession apiSession;

    public TenantAdminDatastore(final APISession apiSession) {
        this.apiSession = apiSession;
    }

    @Override
    public TenantAdminItem update(final APIID unusedId, final Map<String, String> attributes) {
        final TenantAdminItem tenantAdminItem = new TenantAdminItem();
        try {
            final boolean doPause = Boolean.parseBoolean(attributes.get(TenantAdminItem.ATTRIBUTE_IS_PAUSED));
            tenantAdminItem.setId(apiSession.getTenantId());
            if (!doPause) {
                getTenantManagementEngineClient().resumeTenant();
            } else if (doPause) {
                getTenantManagementEngineClient().pauseTenant();
            }
            tenantAdminItem.setIsPaused(doPause);
            return tenantAdminItem;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public TenantAdminItem get(final APIID id) {
        final TenantAdminItem tenantAdminItem = new TenantAdminItem();
        try {
            final boolean tenantPaused = getTenantManagementEngineClient().isTenantPaused();
            tenantAdminItem.setIsPaused(tenantPaused);
            tenantAdminItem.setId(apiSession.getTenantId());
            return tenantAdminItem;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected TenantManagementEngineClient getTenantManagementEngineClient() {
        return new EngineClientFactory(new EngineAPIAccessor(apiSession)).createTenantManagementEngineClient();
    }
}
