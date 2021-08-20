/*******************************************************************************
 * Copyright (C) 2009, 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.engineclient;

import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.i18n.T_;

/**
 * @author Colin PUY
 */
public class TenantManagementEngineClient {

    private final TenantAdministrationAPI tenantAdministrationAPI;

    public TenantManagementEngineClient(final TenantAdministrationAPI tenantManagementAPI) {
        this.tenantAdministrationAPI = tenantManagementAPI;
    }

    public boolean isTenantPaused() {
        return tenantAdministrationAPI.isPaused();
    }

    public void pauseTenant() {
        if (!isTenantPaused()) {
            pause();
        }
    }

    private void pause() {
        try {
            tenantAdministrationAPI.pause();
        } catch (final UpdateException e) {
            throw new APIException(new T_("Error when pausing BPM services"), e);
        }
    }

    public void resumeTenant() {
        if (isTenantPaused()) {
            resume();
        }
    }

    private void resume() {
        try {
            tenantAdministrationAPI.resume();
        } catch (final UpdateException e) {
            throw new APIException(new T_("Error when resuming BPM services"), e);
        }
    }
}
