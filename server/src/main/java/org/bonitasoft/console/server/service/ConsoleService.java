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
package org.bonitasoft.console.server.service;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.server.Service;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class ConsoleService extends Service {

    private APISession sessionSingleton = null;

    /**
     * Get the session
     */
    protected final APISession getSession() {
        if (this.sessionSingleton == null) {
            this.sessionSingleton = (APISession) getHttpSession().getAttribute("apiSession");
        }
        return this.sessionSingleton;
    }

}
