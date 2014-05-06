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

import java.io.Serializable;
import java.util.Map;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;

public class CommandCaller {

    private final CommandAPI commandAPI;

    public CommandCaller(CommandAPI commandAPI) {
        this.commandAPI = commandAPI;
    }

    /**
     * @param commandDelete
     * @param singletonMap
     * @return
     */
    public Serializable run(String command, Map<String, Serializable> parameters) {
        try {
            return commandAPI.execute(command, parameters);
        } catch (final InvalidSessionException e) {
            throw new TestToolkitException("Invalid session", e);
        } catch (final Exception e) {
            throw new TestToolkitException("Unkown exception while executing command <" + command + ">", e);
        }
    }
}
