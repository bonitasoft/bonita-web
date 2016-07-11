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
package org.bonitasoft.web.toolkit.client.ui.component.table.filters;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.table.TableComponent;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
abstract public class TableFilter extends TableComponent {

    protected String name = null;

    protected String label = null;

    protected String tooltip = null;

    protected String uid = null;

    protected String value = null;

    public TableFilter(final String label, final String tooltip, final String name) {
        this(label, tooltip, name, null);
    }

    public TableFilter(final String label, final String tooltip, final String name, final String value) {
        super();
        this.name = name;
        this.label = label;
        this.tooltip = tooltip;
        this.value = value;
        this.uid = DOM.createUniqueId();
    }

    public abstract String getValue();

    public String getName() {
        return this.name;
    }

    protected Element makeRootElement() {
        // Root tag
        final Element e = DOM.createDiv();
        e.addClassName("tablefilter");
        e.addClassName(new JsId(this.name).toString("tablefilter"));
        e.setAttribute("title", this.tooltip);

        // Label
        e.setInnerHTML(HTML.label(this.label, XML.getUniqueId()));

        return e;
    }
}
