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
package org.bonitasoft.web.rest.server.framework.utils;

import java.io.File;

/**
 * @author Séverin Moussel
 * 
 */
public class FilePathBuilder {

    private final StringBuilder path = new StringBuilder();

    public FilePathBuilder(final String path) {
        super();
        insert(path);
    }

    public FilePathBuilder append(final String path) {
        // If null or empty, do nothing
        if (path == null || path.isEmpty()) {
            return this;
        }

        this.path.append(File.separator);

        insert(path);

        return this;
    }

    /**
     * @param path
     */
    private void insert(final String path) {
        if (path.endsWith(File.separator)) {
            this.path.append(path.substring(0, path.length() - 1));
        } else {
            this.path.append(path);
        }
    }

    @Override
    public String toString() {
        return this.path.toString();
    }

}
