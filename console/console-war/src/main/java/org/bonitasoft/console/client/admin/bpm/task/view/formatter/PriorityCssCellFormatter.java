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
package org.bonitasoft.console.client.admin.bpm.task.view.formatter;

import org.bonitasoft.web.rest.api.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.api.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.api.model.bpm.flownode.TaskDefinition;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.DefaultItemTableCellFormatter;


/**
 * Cell Formatter to add css class in priority cell for table using {@link TaskDefinition} (and not {@link HumanTaskDefinition})  
 * @author Colin PUY
 */
public class PriorityCssCellFormatter extends DefaultItemTableCellFormatter {

    @Override
    public void execute() {
        super.execute();
        this.table.getLastCell().addClass(HumanTaskItem.ATTRIBUTE_PRIORITY + "_" + getStringText());
    }
}
