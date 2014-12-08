/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 *
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
 * @author Aurelien Pupier
 * Check that the input match with an email address.
 */
public class MailValidator implements IFormFieldValidator {

	/* (non-Javadoc)
	 * @see org.bonitasoft.forms.server.validator.IFormFieldValidator#validate(org.bonitasoft.forms.client.model.FormFieldValue, java.util.Locale)
	 */
	public boolean validate(final FormFieldValue fieldInput, final Locale locale) {
        if (fieldInput.getValue() instanceof String) {
            final String fieldValue = (String)fieldInput.getValue();
            if (fieldValue != null && fieldValue.length() > 0) {
    	        return fieldValue.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    	    } else {
    	        return true;
    	    }
        }
        return false;
	}
	
	public String getDisplayName() {
		return "Mail";
	}

}
