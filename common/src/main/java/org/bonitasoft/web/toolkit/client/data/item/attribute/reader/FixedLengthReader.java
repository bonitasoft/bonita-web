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
package org.bonitasoft.web.toolkit.client.data.item.attribute.reader;

import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Colin PUY
 * 
 */
public class FixedLengthReader extends AbstractAttributeReader {

    private int maxLength;

    public FixedLengthReader(String attributeToRead, int maxLength) {
        super(attributeToRead);
        if (maxLength < 0) {
            throw new IllegalArgumentException("maxLength must be a positive value");
        }
        this.maxLength = maxLength;
    }

    @Override
    protected String _read(IItem item) {
        String value = item.getAttributeValue(getLeadAttribute());
        if (value != null && value.length() > maxLength) {
            return cutValueAndAddEllipsis(value);
        }
        return value;
    }

    private String cutValueAndAddEllipsis(String value) {
        if (maxLength > 3) {
            return value.substring(0, maxLength - 3) + "...";
        }
        return value.substring(0, maxLength);
    }
}
