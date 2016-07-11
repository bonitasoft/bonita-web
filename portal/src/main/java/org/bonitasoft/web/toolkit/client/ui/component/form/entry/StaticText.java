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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import org.bonitasoft.web.toolkit.client.ui.JsId;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 */
public class StaticText extends Input {

    public StaticText(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description, final String example) {
        super(jsid, label, tooltip, defaultValue, description, example);
    }

    public StaticText(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        this(jsid, label, tooltip, defaultValue, description, null);
    }

    public StaticText(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        this(jsid, label, tooltip, defaultValue, null, null);
    }

    public StaticText(final JsId jsid, final String label, final String tooltip) {
        this(jsid, label, tooltip, null, null, null);
    }

    @Override
    protected Element makeInput(final String uid) {
        final Element input = DOM.createElement("div");
        input.setInnerText(this.defaultValue);
        input.setAttribute("name", getJsId().toString());
        input.setAttribute("title", this.tooltip);
        input.setId(uid);
        return input;
    }

    @Override
    protected String getInputType() {
        return "text";
    }

    @Override
    public String _getValue() {
        return this.inputElement.getInnerText();
    }

    @Override
    public void _setValue(final String value) {
        this.inputElement.setInnerText(value);
    }
}
