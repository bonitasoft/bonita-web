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
package org.bonitasoft.web.toolkit.client.ui.component.containers;

import static com.google.gwt.query.client.GQuery.$;

import java.util.LinkedList;

import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Components;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;

import com.google.gwt.user.client.Element;

/**
 * A container without root tag and wrappers, just the components contained
 * 
 * @author Séverin Moussel
 */
public class ContainerDummy<T extends AbstractComponent> extends Components {

    protected final LinkedList<T> components = new LinkedList<T>();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ContainerDummy() {
    }

    public ContainerDummy(final T... components) {
        this.append(components);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // MANAGE CONTENT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final ContainerDummy<T> append(final T... components) {
        for (final T component : components) {
            this.components.add(component);
            if (isGenerated()) {
                this.appendItemToHtml(component);
            }
        }
        return this;
    }

    public final ContainerDummy<T> addFirst(final T... components) {
        for (final T component : components) {
            this.components.addFirst(component);

            if (isGenerated()) {
                this.prependItemToHtml(component);
            }
        }
        return this;
    }

    public final LinkedList<T> getComponents() {
        return this.components;
    }

    public final T getLast() {
        return this.components.get(this.components.size() - 1);
    }

    public final int size() {
        return this.components.size();
    }

    public final T get(final int index) {
        return this.components.get(index);
    }

    public final ContainerDummy<T> empty() {
        this.components.clear();
        if (isGenerated()) {
            $(getElements()).remove();
        }
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DISPLAY
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Add an item directly in the HTML before the first existing item
     * @param item
     * The item to prepend
     */
    protected final void prependItemToHtml(final T item) {
        HTML.before(this.elements.get(0), item.getElements());
    }

    /**
     * Add an item directly in the HTML after the last existing item
     * 
     * @param item
     *            The item to append
     */
    protected final void appendItemToHtml(final T item) {
        HTML.after(this.elements.get(0), item.getElements());
    }

    @Override
    protected LinkedList<Element> makeElements() {

        final LinkedList<Element> elements = new LinkedList<Element>();

        for (final T component : this.components) {
            for (final Element element : component.getElements()) {
                elements.add(element);
            }
        }

        return elements;
    }

}
