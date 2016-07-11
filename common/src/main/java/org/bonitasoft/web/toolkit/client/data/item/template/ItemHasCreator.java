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
package org.bonitasoft.web.toolkit.client.data.item.template;

import java.util.Date;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Séverin Moussel
 * 
 */
public interface ItemHasCreator {

    public static final String ATTRIBUTE_CREATION_DATE = "creation_date";

    public static final String ATTRIBUTE_CREATED_BY_USER_ID = "created_by_user_id";

    public abstract void setCreationDate(String date);

    public abstract void setCreationDate(Date date);

    public abstract Date getCreationDate();

    public abstract void setCreatedByUserId(String id);

    public abstract void setCreatedByUserId(Long id);

    public abstract void setCreatedByUserId(APIID id);

    public abstract APIID getCreatedByUserId();

    public IItem getCreatedByUser();
}
