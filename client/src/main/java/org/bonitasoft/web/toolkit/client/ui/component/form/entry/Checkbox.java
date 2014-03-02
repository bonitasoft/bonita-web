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

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.JsId;

import com.google.gwt.dom.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class Checkbox extends Input {

    protected boolean checked = false;

    public Checkbox(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        this(jsid, label, tooltip, defaultValue, null, false);
    }

    public Checkbox(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        this(jsid, label, tooltip, defaultValue, description, false);
    }

    public Checkbox(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description, final boolean checked) {
        super(jsid, label, tooltip, defaultValue, description);
        this.checked = checked;
        addClass("checkbox");
    }

    public void setChecked(final boolean checked) {
        this.checked = checked;

        if (isGenerated()) {
            this.setChecked(this.inputElement, checked);
        }
    }

    private native void setChecked(Element input, boolean checked)
    /*-{
         $wnd.$(input).check(checked);
    }-*/;

    @Override
    protected String getInputType() {
        return "checkbox";
    }

    @Override
    protected void postProcessHtml() {
        super.postProcessHtml();

        $(this.inputElement).after($(this.labelElement).children().get(0));

        if (this.checked) {
            this.setChecked(this.inputElement, true);
        }
    }

}
