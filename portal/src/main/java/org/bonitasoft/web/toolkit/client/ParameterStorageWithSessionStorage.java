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
package org.bonitasoft.web.toolkit.client;

import com.google.gwt.storage.client.Storage;


public class ParameterStorageWithSessionStorage {

    private static Storage sessionStorage = Storage.getSessionStorageIfSupported();

    public static String getParameter(final String name) {
        if (sessionStorage != null) {
            return sessionStorage.getItem(name);
        } else {
            return null;
        }
    }

    public static void setParameter(final String name, final String value) {
        if (sessionStorage != null) {
            sessionStorage.setItem(name, value);
        }
    }

    public static void removeParameter(final String name) {
        if (sessionStorage != null) {
            sessionStorage.removeItem(name);
        }
    }
}
