/**
 * Copyright (C) 2012 BonitaSoft S.A.
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

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * 
 * @author Paul Amar
 * 
 */

public class ColorPicker extends Component {

    private String defaultValue;

    /**
     * Default Constructor.
     * 
     * @param jsid
     */
    public ColorPicker(final JsId jsid) {
        super(jsid);
    }

    public ColorPicker(final JsId jsid, final String defaultValue) {
        this(jsid);
        this.defaultValue = defaultValue;
    }

    @Override
    protected Element makeElement() {
        this.element = DOM.createInputText();
        this.element.addClassName("colorpicker");
        this.element.setAttribute("name", getJsId().toString());
        this.element.setAttribute("size", "7");
        if (this.defaultValue != null) {
            this.element.setAttribute("value", this.defaultValue);
        }
        return this.element;
    }

    public String getValue() {
        return this.getValue(this.element);
    }

    private native String getValue(Element e)
    /*-{
        return $wnd.$(e).val();
    }-*/;

    public ColorPicker setValue(final String value) {
        if (isGenerated()) {
            this.setValue(this.element, value);
        } else {
            this.defaultValue = value;
        }
        return this;
    }

    private native void setValue(Element e, String value)
    /*-{
        $wnd.$(e).val(value);
    }-*/;
}
