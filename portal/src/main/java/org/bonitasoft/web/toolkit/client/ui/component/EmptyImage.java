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
package org.bonitasoft.web.toolkit.client.ui.component;

import org.bonitasoft.web.toolkit.client.ui.JsId;


/**
 * @author Colin PUY
 *
 */
public class EmptyImage extends Image {

    public static final String PATH = "icons/default/transparent.gif";
    
    public EmptyImage(final int width, final int height) {
        this(width, height, "");
        addClass("empty");
    }

    public EmptyImage(final JsId jsid, final int width, final int height, final String tooltip) {
        super(jsid, PATH, width, height, tooltip);
        addClass("empty");
    }

    public EmptyImage(final int width, final int height, final String tooltip) {
        super(PATH, width, height, tooltip);
        addClass("empty");
    }
}
