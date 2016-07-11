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
package org.bonitasoft.web.rest.server.api.bpm.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.process.DelegationDefinition;
import org.bonitasoft.web.rest.model.bpm.process.DelegationItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.bpm.process.DelegateDatastore;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Julien Mege
 * 
 */
public class APIDelegation extends ConsoleAPI<DelegationItem> {

    public static final String APISESSION = "apiSession";

    private final DelegateDatastore delegateDatastore = new DelegateDatastore();

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(DelegationDefinition.TOKEN);
    }

    @Override
    public DelegationItem get(final APIID id) {
        return this.delegateDatastore.getDelegate(id.toLong());
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }

    @Override
    public ItemSearchResult<DelegationItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters)
    {
        try {
            final List<DelegationItem> items = this.delegateDatastore.getDelegates(search, filters, orders, page, resultsByPage);
            return new ItemSearchResult<DelegationItem>(page, resultsByPage, this.delegateDatastore.getDelegateCount(), items);

        } catch (final Exception e) {
            throw new APIException(e);
        }

    }

    @Override
    public DelegationItem add(final DelegationItem item) {
        try {

            final DelegationItem delegation = (DelegationItem) Definitions.get(DelegationDefinition.TOKEN).createItem();
            for (final String attributeName : item.getAttributeNames()) {
                if ("icon".equals(attributeName)) {
                    final String iconPath = item.getAttributeValue(attributeName);
                    final String iconName = iconPath.substring(iconPath.lastIndexOf("\\") + 1);
                    delegation.setAttribute(attributeName, iconName);
                } else {
                    delegation.setAttribute(attributeName, item.getAttributeValue(attributeName));
                }
            }

            final String newUuid = this.delegateDatastore.addDelegate(delegation);
            /** TODO Implement Delegation Feature */
            return null;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public DelegationItem update(final APIID id, final Map<String, String> item) {
        try {
            final DelegationItem user = this.delegateDatastore.getDelegate(id.toLong());

            for (final String attributeName : item.keySet()) {
                if (user.hasAttribute(attributeName)) {
                    if ("icon".equals(attributeName)) {
                        final String iconPath = item.get(attributeName);
                        final String iconName = iconPath.substring(iconPath.lastIndexOf("\\") + 1);
                        user.setAttribute(attributeName, iconName);
                    } else {
                        user.setAttribute(attributeName, item.get(attributeName));
                    }
                }
            }

            return this.delegateDatastore.updateDelegate(id.toLong(), user);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public void delete(final List<APIID> ids) {
        try {
            final ArrayList<String> idList = new ArrayList<String>();
            for (final APIID appiId : ids) {
                idList.add(appiId.toString());
            }
            this.delegateDatastore.deleteDelegates(idList);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    // /**
    // * build client user and add openTasksNb, overdueOpenTasksNb
    // *
    // * @param user
    // * @param apiSession
    // * @throws ConsoleException
    // */
    // private UserItem buildClientUser(final User user, final APISession apiSession) throws ConsoleException {
    // final HumanTaskDatastore taskDatastore = new HumanTaskDatastore(apiSession);
    // try {
    // final Map<Long, Long> openTasksNb = taskDatastore.getNumberOfOpenTasks(user.getId());
    // final Map<Long, Long> overdueOpenTasksNb = taskDatastore.getNumberOfOverdueOpenTasks(user.getId());
    // return UserUtil.buildClientUser(apiSession.getRequestedTenantId(), user, openTasksNb.get(user.getId()), overdueOpenTasksNb.get(user.getId()));
    // } catch (final ConsoleException e) {
    // if (LOGGER.isLoggable(Level.SEVERE)) {
    // LOGGER.log(Level.SEVERE, e.getMessage(), e);
    // }
    // throw e;
    // }
    // }

    @Override
    protected void fillDeploys(final DelegationItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final DelegationItem item, final List<String> counters) {
    }

}
