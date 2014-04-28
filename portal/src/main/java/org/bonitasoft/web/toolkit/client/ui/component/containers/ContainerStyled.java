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

import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.core.Node;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class ContainerStyled<T extends Node> extends Container<T> {

    public ContainerStyled() {
        super();
    }

    public ContainerStyled(final JsId jsid, final T... components) {
        super(jsid, components);
    }

    public ContainerStyled(final JsId jsid) {
        super(jsid);
    }

    public ContainerStyled(final T... components) {
        super(components);
    }

    private String makeClasses(final String rootClass, final int index, final int lastIndex) {
        return rootClass + "_" + String.valueOf(index + 1)
                + (index == lastIndex ? " " + rootClass + "_last" : "")
                + (index % 2 == 0 ? " odd" : " even");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Element makeElement() {

        // Root element
        this.element = DOM.createElement(this.rootTagName);
        if (this.rootTagClass != null) {
            this.element.addClassName(this.rootTagClass);
        }
        if (getJsId() != null) {
            this.element.addClassName(getJsId().toString());
        }

        // Styling and adding children
        final int size = this.components.size();
        int counter = 0;
        for (final T component : this.components) {

            final List<Element> childElements = component.getElements();

            // Set style on children if also styled (and avoid mapper)
            if (component instanceof ContainerStyled) {
                childElements.get(0).addClassName(this.makeClasses(((ContainerStyled<Node>) component).getRootTagClass(), counter, size - 1));

                // Adding this to DOM
                HTML.append(this.element, childElements);
            }

            // If we use a wrapper
            else if (this.wrapTagName != null) {
                final Element wrapper = DOM.createElement(this.wrapTagName);

                if (this.wrapTagClass != null) {
                    wrapper.addClassName(this.wrapTagClass);
                    wrapper.addClassName(this.makeClasses(this.wrapTagClass, counter, size - 1));
                } else {
                    wrapper.addClassName(this.makeClasses("", counter, size - 1));
                }
                HTML.append(wrapper, childElements);

                // Adding this to DOM
                this.element.appendChild(wrapper);

            }

            // If we don't use a wrapper
            else {
                // If the component is a singleElementComponent, we set the style on the component
                if (component instanceof Component) {
                    if (this.wrapTagClass != null) {
                        childElements.get(0).addClassName(this.wrapTagClass);
                        childElements.get(0).addClassName(this.makeClasses(this.wrapTagClass, counter, size - 1));
                    } else {
                        childElements.get(0).addClassName(this.makeClasses("", counter, size - 1));
                    }
                }

                // Adding this to DOM
                HTML.append(this.element, childElements);
            }

            counter++;
        }

        return this.element;
    }

    @Override
    public Container<T> append(final T... components) {
        return super.append(components);
    }

    @Override
    public Container<T> prepend(final T... components) {
        return super.prepend(components);
    }

}
