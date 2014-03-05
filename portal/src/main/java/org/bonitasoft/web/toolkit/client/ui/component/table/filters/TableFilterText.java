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

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.XML;
import org.bonitasoft.web.toolkit.client.ui.html.XMLAttributes;

import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * @author Séverin Moussel
 * 
 */
public class TableFilterText extends TableFilter {

    public TableFilterText(final String label, final String tooltip, final String name, final String value) {
        super(label, tooltip, name, value);
    }

    public TableFilterText(final String label, final String tooltip, final String name) {
        super(label, tooltip, name);
    }

    @Override
    protected Element makeElement() {
        // Root tag
        final Element rootElement = makeRootElement();
        rootElement.addClassName("tablefiltertext");

        // Input
        final XMLAttributes attributes = new XMLAttributes("id", XML.getLastUniqueId());
        attributes.add("placeholder", this.tooltip);
        $(rootElement).append(HTML.inputText(this.name, this.value, attributes));

        $(":text", rootElement).keydown(new Function() {

            @Override
            public boolean f(final Event e) {
                // Return
                if (e.getKeyCode() == 13) {
                    TableFilterText.this.table.setPage(0);
                    TableFilterText.this.table.refresh();
                    return false;
                }
                return true;
            }
        });

        return rootElement;
    }

    @Override
    public String getValue() {

        return $(":text:not(.empty)", getElement()).val();
    }
}
