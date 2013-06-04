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
package org.bonitasoft.web.toolkit.client.common.url;

/**
 * @author Vincent Elcrin
 * 
 *         Define constants used as options in URL
 */
public class UrlOption {

    /**
     * Token of the page to show.
     */
    public static final String PAGE = "_p";

    /**
     * Language in which the application must be shown.
     */
    public static final String LANG = "_l";

    /**
     * User profile in which the user is navigating.
     */
    public static final String PROFILE = "_pf";

    /**
     * Filter to select (and show) on a item listing page
     */
    public static final String FILTER = "_f";

    /**
     * Resource filter to select (and show) on a item listing page
     */
    public static final String RESOURCE_FILTER = "_fid";

}
