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
package org.bonitasoft.web.rest.server.api.bpm.flownode;

import java.util.List;

import org.bonitasoft.web.rest.model.bpm.flownode.HiddenUserTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HiddenUserTaskItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.HiddenUserTaskDatastore;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.api.APIHasAdd;
import org.bonitasoft.web.toolkit.server.api.APIHasDelete;
import org.bonitasoft.web.toolkit.server.api.APIHasGet;

/**
 * @author Julien Mege, Vincent Elcrin
 * 
 */
public class APIHiddenUserTask extends ConsoleAPI<HiddenUserTaskItem> implements APIHasGet<HiddenUserTaskItem>, APIHasAdd<HiddenUserTaskItem>, APIHasDelete {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * api session
     */
    public static final String APISESSION = "apiSession";

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(HiddenUserTaskDefinition.TOKEN);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // C.R.U.D.S
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public HiddenUserTaskItem get(final APIID id) {
        // TODO write generic process in the toolkit
        if (id.getIds().size() != 2) {
            throw new APIException("APIID isn't conform. (Usage: UserId/UserTaskId)");
        }

        return new HiddenUserTaskDatastore(getEngineSession()).get(id);

    }

    @Override
    public HiddenUserTaskItem add(final HiddenUserTaskItem item) {
        return new HiddenUserTaskDatastore(getEngineSession()).add(item);
    }

    @Override
    public void delete(final List<APIID> ids) {
        new HiddenUserTaskDatastore(getEngineSession()).delete(ids);
    }

    @Override
    protected void fillDeploys(final HiddenUserTaskItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final HiddenUserTaskItem item, final List<String> counters) {
    }
}
