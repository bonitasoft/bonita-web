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
package org.bonitasoft.web.toolkit.client.data.item.attribute.validator;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class AbstractNumericValidator extends AbstractStringValidator {

    @Override
    /**
     * Check if the value is numeric, then delegate to abstract _check(Double)
     */
    protected final void _check(final String attributeValue) {
        Double numericValue = null;
        try {
            numericValue = Double.valueOf(attributeValue);
        } catch (final NumberFormatException e) {
            addError(_("%attribute% must be a numeric value"));
        }

        if (numericValue != null) {
            this._check(numericValue);
        }

    }

    protected abstract void _check(Double attributeValue);

}
