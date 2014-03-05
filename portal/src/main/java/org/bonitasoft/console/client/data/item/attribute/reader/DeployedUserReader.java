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
package org.bonitasoft.console.client.data.item.attribute.reader;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasDeploys;

/**
 * @author Séverin Moussel
 * 
 */
public class DeployedUserReader extends UserAttributeReader implements HasDeploys {

    public DeployedUserReader(final String attributeToDeploy) {
        super(attributeToDeploy);
    }

    public static String readUser(final IItem item, final String attribute) {
        return new DeployedUserReader(attribute).read(item);
    }

    @Override
    public List<String> getDeploys() {
        return Arrays.asList(getLeadAttribute());
    }

    @Override
    protected String _read(final IItem item) {
        return super._read(getUserItem(item, getLeadAttribute()));
    }

    /**
     * @param item
     * @param attributeToRead
     */
    private UserItem getUserItem(final IItem item, final String attributeToRead) {
        final String userId = item.getAttributeValue(attributeToRead);
        if ("0".equals(userId)) {
            return createSystemUser();
        } else if (item.getDeploy(attributeToRead) == null) {
            if ("".equals(userId) && !"".equals(getDefaultValue())) {
                return createDefaultValueUser();
            }
            return createDeletedUser();
        } else {
            return new UserItem(item.getDeploy(attributeToRead));
        }
    }

    private UserItem createSystemUser() {
        final UserItem user = new UserItem();
        user.setUserName(_("system"));
        user.setFirstName(_("System"));
        return user;
    }

    private UserItem createDeletedUser() {
        final UserItem user = new UserItem();
        user.setUserName(_("deleted"));
        user.setFirstName(_("Deleted"));
        return user;
    }

    private UserItem createDefaultValueUser() {
        final UserItem user = new UserItem();
        user.setFirstName(getDefaultValue());
        return user;
    }

}
