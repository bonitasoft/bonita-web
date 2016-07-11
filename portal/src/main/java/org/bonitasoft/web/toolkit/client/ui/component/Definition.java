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
package org.bonitasoft.web.toolkit.client.ui.component;

import java.util.ArrayList;

import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.HTMLClass;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class Definition extends Component {

    private final String label;

    private final String definition;

    private AbstractComponent[] components = null;

    private String tooltip = null;

    public Definition(final String label, final String definition) {
        this(label, (String) null, definition, (AbstractComponent) null);
    }

    public Definition(final String label, final AbstractComponent... components) {
        this(label, (String) null, "", components);
        this.components = components;
    }

    public Definition(final String label, final String definition, final AbstractComponent... components) {
        this(label, (String) null, definition, components);
        this.components = components;
    }

    public Definition(final String label, final String tooltip, final String definition) {
        this(label, tooltip, definition, (AbstractComponent) null);
    }

    public Definition(final String label, final String tooltip, final String definition, final AbstractComponent... components) {
        this.components = components;
        this.label = label;
        this.definition = definition;
        this.tooltip = tooltip;
    }

    @Override
    protected Element makeElement() {
        this.element = XML.makeElement(HTML.div(new HTMLClass("definition")));

        final java.util.List<AbstractComponent> labelComponents = new ArrayList<AbstractComponent>();
        final java.util.List<AbstractComponent> definitionComponents = new ArrayList<AbstractComponent>();

        if (this.components != null) {
            final int countComponentsInLabel = this.label.split("%%").length - 1;
            for (int i = 0; i < this.components.length; i++) {
                if (i < countComponentsInLabel) {
                    labelComponents.add(this.components[i]);
                } else {
                    definitionComponents.add(this.components[i]);
                }
            }
        }

        final Text label = new Text(this.label, labelComponents.size() > 0 ? labelComponents.toArray(new AbstractComponent[0]) : (AbstractComponent[]) null)
                .setRootTagName("label");
        if (this.tooltip != null) {
            label.setTooltip(this.tooltip);
        }

        final Text definition = new Text(this.definition, definitionComponents.size() > 0 ? definitionComponents.toArray(new AbstractComponent[0])
                : (AbstractComponent[]) null);

        appendComponentToHtml(this.element, label);
        appendComponentToHtml(this.element, definition);

        return this.element;
    }

}
