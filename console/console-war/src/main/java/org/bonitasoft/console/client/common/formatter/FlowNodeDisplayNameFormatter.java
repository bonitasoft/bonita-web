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
package org.bonitasoft.console.client.common.formatter;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeItem;
import org.bonitasoft.web.toolkit.client.ui.component.Span;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.ItemTableCellFormatter;

/**
 * Formatter for archived Display Name
 * Add different CSS Class for the Type
 * 
 * @author Julien MEGE
 */
public class FlowNodeDisplayNameFormatter extends ItemTableCellFormatter {

    @Override
    public void execute() {
        FlowNodeItem task = (FlowNodeItem) getItem();
        this.table.addCell(newPrefixSpan(_("Task name:")).addClass(task.getType().toLowerCase()), new Span(getText()));
    }

    private String getText() {
        return this.attributeReader.read(this.item);
    }

    protected Component newPrefixSpan(String prefix) {
        return new Span(prefix).addClass("prepend");
    }
}
