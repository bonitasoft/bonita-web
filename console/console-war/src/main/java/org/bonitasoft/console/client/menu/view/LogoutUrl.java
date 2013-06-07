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
package org.bonitasoft.console.client.menu.view;

import org.bonitasoft.web.toolkit.client.common.UrlBuilder;

/**
 * @author Vincent Elcrin
 * 
 */
public class LogoutUrl {

    public static final String LOGOUT_URL = "../logoutservice";

    private final UrlBuilder builder;

    public LogoutUrl(UrlBuilder builder, String locale) {
        this.builder = builder;
        this.builder.setRootUrl(LOGOUT_URL);
        this.builder.addParameter("_l", locale);
    }

    public void setParameter(String key, String value) {
        builder.addParameter(key, value);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
