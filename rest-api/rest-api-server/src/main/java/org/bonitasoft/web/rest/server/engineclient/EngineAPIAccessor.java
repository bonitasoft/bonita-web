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

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.GroupAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
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

    public ProfileAPI getProfileAPI(APISession session) {
        try {
            return TenantAPIAccessor.getProfileAPI(session);
        } catch (InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (BonitaHomeNotSetException e) {
            throw new APIException(e);
        } catch (ServerAPIException e) {
            throw new APIException(e);
        } catch (UnknownAPITypeException e) {
            throw new APIException(e);
        }
    }
    
    public ProcessAPI getProcessAPI(APISession session) {
        try {
            return TenantAPIAccessor.getProcessAPI(session);
        } catch (Exception e) {
            throw new APIException("Error when getting engine process API", e);
        }
    }
    
    public IdentityAPI getIdentityAPI(APISession session) {
        try {
            return TenantAPIAccessor.getIdentityAPI(session);
        } catch (Exception e) {
            throw new APIException("Error when getting engine identity API", e);
        }
    }

   public GroupAPI getGroupAPI(APISession session) {
        try {
            return TenantAPIAccessor.getIdentityAPI(session);
        } catch (Exception e) {
            throw new APIException("Error when getting engine group API", e);
        }
    }
}
