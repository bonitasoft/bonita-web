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
package org.bonitasoft.web.rest.server.datastore.bpm.flownode;

import java.util.Map;

import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.FlowNodeType;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.UserTaskInstance;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.flownode.UserTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.UserTaskItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
public class AbstractUserTaskDatastore<CONSOLE_ITEM extends UserTaskItem, ENGINE_ITEM extends UserTaskInstance>
        extends AbstractHumanTaskDatastore<CONSOLE_ITEM, ENGINE_ITEM> {

    public AbstractUserTaskDatastore(final APISession engineSession) {
        super(engineSession);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // C.R.U.D.S
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    @Override
    public CONSOLE_ITEM get(final APIID id) {
        try {

            // FIXME replace by getUserTaskInstance
            final HumanTaskInstance humanTaskInstance = getProcessAPI().getHumanTaskInstance(id.toLong());

            if (!(humanTaskInstance instanceof UserTaskInstance)) {
                throw new APIItemNotFoundException("User task", id);
            }

            return convertEngineToConsoleItem((ENGINE_ITEM) humanTaskInstance);
        } catch (final ActivityInstanceNotFoundException e) {
            throw new APIItemNotFoundException(UserTaskDefinition.TOKEN, id);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    protected SearchOptionsBuilder makeSearchOptionBuilder(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        final SearchOptionsBuilder builder = super.makeSearchOptionBuilder(page, resultsByPage, search, orders, filters);

        builder.filter(ActivityInstanceSearchDescriptor.ACTIVITY_TYPE, FlowNodeType.USER_TASK);

        return builder;
    }
}
