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
package org.bonitasoft.console.common.server.login.credentials;



/**
 * @author Julien Reboul
 *
 */
public class StandardCredentials implements Credentials {

    private String name;

    private String password;

    public StandardCredentials(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    /**
     * @see org.bonitasoft.console.common.server.login.credentials.Credentials#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @see org.bonitasoft.console.common.server.login.credentials.Credentials#getPassword()
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}
