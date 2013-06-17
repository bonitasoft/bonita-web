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

import static org.bonitasoft.web.rest.api.model.portal.profile.AbstractMemberItem.ATTRIBUTE_GROUP_ID;
import static org.bonitasoft.web.rest.api.model.portal.profile.AbstractMemberItem.ATTRIBUTE_ROLE_ID;
import static org.bonitasoft.web.rest.api.model.portal.profile.AbstractMemberItem.ATTRIBUTE_USER_ID;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.api.model.identity.GroupItem;
import org.bonitasoft.web.rest.api.model.identity.RoleItem;
import org.bonitasoft.web.rest.api.model.identity.UserItem;
import org.bonitasoft.web.rest.api.model.portal.profile.AbstractMemberItem;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.CompoundAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;

/**
 * @author Séverin Moussel
 * 
 */
public class MemberAttributeReader extends CompoundAttributeReader {

    private static final String TEMPLATE_USER = _("%firstname% %lastname%");

    private static final String TEMPLATE_ROLE = _("%rolename%");

    private static final String TEMPLATE_GROUP = _("%groupname%");

    private static final String TEMPLATE_MEMBERSHIP = _("%rolename% of %groupname%");
    
    public MemberAttributeReader() {
        super("member", null);
        addReader("firstname", new DeployedAttributeReader(ATTRIBUTE_USER_ID, UserItem.ATTRIBUTE_FIRSTNAME));
        addReader("lastname", new DeployedAttributeReader(ATTRIBUTE_USER_ID, UserItem.ATTRIBUTE_LASTNAME));
        addReader("rolename", new DeployedAttributeReader(ATTRIBUTE_ROLE_ID, RoleItem.ATTRIBUTE_DISPLAY_NAME));
        addReader("groupname", new DeployedAttributeReader(ATTRIBUTE_GROUP_ID, GroupItem.ATTRIBUTE_DISPLAY_NAME));
    }

    public MemberAttributeReader(String memberType) {
        super("member", null);
        if (memberType.equals(ProfileMemberItem.VALUE_MEMBER_TYPE_USER)) {
            addReader("firstname", new DeployedAttributeReader(ATTRIBUTE_USER_ID, UserItem.ATTRIBUTE_FIRSTNAME));
            addReader("lastname", new DeployedAttributeReader(ATTRIBUTE_USER_ID, UserItem.ATTRIBUTE_LASTNAME));
        } else if (memberType.equals(ProfileMemberItem.VALUE_MEMBER_TYPE_ROLE)) {
            addReader("rolename", new DeployedAttributeReader(ATTRIBUTE_ROLE_ID, RoleItem.ATTRIBUTE_DISPLAY_NAME));
        } else if (memberType.equals(ProfileMemberItem.VALUE_MEMBER_TYPE_GROUP)) {
            addReader("groupname", new DeployedAttributeReader(ATTRIBUTE_GROUP_ID, GroupItem.ATTRIBUTE_DISPLAY_NAME));
        } else if (memberType.equals(ProfileMemberItem.VALUE_MEMBER_TYPE_MEMBERSHIP)) {
            addReader("groupname", new DeployedAttributeReader(ATTRIBUTE_GROUP_ID, GroupItem.ATTRIBUTE_DISPLAY_NAME));
            addReader("rolename", new DeployedAttributeReader(ATTRIBUTE_ROLE_ID, RoleItem.ATTRIBUTE_DISPLAY_NAME));
        }
    }

    public static String readMember(final AbstractMemberItem member) {
        return new MemberAttributeReader().read(member);
    }

    @Override
    protected String _read(final IItem item) {
        if (item.getAttributeValueAsAPIID(ATTRIBUTE_USER_ID) != null) {
            setTemplate(TEMPLATE_USER);
        } else if (item.getAttributeValueAsAPIID(ATTRIBUTE_ROLE_ID) != null && item.getAttributeValueAsAPIID(ATTRIBUTE_GROUP_ID) != null) {
            setTemplate(TEMPLATE_MEMBERSHIP);
        } else if (item.getAttributeValueAsAPIID(ATTRIBUTE_ROLE_ID) != null) {
            setTemplate(TEMPLATE_ROLE);
        } else if (item.getAttributeValueAsAPIID(ATTRIBUTE_GROUP_ID) != null) {
            setTemplate(TEMPLATE_GROUP);
        }

        return super._read(item);
    }

}
