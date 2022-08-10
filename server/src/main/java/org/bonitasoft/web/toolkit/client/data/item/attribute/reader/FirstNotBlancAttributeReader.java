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

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Julien Mege
 * 
 */
public class FirstNotBlancAttributeReader extends AbstractAttributeReader {

    private final List<String> attributesToRead;

    public FirstNotBlancAttributeReader(final String... AttributesToRead) {
        this.attributesToRead = Arrays.asList(AttributesToRead);
    }

    @Override
    protected String _read(final IItem item) {

        for (final String attribute : this.attributesToRead) {
            if (!StringUtil.isBlank(item.getAttributeValue(attribute))) {
                return item.getAttributeValue(attribute);
            }
        }

        return null;
    }
}
