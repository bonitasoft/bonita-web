/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.identity;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Item;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInfoItem extends Item {

    public static final String FILTER_USER_ID = "userId";

    public static final String ATTRIBUTE_DEFINITION_ID = "definitionId";

    public static final String ATTRIBUTE_USER_ID = "userId";

    public static final String ATTRIBUTE_VALUE = "value";

    @Override
    public CustomUserInfoDefinition getItemDefinition() {
        return new CustomUserInfoDefinition();
    }

    public void setUserId(long userId) {
        setAttribute(ATTRIBUTE_USER_ID, userId);
    }

    public void setDefinition(APIID id) {
        setAttribute(ATTRIBUTE_DEFINITION_ID, id);
    }

    public void setDefinition(CustomUserInfoDefinitionItem definition) {
        setDefinition(definition.getId());
        setDeploy(ATTRIBUTE_DEFINITION_ID, definition);
    }

    public void setValue(String value) {
        setAttribute(ATTRIBUTE_VALUE, value);
    }

    public long getUserId() {
        return getAttributeValueAsLong(ATTRIBUTE_USER_ID);
    }

    public CustomUserInfoDefinitionItem getDefinition() {
        return (CustomUserInfoDefinitionItem) getDeploy(ATTRIBUTE_DEFINITION_ID);
    }

    public String getValue() {
        return getAttributeValue(ATTRIBUTE_VALUE);
    }
}
