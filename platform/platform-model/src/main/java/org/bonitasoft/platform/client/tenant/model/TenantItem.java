/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.platform.client.tenant.model;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Haojie Yuan
 */
public class TenantItem extends Item {

    public TenantItem() {
        super();
    }

    public TenantItem(final IItem item) {
        super(item);
    }

    public final static String ATTRIBUTE_ICON = "icon";

    public final static String ATTRIBUTE_ID = "id";

    public final static String ATTRIBUTE_NAME = "name";

    public final static String ATTRIBUTE_DESCRIPTION = "desc";

    public final static String ATTRIBUTE_STATE = "state";

    public final static String ATTRIBUTE_CREATED_DATE = "creation";

    public final static String ATTRIBUTE_USERNAME = "username";

    public final static String ATTRIBUTE_PASSWORD = "password";

    public final static String STATE_ENABLE = "ACTIVATED";

    public final static String STATE_DISABLE = "DEACTIVATED";

    public TenantItem(final String tenantId, final String name, final String description, final String iconPath, final String state, final String creationDate,
            final String username, final String password) {
        this.setAttribute(ATTRIBUTE_ID, tenantId);
        this.setAttribute(ATTRIBUTE_NAME, name);
        this.setAttribute(ATTRIBUTE_DESCRIPTION, description);
        this.setAttribute(ATTRIBUTE_ICON, iconPath);
        this.setAttribute(ATTRIBUTE_STATE, state);
        this.setAttribute(ATTRIBUTE_CREATED_DATE, creationDate);
        this.setAttribute(ATTRIBUTE_USERNAME, username);
        this.setAttribute(ATTRIBUTE_PASSWORD, password);
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new TenantDefinition();
    }

}
