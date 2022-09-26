/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.model.user;

import java.io.Serializable;

/**
 * @author Guo Yongtao
 *
 */
public class User implements Serializable {

    /**
     * ID used for serialization.
     */
    private static final long serialVersionUID = 1940844173066923676L;

    /**
     * The username
     */
    protected String username;

    /**
     * Indicates the locale to use to display the user interface
     */
    protected String locale;

    public User() {
        super();
        // Mandatory for serialization.
    }

    public User(final String username, final String locale) {
        this.username = username;
        this.locale = locale;
    }

    /**
     * @return the userUUID
     */
    public String getUsername() {
        return username;
    }

    public String getLocale() {
        return locale;
    }

}
