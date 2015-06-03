/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.auth.impl.jaas;

import java.io.Serializable;
import java.security.Principal;

/**
 * Subject of principal
 * 
 * @author Qixiang Zhang
 * 
 */
public class ConsolePrincipal implements Principal, Serializable {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1120874595111127685L;

    /**
     * Subject identities
     */
    private final String id;

    /**
     * 
     * Default Constructor.
     * 
     * @param id
     *            Subject identities
     */
    public ConsolePrincipal(final String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * @see java.security.Principal#getName()
     */
    @Override
    public String getName() {
        return this.id;
    }

}
