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

import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class Textarea extends Component {

    private String defaultValue = null;

    /**
     * @param value
     *            the value to set
     */
    public Textarea setValue(final String value) {
        if (isGenerated()) {
            _setValue(this.element, value);
        } else {
            this.defaultValue = value;
        }
        return this;
    }

    public native void _setValue(final Element e, final String value)
    /*-{
        $wnd.$(e).val(value);
    }-*/;

    public String getValue() {
        return _getValue(this.element);
    }

    public native String _getValue(final Element e)
    /*-{
        return $wnd.$(e).val();
    }-*/;

    @Override
    protected Element makeElement() {
        this.element = DOM.createTextArea();
        if (this.defaultValue != null) {
            this.element.setInnerText(this.defaultValue);
            this.defaultValue = null; // Memory cleaning
        }

        return this.element;
    }

}
