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

import java.sql.Date;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * @author Paul AMAR
 * 
 */
public class DateMinMaxValidator extends AbstractDateValidator {

    private final Date min;

    private final Date max;

    private final Boolean includeMinDate;

    private final Boolean includeMaxDate;

    public DateMinMaxValidator(final Date min, final Date max) {
        this(min, false, max, false);
    }

    public DateMinMaxValidator(final Date min, final Boolean includeMinDate, final Date max) {
        this(min, includeMinDate, max, false);
    }

    public DateMinMaxValidator(final Date min, final Date max, final Boolean includeMaxDate) {
        this(min, false, max, includeMaxDate);
    }

    public DateMinMaxValidator(final Date min, final Boolean includeMinDate, final Date max, final Boolean includeMaxDate) {
        super();
        this.min = min;
        this.max = max;
        this.includeMinDate = includeMinDate;
        this.includeMaxDate = includeMaxDate;
    }

    @Override
    public void _check(final Date attributeValue) {
        // Checking for including the minDate
        if (this.includeMinDate) {
            if (this.min != null && attributeValue.compareTo(this.min) < 0) {
                addError(_("%attribute% must be more or equal than %value%", new Arg("value", this.min)));
            }
        } else {
            if (this.min != null && attributeValue.compareTo(this.min) <= 0) {
                addError(_("%attribute% must be more than %value%", new Arg("value", this.min)));
            }
        }

        // Checking for including the maxDate
        if (this.includeMaxDate) {
            if (this.max != null && attributeValue.compareTo(this.max) > 0) {
                addError(_("%attribute% must be less or equal than %value%", new Arg("value", this.max)));
            }
        } else {
            if (this.max != null && attributeValue.compareTo(this.max) >= 0) {
                addError(_("%attribute% must be less than %value%", new Arg("value", this.max)));
            }
        }
    }
}
