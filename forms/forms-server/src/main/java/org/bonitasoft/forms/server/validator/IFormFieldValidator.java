/**
 * Copyright (C) 2009 BonitaSoft S.A.
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
 * Form field Validator Interface
 * 
 * @author Anthony Birembaut
 */
public interface IFormFieldValidator {

    /**
     * Validate a field
     * @param fieldInput a field value
     * @param locale the user's locale (useful for dates validation)
     * @return true if the field value comply with the validation. false otherwise
     */
    boolean validate(FormFieldValue fieldInput, Locale locale);
    
    /**
     * @return the display name of the validator
     */
    String getDisplayName();

}
