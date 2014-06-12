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

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * @author Séverin Moussel
 * 
 */
public class StringMinMaxLengthValidator extends AbstractStringValidator {

    private Integer minLength = null;

    private Integer maxLength = null;

    private final Boolean includeMin;

    private final Boolean includeMax;

    public StringMinMaxLengthValidator(final Integer minLength, final Integer maxLength) {
        this(minLength, false, maxLength, false);
    }

    public StringMinMaxLengthValidator(final Integer minLength, final Boolean includeMin, final Integer maxLength, final Boolean includeMax) {
        super();

        this.minLength = minLength;
        this.maxLength = maxLength;

        if (this.minLength != null && this.maxLength != null && this.minLength.compareTo(this.maxLength) > 0) {
            throw new IllegalArgumentException("minlength must be lower or equal to maxLength");
        }

        this.includeMax = includeMax;
        this.includeMin = includeMin;

    }

    @Override
    protected void _check(final String attributeValue) {
        final int length = attributeValue.length();

        // Checking for including the minLength
        if (includeMin) {
            if (minLength != null && length < minLength) {
                addError(_("%attribute% must be more or equal than %value%", new Arg("value", minLength)));
            }
        } else {
            if (minLength != null && length <= minLength) {
                addError(_("%attribute% must be more than %value%", new Arg("value", minLength)));
            }
        }

        // Checking for including the maxLength
        if (includeMax) {
            if (maxLength != null && length > maxLength) {
                addError(_("%attribute% must be less or equal than %value%", new Arg("value", maxLength)));
            }
        } else {
            if (maxLength != null && length >= maxLength) {
                addError(_("%attribute% must be less than %value%", new Arg("value", maxLength)));
            }
        }

    }

}
