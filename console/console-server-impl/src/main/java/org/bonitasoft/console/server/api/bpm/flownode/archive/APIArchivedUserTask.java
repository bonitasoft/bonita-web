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
package org.bonitasoft.console.server.api.bpm.flownode.archive;

import java.util.List;

import org.bonitasoft.console.client.model.bpm.flownode.ArchivedUserTaskDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.ArchivedUserTaskItem;
import org.bonitasoft.console.server.api.bpm.flownode.AbstractAPIUserTask;
import org.bonitasoft.console.server.datastore.bpm.flownode.archive.ArchivedUserTaskDatastore;

/**
 * @author Séverin Moussel
 * 
 */
public class APIArchivedUserTask extends AbstractAPIUserTask<ArchivedUserTaskItem> {

    @Override
    protected ArchivedUserTaskDefinition defineItemDefinition() {
        return new ArchivedUserTaskDefinition();
    }

    @Override
    protected ArchivedUserTaskDatastore defineDefaultDatastore() {
        return new ArchivedUserTaskDatastore(getEngineSession());
    }

    @Override
    protected List<String> defineReadOnlyAttributes() {
        final List<String> attributes = super.defineReadOnlyAttributes();

        attributes.add(ArchivedUserTaskItem.ATTRIBUTE_ARCHIVED_DATE);

        return attributes;
    }
}
