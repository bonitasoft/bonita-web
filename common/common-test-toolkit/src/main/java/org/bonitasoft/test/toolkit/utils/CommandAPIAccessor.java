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
package org.bonitasoft.test.toolkit.utils;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;

/**
 * @author Vincent Elcrin
 * 
 */
public class CommandAPIAccessor {

    private final APISession session;

    public CommandAPIAccessor(final APISession session) {
        this.session = session;
    }

    public CommandAPI getCommandAPI() {
        try {
            return TenantAPIAccessor.getCommandAPI(session);
        } catch (InvalidSessionException e) {
            throw new TestToolkitException("Session is invalid", e);
        } catch (BonitaHomeNotSetException e) {
            throw new TestToolkitException("Bonita home is not set properly", e);
        } catch (ServerAPIException e) {
            throw new TestToolkitException("Error while communicating with API", e);
        } catch (UnknownAPITypeException e) {
            throw new TestToolkitException("Can't find API", e);
        }
    }
}
