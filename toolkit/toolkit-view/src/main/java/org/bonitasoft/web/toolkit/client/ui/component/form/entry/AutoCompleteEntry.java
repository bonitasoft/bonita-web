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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.AutoCompleteTextInput;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class AutoCompleteEntry extends FormEntry {

    private AutoCompleteTextInput inputComponent = null;

    public AutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition definition, final String labelAttributeName,
            final String valueAttributeName, final String defaultValue, final String description, final String example) {
        this(jsid, label, tooltip, definition, new AttributeReader(labelAttributeName), valueAttributeName, defaultValue, description, example);
    }

    public AutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition definition, final String labelAttributeName,
            final String valueAttributeName, final String defaultValue, final String description) {
        this(jsid, label, tooltip, definition, labelAttributeName, valueAttributeName, defaultValue, description, null);
    }

    public AutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition definition, final String labelAttributeName,
            final String valueAttributeName, final String defaultValue) {
        this(jsid, label, tooltip, definition, labelAttributeName, valueAttributeName, defaultValue, null, null);
    }

    public AutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition definition, final String labelAttributeName,
            final String valueAttributeName) {
        this(jsid, label, tooltip, definition, labelAttributeName, valueAttributeName, null, null, null);
    }

    public AutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition definition,
            final AbstractAttributeReader labelTemplate,
            final String valueAttributeName, final String defaultValue, final String description, final String example) {
        super(jsid, label, tooltip, defaultValue, description, example);

        this.inputComponent = new AutoCompleteTextInput(jsid, definition, labelTemplate, valueAttributeName);

        if (this.defaultValue != null) {
            this.inputComponent.setValue(this.defaultValue);
        }

        addClass("autocomplete");
    }

    public AutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition definition,
            final AbstractAttributeReader labelTemplate,
            final String valueAttributeName, final String defaultValue, final String description) {
        this(jsid, label, tooltip, definition, labelTemplate, valueAttributeName, defaultValue, description, null);
    }

    public AutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition definition,
            final AbstractAttributeReader labelTemplate,
            final String valueAttributeName, final String defaultValue) {
        this(jsid, label, tooltip, definition, labelTemplate, valueAttributeName, defaultValue, null, null);
    }

    public AutoCompleteEntry(final JsId jsid, final String label, final String tooltip, final ItemDefinition definition,
            final AbstractAttributeReader labelTemplate,
            final String valueAttributeName) {
        this(jsid, label, tooltip, definition, labelTemplate, valueAttributeName, null, null, null);
    }

    @Override
    public String _getValue() {
        return this.inputComponent.getValue();
    }

    @Override
    public void _setValue(final String value) {
        this.inputComponent.setValue(value);
    }

    public AutoCompleteEntry addFilter(String filterName, String filterValue) {
        inputComponent.addSearchFilter(filterName, filterValue);
        return this;
    }
    
    @Override
    protected Element makeInput(final String uid2) {
        final Element div = DOM.createDiv();
        AbstractComponent.appendComponentToHtml(div, this.inputComponent);
        return div;
    }

    public <H extends EventHandler> void addInputHandler(H handler, GwtEvent.Type<H> type) {
        this.inputComponent.addHandler(handler, type);
    }
}
