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
package org.bonitasoft.console.server.model.builder.profile.member;

/**
 * @author Vincent Elcrin
 * 
 */
public abstract class AbstractProfileMemberBuilder<O> {

    protected Long id = 1L;
    protected Long profileId = 2L;
    protected Long userId = 3L;
    protected Long groupId = 4L;
    protected Long roleId = 5L;

    public AbstractProfileMemberBuilder<O> withId(Long id) {
        this.id = id;
        return this;
    }

    public AbstractProfileMemberBuilder<O> withProfileId(Long id) {
        this.profileId = id;
        return this;
    }

    public AbstractProfileMemberBuilder<O> withUserId(Long id) {
        this.userId = id;
        return this;
    }

    public AbstractProfileMemberBuilder<O> withGroupId(Long id) {
        this.groupId = id;
        return this;
    }

    public AbstractProfileMemberBuilder<O> withRoleId(Long id) {
        this.roleId = id;
        return this;
    }

    public abstract O build();

}
