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
package org.bonitasoft.web.rest.server.framework;

import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Vincent Elcrin
 * 
 */
public interface Deployer {

    String getDeployedAttribute();

    void deployIn(IItem item);

    default boolean isDeployable(final String attributeName, final IItem item) {
        final String attributeValue = item.getAttributeValue(attributeName);

        if (attributeValue != null && !attributeValue.isEmpty()) {
            try {
                final long longAttrValue = Long.valueOf(attributeValue);
                //only positive numeric Ids are supported
                return longAttrValue > 0L;
            } catch (final NumberFormatException e) {
                //non numeric Id are supported
                return true;
            }
        }
        return false;
    }
}
