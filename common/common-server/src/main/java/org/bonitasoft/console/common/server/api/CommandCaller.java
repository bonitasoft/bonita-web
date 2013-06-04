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
package org.bonitasoft.console.common.server.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APISessionInvalidException;

/**
 * @author Vincent Elcrin
 * 
 */
public final class CommandCaller {

    private final APISession session;

    private final String command;

    private final Map<String, Serializable> parameters;

    /**
     * Default Constructor.
     */
    public CommandCaller(APISession session, final String command) {
        this.session = session;
        this.command = command;
        parameters = new HashMap<String, Serializable>();
    }

    public CommandCaller addParameter(final String key, final Serializable value) {
        parameters.put(key, value);
        return this;
    }

    public Serializable run() {
        try {
            final CommandAPI commandAPI = TenantAPIAccessor.getCommandAPI(this.session);
            return commandAPI.execute(this.command, this.parameters);
        } catch (InvalidSessionException e) {
            throw new APISessionInvalidException(e);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }
}
