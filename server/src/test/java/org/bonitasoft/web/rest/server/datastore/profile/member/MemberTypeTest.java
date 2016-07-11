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

import static junit.framework.Assert.assertEquals;

import org.bonitasoft.web.rest.model.portal.profile.AbstractMemberItem;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class MemberTypeTest {

    @Test
    public void testWeCanRetrieveMemberTypeFromAString() throws Exception {
        MemberType type = MemberType.from(AbstractMemberItem.VALUE_MEMBER_TYPE_USER);

        assertEquals(MemberType.USER, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotExistingConstConversionThrowsException() throws Exception {
        MemberType type = MemberType.from("notExistingConst");

        assertEquals(MemberType.USER, type);
    }
}
