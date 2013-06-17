/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.platform.client.platform.model;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Zhiheng Yang
 * 
 */
public class PlatformItem extends Item {

    public PlatformItem() {
        super();
    }

    public PlatformItem(final IItem item) {
        super(item);
    }

    public final static String ATTRIBUTE_VERSION = "version";

    public final static String ATTRIBUTE_PRE_VERSION = "previousVersion";

    public final static String ATTRIBUTE_INIT_VERSION = "initialVersion";

    public final static String ATTRIBUTE_CREATED_DATE = "created";

    public final static String ATTRIBUTE_CREATEDBY = "createdBy";

    public final static String ATTRIBUTE_STATE = "state";

    public PlatformItem(final String version, final String preVersion, final String initVersion, final String createdDate, final String createdBy,
            final String state) {

        this.setAttribute(ATTRIBUTE_VERSION, version);
        this.setAttribute(ATTRIBUTE_PRE_VERSION, preVersion);
        this.setAttribute(ATTRIBUTE_INIT_VERSION, initVersion);
        this.setAttribute(ATTRIBUTE_CREATED_DATE, createdDate);
        this.setAttribute(ATTRIBUTE_CREATEDBY, createdBy);
        this.setAttribute(ATTRIBUTE_STATE, state);

    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new PlatformDefinition();
    }

}
