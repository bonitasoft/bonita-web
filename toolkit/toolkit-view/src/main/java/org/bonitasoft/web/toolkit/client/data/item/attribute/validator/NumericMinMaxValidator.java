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
public class NumericMinMaxValidator extends AbstractNumericValidator {

    private Double min = null;

    private Double max = null;

    private final Boolean includeMin;

    private final Boolean includeMax;

    public NumericMinMaxValidator(final Long min, final Long max) {
        this(min != null ? new Double(min) : (Double) null, max != null ? new Double(max) : (Double) null);
    }

    public NumericMinMaxValidator(final Double min, final Double max) {
        this(min, false, max, false);
    }

    public NumericMinMaxValidator(final Double min, final Boolean includeMin, final Double max) {
        this(min, includeMin, max, false);
    }

    public NumericMinMaxValidator(final Double min, final Double max, final Boolean includeMax) {
        this(min, false, max, includeMax);
    }

    public NumericMinMaxValidator(final Double min, final Boolean includeMin, final Double max, final Boolean includeMax) {
        super();
        this.min = min;
        this.max = max;
        this.includeMax = includeMax;
        this.includeMin = includeMin;

    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.client.toolkit.item.attribute.validator.AbstractStringValidator#check(java.lang.Object)
     */
    @Override
    protected final void _check(final Double attributeValue) {
        // Checking for including the min
        if (this.includeMin) {
            if (this.min != null && attributeValue.compareTo(this.min) < 0) {
                addError(_("%attribute% must be more or equal than %value%", new Arg("value", attributeValue)));
            }
        } else {
            if (this.min != null && attributeValue.compareTo(this.min) <= 0) {
                addError(_("%attribute% must be more than %value%", new Arg("value", attributeValue)));
            }
        }

        // Checking for including the max
        if (this.includeMax) {
            if (this.max != null && attributeValue.compareTo(this.max) > 0) {
                addError(_("%attribute% must be less or equal than %value%", new Arg("value", attributeValue)));
            }
        } else {
            if (this.max != null && attributeValue.compareTo(this.max) >= 0) {
                addError(_("%attribute% must be less than %value%", new Arg("value", attributeValue)));
            }
        }
    }

}
