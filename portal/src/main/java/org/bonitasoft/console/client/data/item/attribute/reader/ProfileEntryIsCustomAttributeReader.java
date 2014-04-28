/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.client.data.item.attribute.reader;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;


/**
 * @author Fabio Lombardi
 *
 */
public class ProfileEntryIsCustomAttributeReader extends AbstractAttributeReader {
    
    public ProfileEntryIsCustomAttributeReader(String attributeToRead) {
        super(attributeToRead);
    }
    
    @Override
    protected String _read(final IItem item) {
        final String isCustom = item.getAttributeValue(leadAttribute);
        if (isCustom.equals(Boolean.TRUE.toString())) {
            return "(C)";
        } else {
            return "";
        }
    }

}
