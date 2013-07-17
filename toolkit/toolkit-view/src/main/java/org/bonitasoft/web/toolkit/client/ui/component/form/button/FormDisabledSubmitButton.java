/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component.form.button;

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form.FormSubmitAction;

import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Event;

/**
 * @author Rohart Bastien
 * 
 */
public class FormDisabledSubmitButton extends FormSubmitButton {

    public FormDisabledSubmitButton(JsId jsid, String label, String tooltip, FormSubmitAction action) {
        super(jsid, label, tooltip, action);
    }

    @Override
    protected void postProcessHtml() {
        element.addClassName("disabled");
        super.postProcessHtml();
        if (isEnabled()) {
            $(this.element).click(new Function() {

                @Override
                public boolean f(Event e) {
                    return false;
                }

            });
        }
    }
}
