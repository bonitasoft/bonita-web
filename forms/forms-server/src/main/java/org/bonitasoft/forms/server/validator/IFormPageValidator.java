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
import java.util.Map;

import org.bonitasoft.forms.client.model.FormFieldValue;

/**
 * Form page validator interface
 * 
 * @author Anthony Birembaut
 */
public interface IFormPageValidator {

    /**
     * Validate the page
     * @param fieldValues a map of the fields ids and values
     * @param locale the user's locale (useful for dates validation)
     * @return true if the page's fields values comply with the validation. false otherwise
     */
    boolean validate(Map<String, FormFieldValue> fieldValues, Locale locale);
    
    /**
     * @return the display name of the validator
     */
    String getDisplayName();
    
}
