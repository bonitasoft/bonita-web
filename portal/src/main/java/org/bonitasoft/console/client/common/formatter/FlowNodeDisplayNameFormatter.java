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
import org.bonitasoft.web.toolkit.client.ui.component.Html;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Formatter for FlowNode Display Name
 * Add different CSS Class and tooltip
 * 
 * @author Julien MEGE
 */
public class FlowNodeDisplayNameFormatter extends FlowNodeCellFormatter {

    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template(
                "<span title='{2}' class='{1} prepend'>{0}</span>"
                        + "<span>{3}</span>")
                SafeHtml cell(String preFix, String cssClass, String tooltip, String displayName);

    }

    private Templates TEMPLATES = GWT.create(Templates.class);

    @Override
    public void execute() {
        FlowNodeItem task = (FlowNodeItem) getItem();
        this.table.addCell(new Html(TEMPLATES.cell(_("Task name:"), task.getType().toLowerCase(), getTooltip(), getText())));
    }

}
