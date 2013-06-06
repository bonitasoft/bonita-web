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
public interface PortalRes extends ResourceClientBunble {

    PortalRes INSTANCE = GWT.create(PortalRes.class);

    @Source(RESOURCES_PATH + "scripts/includes/common.js")
    TextResource common();

    @Source(RESOURCES_PATH + "scripts/includes/popup.js")
    TextResource popup();

    @Source(RESOURCES_PATH + "scripts/includes/dashboard.js")
    TextResource dashboard();

    @Source(RESOURCES_PATH + "scripts/includes/form.js")
    TextResource form();

    @Source(RESOURCES_PATH + "scripts/includes/skin.js")
    TextResource skin();

    @Source(RESOURCES_PATH + "scripts/includes/table.js")
    TextResource table();

    @Source(RESOURCES_PATH + "scripts/includes/menu.js")
    TextResource menu();

    @Source(RESOURCES_PATH + "scripts/includes/switchPanel.js")
    TextResource switchPanel();

    @Source(RESOURCES_PATH + "scripts/includes/wizard.js")
    TextResource wizard();

    @Source(RESOURCES_PATH + "scripts/includes/socialbar.js")
    TextResource socialbar();

    @Source(RESOURCES_PATH + "scripts/includes/gwt.js")
    TextResource gwt();

    @Source(RESOURCES_PATH + "scripts/includes/datepicker.js")
    TextResource datepicker();

    @Source(RESOURCES_PATH + "scripts/includes/autouploader.js")
    TextResource autoUploader();

    @Source(RESOURCES_PATH + "scripts/includes/array.prototype.js")
    TextResource arrayPrototype();

}
