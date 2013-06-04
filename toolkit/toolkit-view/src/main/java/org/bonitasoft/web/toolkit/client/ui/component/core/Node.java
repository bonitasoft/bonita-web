/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component.core;

import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.JsId;

import com.google.gwt.user.client.Element;

/**
 * Interface that all the single DOM node have to implements.<br>
 * 
 * @author Séverin Moussel
 */
public interface Node {

    /**
     * Retrieve an identifier (not unique) to tell what the node is for.
     * 
     * @return This method must return the JsId of the node
     */
    JsId getJsId();

    /**
     * Retrieve the root DOM elements of this node.<br />
     * 
     * @return This method returns the root DOM elements of this node
     */
    List<Element> getElements();

    /**
     * Reset the node generation.
     */
    void resetGeneration();
}
