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
package org.bonitasoft.web.toolkit.client.ui.component.table;

import static com.google.gwt.query.client.GQuery.$;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.XML;
import org.bonitasoft.web.toolkit.client.ui.html.XMLAttributes;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * @author Séverin Moussel
 * 
 */
public class TableFilterSelect extends TableFilter {

    private GQuery selectElement = null;

    private final LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();

    public TableFilterSelect(final Table table, final String label, final String tooltip, final String name) {
        super(table, label, tooltip, name, null);
    }

    public TableFilterSelect(final Table table, final String label, final String tooltip, final String name, final String value) {
        super(table, label, tooltip, name, value);
    }

    public TableFilterSelect addOption(final String name, final String value, final boolean selected) {
        if (isGenerated()) {
            makeOptionHtml(name, value, selected);
        } else {
            this.options.put(name, value);
            if (selected) {
                this.value = value;
            }
        }
        return this;
    }

    private void makeOptionHtml(final String name, final String value, final boolean selected) {
        this.selectElement.append(HTML.option(name, value, selected));
    }

    @Override
    protected Element makeElement() {
        // Root tag
        final Element rootElement = makeRootElement();
        rootElement.addClassName("tablefilterselect");

        // Input
        this.selectElement = $(HTML.select(this.name, new XMLAttributes("id", XML.getLastUniqueId())));

        for (final Entry<String, String> entry : this.options.entrySet()) {
            makeOptionHtml(entry.getValue(), entry.getValue(), entry.getValue().equals(this.value));
        }

        this.selectElement.change(new Function() {

            @Override
            public boolean f(final Event e) {
                TableFilterSelect.this.table.setPage(0);
                TableFilterSelect.this.table.refresh();
                return true;
            }

        });

        rootElement.appendChild(this.selectElement.get(0));

        return rootElement;
    }

    @Override
    public String getValue() {
        return this.selectElement.val();
    }
}
