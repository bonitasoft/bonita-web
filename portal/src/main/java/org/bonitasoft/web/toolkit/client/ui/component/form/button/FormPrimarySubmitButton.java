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
package org.bonitasoft.web.toolkit.client.ui.component.form.button;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonPrimaryAction;

/**
 * @author Séverin Moussel
 * 
 */
public class FormPrimarySubmitButton extends FormSubmitButton {

    public FormPrimarySubmitButton(JsId jsid, String label, String tooltip, Action action) {
        super(jsid, label, tooltip, action);
    }

    @Override
    protected void postProcessHtml() {
        super.postProcessHtml();
        element.addClassName(ButtonPrimaryAction.BTN_PRIMARY_ACTION_CLASS);
        element.removeClassName(ButtonAction.CSS_CLASS);

    }
}
