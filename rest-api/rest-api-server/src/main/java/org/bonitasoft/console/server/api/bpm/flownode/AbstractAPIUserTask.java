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
package org.bonitasoft.console.server.api.bpm.flownode;

import org.bonitasoft.console.server.datastore.bpm.flownode.UserTaskDatastore;
import org.bonitasoft.web.rest.api.model.bpm.flownode.IUserTaskItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.UserTaskDefinition;
import org.bonitasoft.web.toolkit.server.api.Datastore;

/**
 * @author Séverin Moussel
 * 
 */
public class AbstractAPIUserTask<ITEM extends IUserTaskItem> extends AbstractAPIHumanTask<ITEM> {

    @Override
    protected UserTaskDefinition defineItemDefinition() {
        return UserTaskDefinition.get();
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new UserTaskDatastore(getEngineSession());
    }

}
