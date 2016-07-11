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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.validator;

import org.bonitasoft.forms.client.model.FormFieldValue;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Aurelien Pupier
 * 
 */
public class LengthValidator extends AbstractFormValidator implements IFormFieldValidator {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(GroovyPageValidator.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(final FormFieldValue fieldInput, final Locale locale) {
        if (fieldInput.getValue() != null) {
            final String fieldValue = fieldInput.getValue().toString();
            if (fieldValue.length() > 0) {
                try {
                    final int targetLength = Integer.parseInt(getParameter().getContent());
                    return fieldValue.length() == targetLength;
                } catch (final NumberFormatException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "The parameter for a length validator should be an integer.", e);
                    }
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDisplayName() {
        return "Length";
    }

}
