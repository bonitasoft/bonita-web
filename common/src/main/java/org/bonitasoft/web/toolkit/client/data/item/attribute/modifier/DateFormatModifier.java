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
package org.bonitasoft.web.toolkit.client.data.item.attribute.modifier;

import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

/**
 * 
 * 
 * @author Séverin Moussel
 * 
 */
public class DateFormatModifier extends AbstractStringModifier {

    private final DateFormat.FORMAT inputFormat;

    private final DateFormat.FORMAT outputFormat;

    public DateFormatModifier(final DateFormat.FORMAT inputFormat, final DateFormat.FORMAT outputFormat) {
        super();
        this.inputFormat = inputFormat;
        this.outputFormat = outputFormat;
    }

    @Override
    public String clean(final String value) {
        if (StringUtil.isBlank(value)) {
            return null;
        }
        try {
            return DateFormat.formatToFormat(value, this.inputFormat, this.outputFormat);
        } catch (final IllegalArgumentException e) {
            return value;
        }
    }

}
