/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.client.mvp;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * @author Vincent Elcrin
 */
public class EmptyImageResource implements ImageResource {

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getLeft() {
        return 0;
    }

    @Override
    public SafeUri getSafeUri() {
        return null;
    }

    @Override
    public int getTop() {
        return 0;
    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

}
