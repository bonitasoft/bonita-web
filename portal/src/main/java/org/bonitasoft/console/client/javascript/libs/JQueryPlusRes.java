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
package org.bonitasoft.console.client.javascript.libs;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Vincent Elcrin
 * 
 */
public interface JQueryPlusRes extends ResourceClientBunble {

    JQueryPlusRes INSTANCE = GWT.create(JQueryPlusRes.class);

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/extend.js")
    TextResource extend();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/debug.js")
    TextResource debug();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/vartypes.js")
    TextResource vartypes();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/compatibility.js")
    TextResource compatibility();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/dom.js")
    TextResource dom();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/options.js")
    TextResource options();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/css.js")
    TextResource css();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/uiManager.js")
    TextResource uiManager();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/togglePanel.js")
    TextResource togglePanel();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/enable.js")
    TextResource enable();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/selectors.js")
    TextResource selectors();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/color.js")
    TextResource color();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/events.js")
    TextResource events();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/manipulation.js")
    TextResource manipulation();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/checkbox.js")
    TextResource checkbox();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/tabs.js")
    TextResource tabs();

    @Source(RESOURCES_PATH + "scripts/jquery/jqueryplus/loading.js")
    TextResource loading();
}
