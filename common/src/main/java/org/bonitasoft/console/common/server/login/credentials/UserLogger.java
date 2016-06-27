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
package org.bonitasoft.console.common.server.login.credentials;

import java.io.Serializable;
import java.util.Map;

import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Vincent Elcrin
 * 
 */
public class UserLogger {

    /**
     * Overridden in SP
     */
    public APISession doLogin(Credentials credentials) throws LoginFailedException {
        try {
            return getDatastore().login(credentials.getName(),
                    credentials.getPassword());
        } catch (final BonitaException e) {
            throw new LoginFailedException(e.getMessage(), e);
        }
    }

    /**
     * Overridden in SP
     */
    public APISession doLogin(Map<String, Serializable> credentials) throws LoginFailedException {
        try {
            return getDatastore().login(credentials);
        } catch (final BonitaException e) {
            throw new LoginFailedException(e.getMessage(), e);
        }
    }

    private LoginDatastore getDatastore() {
        return new LoginDatastore();
    }

}
