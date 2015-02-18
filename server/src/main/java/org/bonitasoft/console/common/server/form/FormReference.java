/**
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.form;

import java.io.Serializable;


public class FormReference implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -1692145871057019847L;

    private String reference;

    private boolean external;

    protected FormReference(final String reference, final boolean external) {
        this.reference = reference;
        this.external = external;
    }


    public String getReference() {
        return reference;
    }


    public void setReference(final String reference) {
        this.reference = reference;
    }


    public boolean isExternal() {
        return external;
    }


    public void setExternal(final boolean external) {
        this.external = external;
    }



}
