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
package org.bonitasoft.web.rest.server.datastore.profile.member;

import org.bonitasoft.web.rest.model.portal.profile.AbstractMemberItem;

/**
 * @author Vincent Elcrin
 * 
 */
public enum MemberType {
    USER(AbstractMemberItem.VALUE_MEMBER_TYPE_USER),
    GROUP(AbstractMemberItem.VALUE_MEMBER_TYPE_GROUP),
    ROLE(AbstractMemberItem.VALUE_MEMBER_TYPE_ROLE),
    MEMBERSHIP(AbstractMemberItem.VALUE_MEMBER_TYPE_MEMBERSHIP);

    private String type;

    private MemberType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static MemberType from(String type) {
        for (MemberType candidate : MemberType.values()) {
            if (candidate.getType().equals(type)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("No enum const for " + type + " found");
    }
}
