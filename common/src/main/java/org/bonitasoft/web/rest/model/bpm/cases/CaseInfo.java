/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.bpm.cases;

import java.util.Map;

import org.bonitasoft.engine.api.FlownodeCounters;

/**
 * @author Julien Reboul
 *
 */
public class CaseInfo {

    private long id;

    private Map<String, FlownodeCounters> flowNodeStatesCounters;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the counters
     */
    public Map<String, FlownodeCounters> getFlowNodeStatesCounters() {
        return flowNodeStatesCounters;
    }

    /**
     * @param counters
     *            the counters to set
     */
    public void setFlowNodeStatesCounters(final Map<String, FlownodeCounters> counters) {
        flowNodeStatesCounters = counters;
    };

}
