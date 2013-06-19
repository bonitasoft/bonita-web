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

import org.bonitasoft.web.rest.model.bpm.flownode.ITaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskDefinition;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.TaskDatastore;
import org.bonitasoft.web.toolkit.server.api.Datastore;

/**
 * @author Séverin Moussel
 * 
 */
public class AbstractAPITask<ITEM extends ITaskItem> extends AbstractAPIActivity<ITEM> {

    @Override
    protected TaskDefinition defineItemDefinition() {
        return TaskDefinition.get();
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new TaskDatastore(getEngineSession());
    }

}
