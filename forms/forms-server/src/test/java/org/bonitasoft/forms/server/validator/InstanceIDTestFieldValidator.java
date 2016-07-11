/**
 * Copyright (C) 2010 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.validator;

import java.util.Locale;

import org.bonitasoft.forms.client.model.FormFieldValue;

/**
 * @author Anthony Birembaut
 *
 */
public class InstanceIDTestFieldValidator extends AbstractFormValidator implements IFormFieldValidator {

    /* (non-Javadoc)
     * @see org.bonitasoft.forms.server.validator.IFormFieldValidator#getDisplayName()
     */
    public String getDisplayName() {
        return "Process instance UUID validator";
    }

    /* (non-Javadoc)
     * @see org.bonitasoft.forms.server.validator.IFormFieldValidator#validate(org.bonitasoft.forms.client.model.FormFieldValue, java.util.Locale)
     */
    public boolean validate(FormFieldValue fieldInput, Locale locale) {
        if (getProcessInstanceID() != -1 && getProcessInstanceID() == (Long)fieldInput.getValue()) {
            return true;
        }
        return false;
    }

}
