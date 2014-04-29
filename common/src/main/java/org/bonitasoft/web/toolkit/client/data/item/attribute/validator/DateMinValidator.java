/**
 * Copyright (C) 2012 BonitaSoft S.A.
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

import java.sql.Date;

/**
 * @author Paul AMAR
 * 
 */
public class DateMinValidator extends DateMinMaxValidator {

    /**
     * Default Constructor.
     * 
     * @param min
     * @param includeMinDate
     */
    public DateMinValidator(final Date min, final Boolean includeMinDate) {
        super(min, includeMinDate, null, false);
    }

    /**
     * Default Constructor.
     * 
     * @param min
     */
    public DateMinValidator(final Date min) {
        this(min, false);
    }

}
