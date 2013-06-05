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

import java.util.Date;

import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.DateFormatModifier;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.DatePicker;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

import com.google.gwt.user.client.Element;

/**
 * @author Paul AMAR
 * 
 */
public class DatePickerEntry extends FormEntry {

    private DatePicker inputComponent = null;

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
    public DatePickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description, final String example) {
        super(jsid, label, tooltip, defaultValue, description, example);
        this.inputComponent = new DatePicker(jsid, defaultValue);

        addInputModifier(new DateFormatModifier(DateFormat.FORMAT.FORM, DateFormat.FORMAT.SQL));
        addOutputModifier(new DateFormatModifier(DateFormat.FORMAT.SQL, DateFormat.FORMAT.FORM));

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
    public DatePickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
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
    public DatePickerEntry(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        this(jsid, label, tooltip, defaultValue, null);
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     * @param label
     * @param tooltip
     */
    public DatePickerEntry(final JsId jsid, final String label, final String tooltip) {
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

    /**
     * @param startDate
     */
    public DatePickerEntry setStartDate(final Date startDate) {
        this.inputComponent.setStartDate(startDate);
        return this;
    }

    /**
     * @param endDate
     */
    public DatePickerEntry setEndDate(final Date endDate) {
        this.inputComponent.setEndDate(endDate);
        return this;
    }

}
