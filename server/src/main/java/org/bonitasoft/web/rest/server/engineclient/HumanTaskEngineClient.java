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
package org.bonitasoft.web.rest.server.engineclient;


import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;

/**
 * @author Colin PUY
 * @author Elias Ricken de Medeiros
 *
 */
public class HumanTaskEngineClient {

    private final ProcessAPI processAPI;

    public HumanTaskEngineClient(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    public long countOpenedHumanTasks() {
        final SearchOptions search = new SearchOptionsBuilder(0, 0)
                .filter(HumanTaskInstanceSearchDescriptor.STATE_NAME, HumanTaskItem.VALUE_STATE_READY).done();
        try {
            return processAPI.searchHumanTaskInstances(search).getCount();
        } catch (final Exception e) {
            throw new APIException("Error when counting opened cases", e);
        }
    }

}
