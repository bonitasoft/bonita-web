package org.bonitasoft.console.client.javascript.libs;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.TextResource;

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

/**
 * @author Vincent Elcrin
 * 
 */
public interface PLUploadRes extends ResourceClientBunble {

    PLUploadRes INSTANCE = GWT.create(PLUploadRes.class);

    @Source(RESOURCES_PATH + "scripts/ext/plupload.js")
    TextResource plupload();

    @Source(RESOURCES_PATH + "scripts/ext/plupload.gears.js")
    TextResource gears();

    @Source(RESOURCES_PATH + "scripts/ext/plupload.silverlight.js")
    TextResource silverlight();

    @Source(RESOURCES_PATH + "scripts/ext/plupload.flash.js")
    TextResource flash();

    @Source(RESOURCES_PATH + "scripts/ext/plupload.browserplus.js")
    TextResource browserplus();

    @Source(RESOURCES_PATH + "scripts/ext/plupload.html4.js")
    TextResource html4();

    @Source(RESOURCES_PATH + "scripts/ext/plupload.html5.js")
    TextResource html5();

}
