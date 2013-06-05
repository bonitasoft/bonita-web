/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component.core;

import static com.google.gwt.query.client.GQuery.$;

import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.JsId;

import com.google.gwt.user.client.Element;

/**
 * A class representing a component made of multiple root DOM elements.
 * 
 * @author Séverin Moussel
 */
public abstract class Components extends AbstractComponent {

    /**
     * List of root elements of this component.
     */
    protected final LinkedList<Element> elements = new LinkedList<Element>();

    /**
     * Generate the root DOM elements.
     * 
     * @return This method returns the root DOM elements.
     */
    @Override
    protected final List<Element> _getElements() {
        if (!this.generated) {
            preProcessHtml();
            this.elements.addAll(makeElements());
            postProcessHtml();
            this.generated = true;
        }
        return this.elements;
    }

    /**
     * Return null as JsId.
     * 
     * @return This method returns null.
     */
    @Override
    public final JsId getJsId() {
        return null;
    }

    /**
     * Replace the elements of the component by new elements.
     * 
     * @param newElements
     *            The new elements to put.
     */
    protected final void replace(final List<Element> newElements) {
        if (isGenerated()) {
            $(this.elements.get(0)).before($(newElements));
            $(this.elements).remove();
        }

        this.elements.clear();
        this.elements.addAll(newElements);
    }

    /**
     * Reset the node generation.
     */
    @Override
    public final void resetGeneration() {
        this.generated = false;
        this.elements.clear();
    }

    /**
     * Generate the root DOM elements.
     * 
     * @return This method returns the root DOM elements.
     */
    protected abstract List<Element> makeElements();

}
