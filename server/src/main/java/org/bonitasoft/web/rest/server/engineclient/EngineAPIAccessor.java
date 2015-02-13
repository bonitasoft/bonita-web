/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.engineclient;

import org.bonitasoft.engine.api.GroupAPI;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APISessionInvalidException;

/**
 * @author Vincent Elcrin
 *
 */
public class EngineAPIAccessor {

    private final APISession session;

    public EngineAPIAccessor(final APISession session) {
        this.session = session;
    }

    public APISession getSession() {
        return session;
    }

    public ProfileAPI getProfileAPI() {
        try {
            return TenantAPIAccessor.getProfileAPI(getSession());
        } catch (final InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (final BonitaHomeNotSetException e) {
            throw new APIException(e);
        } catch (final ServerAPIException e) {
            throw new APIException(e);
        } catch (final UnknownAPITypeException e) {
            throw new APIException(e);
        }
    }

    public ProcessAPI getProcessAPI() {
        try {
            return TenantAPIAccessor.getProcessAPI(getSession());
        } catch (final Exception e) {
            throw new APIException("Error when getting engine process API", e);
        }
    }

    public IdentityAPI getIdentityAPI() {
        try {
            return TenantAPIAccessor.getIdentityAPI(getSession());
        } catch (final Exception e) {
            throw new APIException("Error when getting engine identity API", e);
        }
    }

    public GroupAPI getGroupAPI() {
        try {
            return TenantAPIAccessor.getIdentityAPI(getSession());
        } catch (final Exception e) {
            throw new APIException("Error when getting engine group API", e);
        }
    }

    public PageAPI getPageAPI() {
        try {
            return TenantAPIAccessor.getCustomPageAPI(getSession());
        } catch (final Exception e) {
            throw new APIException("Error when getting engine page API", e);
        }
    }

    public TenantAdministrationAPI getTenantAdministrationAPI() {
        try {
            return TenantAPIAccessor.getTenantAdministrationAPI(getSession());
        } catch (final Exception e) {
            throw new APIException("Error when getting engine tenant management API", e);
        }
    }
}
