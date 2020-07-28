/**
 * Copyright (C) 2020 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.api.extension;

/**
 * This class has the responsability to store a REST API extension controller class name, and if this REST API is used in
 * compiled mode or in source mode.
 * - In compiled mode, name is supposed to be the qualified name of a Java class present in the Jar.
 * - In source mode, name is supposed to be the path to the main Groovy source file
 */
public class ControllerClassName {

    private String name;
    private boolean source;

    public ControllerClassName(String name, boolean source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public boolean isSource() {
        return source;
    }

}
