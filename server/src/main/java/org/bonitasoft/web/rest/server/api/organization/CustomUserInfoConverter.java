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
package org.bonitasoft.web.rest.server.api.organization;

import org.bonitasoft.engine.identity.CustomUserInfo;
import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.engine.identity.CustomUserInfoValue;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInfoConverter extends ItemConverter<CustomUserInfoItem, CustomUserInfoValue> {

    public CustomUserInfoDefinitionItem convert(CustomUserInfoDefinition definition) {
        CustomUserInfoDefinitionItem item = new CustomUserInfoDefinitionItem();
        item.setId(APIID.makeAPIID(definition.getId()));
        item.setName(definition.getName());
        item.setDescription(definition.getDescription());
        return item;
    }

    public CustomUserInfoItem convert(CustomUserInfo information) {
        CustomUserInfoItem item = new CustomUserInfoItem();
        item.setUserId(information.getUserId());
        item.setDefinition(convert(information.getDefinition()));
        item.setValue(information.getValue());
        return item;
    }

    @Override
    public CustomUserInfoItem convert(CustomUserInfoValue value) {
        CustomUserInfoItem item = new CustomUserInfoItem();
        item.setUserId(value.getUserId());
        item.setDefinition(APIID.makeAPIID(value.getDefinitionId()));
        item.setValue(value.getValue());
        return item;
    }
}
