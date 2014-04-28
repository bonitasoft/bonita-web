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
package org.bonitasoft.console.common.server.login.localization;

/**
 * @author Vincent Elcrin
 * 
 */
public class UrlValue {

    private String value;

    public UrlValue(String value) {
        this.value = value;
    }

    public UrlValue(String[] values) {
        this(merge(values));
    }

    private static String merge(String[] values) {
        final StringBuilder value = new StringBuilder();
        for (final String parameterValue : values) {
            if (value.length() > 0) {
                value.append(",");
            }
            value.append(parameterValue);
        }
        return value.toString();
    }

    @Override
    public String toString() {
        return value;
    }

}
