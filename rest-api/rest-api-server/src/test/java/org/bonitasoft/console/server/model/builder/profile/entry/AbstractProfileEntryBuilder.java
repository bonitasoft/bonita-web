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
package org.bonitasoft.console.server.model.builder.profile.entry;

import org.bonitasoft.web.rest.api.model.portal.profile.ProfileEntryItem.VALUE_TYPE;

/**
 * @author Vincent Elcrin
 * 
 */
public abstract class AbstractProfileEntryBuilder<O> {

    protected long id = 1L;
    protected String name = "aName";
    protected String custormName = "aCustomName";
    protected long profileId = 2L;
    protected long index = 3L;
    protected VALUE_TYPE type = VALUE_TYPE.link;
    protected String description = "aDesciption";
    protected long parentId = 4L;
    protected String page = "aPageName";

    public AbstractProfileEntryBuilder<O> withId(long id) {
        this.id = id;
        return this;
    }

    public AbstractProfileEntryBuilder<O> withName(String name) {
        this.name = name;
        return this;
    }

    public AbstractProfileEntryBuilder<O> withCustomName(String name) {
        this.custormName = name;
        return this;
    }

    public AbstractProfileEntryBuilder<O> withProfileId(long profileId) {
        this.profileId = profileId;
        return this;
    }

    public AbstractProfileEntryBuilder<O> withIndex(int index) {
        this.index = index;
        return this;
    }

    public AbstractProfileEntryBuilder<O> withType(VALUE_TYPE type) {
        this.type = type;
        return this;
    }

    public AbstractProfileEntryBuilder<O> withDescription(String description) {
        this.description = description;
        return this;
    }

    public AbstractProfileEntryBuilder<O> withParentId(long parentId) {
        this.parentId = parentId;
        return this;
    }

    public AbstractProfileEntryBuilder<O> withPage(String page) {
        this.page = page;
        return this;
    }

    public abstract O build();

}
