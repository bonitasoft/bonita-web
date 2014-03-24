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

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInfoAssociationDefinition extends ItemDefinition<CustomUserInfoItem> {

    public static final String TOKEN = "customuserinfo/user";

    @Override
    protected String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return "../API/identity/customuserinfo/user"; //<user id>
    }

    @Override
    protected void defineAttributes() {

    }

    @Override
    protected void definePrimaryKeys() {

    }

    @Override
    protected CustomUserInfoItem _createItem() {
        return new CustomUserInfoItem();
    }

    @Override
    public APICaller<CustomUserInfoItem> getAPICaller() {
        throw new UnsupportedOperationException("This is method is not used");
    }
}
