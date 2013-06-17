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
package org.bonitasoft.web.rest.server.api.bpm.flownode;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.api.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.api.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.HumanTaskDatastore;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.api.Datastore;

/**
 * @author Séverin Moussel
 * 
 */
public class AbstractAPIHumanTask<ITEM extends IHumanTaskItem> extends AbstractAPITask<ITEM> {

    @Override
    protected HumanTaskDefinition defineItemDefinition() {
        return HumanTaskDefinition.get();
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new HumanTaskDatastore(getEngineSession());
    }

    @Override
    public String defineDefaultSearchOrder() {
        // FIXME
        return HumanTaskItem.ATTRIBUTE_PRIORITY + " DESC";
    }

    // // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CRUDS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ITEM update(final APIID id, final Map<String, String> attributes) {
        // Cant unassigned a manual task
        final String assignedUserId = attributes.get(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID);
        if (attributes.containsKey(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID) && StringUtil.isBlank(assignedUserId)) {
            if (get(id).isManualTask()) {
                throw new APIForbiddenException("Can't unassigned a manual task.");
            }
        }

        return super.update(id, attributes);
    }

    // // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected List<String> defineReadOnlyAttributes() {
        final List<String> attributes = super.defineReadOnlyAttributes();
        attributes.add(HumanTaskItem.ATTRIBUTE_ACTOR_ID);
        attributes.add(HumanTaskItem.ATTRIBUTE_ASSIGNED_DATE);

        return attributes;
    }

}
