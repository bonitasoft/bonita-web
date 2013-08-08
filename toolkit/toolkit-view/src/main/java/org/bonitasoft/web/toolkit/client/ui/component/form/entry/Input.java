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
public abstract class Input extends FormEntry {

    /**
     * Default Constructor.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     */
    public Input(final JsId jsid, final String label, final String tooltip) {
        this(jsid, label, tooltip, null, null, null);
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     */
    public Input(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        this(jsid, label, tooltip, defaultValue, null, null);
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param description
     */
    public Input(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        this(jsid, label, tooltip, defaultValue, description, null);
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param description
     * @param example
     */
    public Input(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description, final String example) {
        super(jsid, label, tooltip, defaultValue, description, example);
    }

    @Override
    protected Element makeInput(final String uid) {
        final Element input = DOM.createElement("input");
        input.setAttribute("type", getInputType());
        input.setAttribute("name", getJsId().toString());
        input.setAttribute("title", this.tooltip);
        if (!isEnabled()) {
            input.setAttribute("disabled", "disabled");
        }
        input.setId(uid);
        return input;
    }

    abstract protected String getInputType();

    @Override
    public String _getValue() {
        return this.val(this.inputElement);
    }

    protected native String val(Element e)
    /*-{
        var input = $wnd.$(e);
        
        if (input.is(':checkbox') || input.is(':radio')) {
            var form = input.closest('form');
            input = form.find('[name=' + input.attr('name') + ']:checked');
        }
        
        return input.val();
    }-*/;

    @Override
    public void _setValue(final String value) {
        this.val(this.inputElement, value);
    }

    protected native void val(Element e, final String value)
    /*-{
        $wnd.$(e).val(value);
    }-*/;

}
