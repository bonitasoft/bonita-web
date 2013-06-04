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

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.ColorPicker;

import com.google.gwt.user.client.Element;

/**
 * @author Paul AMAR
 * 
 */
public class ColorPickerEntry extends FormEntry {

    private ColorPicker inputComponent = null;

    /**
     * Default Constructor.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param defaultValue
     * @param description
     * @param example
     */
    public ColorPickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description, final String example) {
        super(jsid, label, tooltip, defaultValue, description, example);
        this.inputComponent = new ColorPicker(jsid, defaultValue);
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param defaultValue
     * @param description
     */
    public ColorPickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        this(jsid, label, tooltip, defaultValue, description, null);
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     * @param defaultValue
     */
    public ColorPickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        this(jsid, label, tooltip, defaultValue, null);
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     */
    public ColorPickerEntry(final JsId jsid, final String label, final String tooltip) {
        this(jsid, label, tooltip, null);

    }

    @Override
    public void _setValue(final String value) {
        this.inputComponent.setValue(value);
    }

    @Override
    protected Element makeInput(final String uid2) {
        return this.inputComponent.getElement();
    }

    @Override
    public String _getValue() {
        return this.inputComponent.getValue();
    }

}
