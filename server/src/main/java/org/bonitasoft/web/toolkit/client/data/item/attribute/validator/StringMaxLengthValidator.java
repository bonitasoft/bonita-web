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

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * @author Séverin Moussel
 * 
 */
public class StringMaxLengthValidator extends AbstractStringValidator {
    private final Integer maxLength;
    private final Boolean includeMax;

    public StringMaxLengthValidator(final Integer maxLength) {
        this(maxLength, true);
    }
    public StringMaxLengthValidator(final Integer maxLength, final Boolean includeMax) {
        this.maxLength = maxLength;
        this.includeMax = includeMax;
    }

    @Override
    protected void _check(final String attributeValue) {
        final int length = attributeValue.length();

        // Checking for including the maxLength
        if (includeMax) {
            if (maxLength != null && length > maxLength) {
                addError(AbstractI18n.t_("%attribute% must be less or equal than %value%", new Arg("value", maxLength)));
            }
        } else {
            if (maxLength != null && length >= maxLength) {
                addError(AbstractI18n.t_("%attribute% must be less than %value%", new Arg("value", maxLength)));
            }
        }

    }
}
