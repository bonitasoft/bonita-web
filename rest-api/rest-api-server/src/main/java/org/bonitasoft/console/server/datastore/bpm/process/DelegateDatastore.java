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
package org.bonitasoft.console.server.datastore.bpm.process;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.api.model.bpm.process.DelegationItem;

/**
 * @author Haojie Yuan
 * 
 */
public class DelegateDatastore {


    private LinkedHashMap<String, DelegationItem> USERS = null;

    /**
     * Set api session
     * 
     * @param apiSession
     *            the apiSession to set
     */
    public DelegateDatastore(final APISession apiSession) {
    }

    /**
     * Default constructor.
     */
    public DelegateDatastore() {
        this.USERS = new LinkedHashMap<String, DelegationItem>();

        final String iconPath = "http://www.veryicon.com/icon/png/System/Scrap/User.png";

        for (int id = 1; id < 11; id++) {
            this.USERS
                    .put(String.valueOf(id), new DelegationItem(id, iconPath, "DelegateUser_" + id, getRandomDate(), getRandomDate(), "No delegates defined"));
        }
    }

    public static String getRandomDate() {
        final Date date = new Date((long) (Math.random() * 851990400 - 157766400));
        return date.toString();
    }

    public long getDelegateCount() throws Exception {
        return this.USERS.size();
    }

    public DelegationItem getDelegate(final long id) {
        return this.USERS.get(String.valueOf(id));
    }

    public List<DelegationItem> getDelegates(final String search, final Map<String, String> filters, final String order, final int pageIndex,
            final int itemPerPage)
            throws Exception {
        final ArrayList<DelegationItem> userList = new ArrayList<DelegationItem>(this.USERS.values());

        if (search != null) {
            final String searchRef = search.toLowerCase();

            for (int i = 0; i < userList.size(); i++) {
                final DelegationItem delegateItem = userList.get(i);
                if (delegateItem.getAttributeValue(DelegationItem.ATTRIBUTE_USER_NAME).toLowerCase().indexOf(searchRef) < 0) {
                    userList.remove(i--);
                }
            }
        }

        if (filters != null) {
            for (final String name : filters.keySet()) {
                final String value = filters.get(name);

                for (int i = 0; i < userList.size(); i++) {
                    final DelegationItem delegateItem = userList.get(i);
                    if (!value.equals(delegateItem.getAttributeValue(name))) {
                        userList.remove(i--);
                    }
                }
            }
        }

        if (userList.size() < pageIndex * itemPerPage) {
            return new ArrayList<DelegationItem>();
        }

        return userList.subList(pageIndex * itemPerPage, Math.min(userList.size(), pageIndex * itemPerPage + itemPerPage));
    }

    public String addDelegate(final DelegationItem aUser) throws Exception {
        aUser.setAttribute(DelegationItem.ATTRIBUTE_ID, String.valueOf(getDelegateCount() + 1));
        this.USERS.put(String.valueOf(getDelegateCount() + 1), aUser);

        return String.valueOf(getDelegateCount());
    }

    public DelegationItem updateDelegate(final long userUUID, final DelegationItem aUser) throws Exception {

        final DelegationItem delegateItem = this.USERS.get(String.valueOf(userUUID));
        delegateItem.setAttributes(aUser.getAttributes());

        return delegateItem;
    }

    public boolean deleteDelegates(final Collection<String> ids) throws Exception {

        for (final String uuid : ids) {
            this.USERS.remove(uuid);
        }
        return true;

    }

}
