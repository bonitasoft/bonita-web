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
package org.bonitasoft.web.rest.model.bpm.process;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * Delegation item
 * 
 * @author Qixiang Zhang
 * 
 */
public class DelegationItem extends Item {

    public DelegationItem() {
        super();
    }

    public DelegationItem(final IItem item) {
        super(item);
    }

    /**
     * Delegate of id
     */
    public static final String ATTRIBUTE_ID = "delegateId";

    /**
     * Delegate of icon
     */
    public static final String ATTRIBUTE_ICON = "icon";

    /**
     * User of id
     */
    public static final String ATTRIBUTE_USER_NAME = "userName";

    /**
     * Start date
     */
    public static final String ATTRIBUTE_START_DATE = "startDate";

    /**
     * End date
     */
    public static final String ATTRIBUTE_END_DATE = "endDate";

    /**
     * Delegate state
     */
    public static final String ATTRIBUTE_DELEGATE_STATE = "delegateState";

    /**
     * 
     * Default Constructor.
     * 
     * @param delegateId
     *            delegate of id
     * @param iconPath
     *            icon of path
     * @param userName
     *            user of name
     * @param startDate
     *            start date
     * @param endDate
     *            end date
     * @param delegateState
     *            delegate state
     */
    public DelegationItem(final long delegateId, final String iconPath, final String userName, final String startDate, final String endDate,
            final String delegateState) {
        this.setAttribute(ATTRIBUTE_ID, String.valueOf(delegateId));
        this.setAttribute(ATTRIBUTE_ICON, iconPath);
        this.setAttribute(ATTRIBUTE_USER_NAME, userName);
        this.setAttribute(ATTRIBUTE_START_DATE, startDate);
        this.setAttribute(ATTRIBUTE_END_DATE, endDate);
        this.setAttribute(ATTRIBUTE_DELEGATE_STATE, delegateState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDefinition getItemDefinition() {
        return new DelegationDefinition();
    }

}
