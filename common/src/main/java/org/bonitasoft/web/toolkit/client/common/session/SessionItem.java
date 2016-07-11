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
package org.bonitasoft.web.toolkit.client.common.session;

import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Julien Mege
 */
public class SessionItem extends Item {

    public SessionItem() {
        super();
    }

    public static final String ATTRIBUTE_SESSIONID = "session_id";

    public static final String ATTRIBUTE_USERID = "user_id";

    public static final String ATTRIBUTE_FIRSTNAME = "first_name";

    public static final String ATTRIBUTE_LASTNAME = "last_name";

    public static final String ATTRIBUTE_USERNAME = "user_name";

    public static final String ATTRIBUTE_ICON = "icon";

    public static final String ATTRIBUTE_IS_TECHNICAL_USER = "is_technical_user";

    public static final String ATTRIBUTE_VERSION = "version";

    public static final String ATTRIBUTE_CONF = "conf";

    public static final String ATTRIBUTE_COPYRIGHT = "copyright";

    @Override
    public ItemDefinition getItemDefinition() {
        return new SessionDefinition();
    }

}
