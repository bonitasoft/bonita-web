/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.admin.bpm.task.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.bpm.accessor.IActivityAccessor;
import org.bonitasoft.console.client.common.component.snippet.SectionSnippet;
import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

/**
 * @author Julien Mege
 *
 */
public class ArchivedTaskTechnicalInformationSnippet implements SectionSnippet {

    protected final IActivityAccessor activity;

    /**
     * Default Constructor.
     *
     * Need {@link ActivityItem#ATTRIBUTE_EXECUTED_BY_USER_ID} to be deployed!
     */
    public ArchivedTaskTechnicalInformationSnippet(final IActivityAccessor activity) {
        this.activity = activity;
    }

    @Override
    public Section build() {
        String template = "";

        final Arg executedByArg = buildExecutedByArg(activity);

        if (TaskItem.VALUE_STATE_COMPLETED.equals(activity.getState())) {
            template = _("Done %task_last_update% by %executed_by%",
                    new Arg("task_last_update", DateFormat.dateToDisplayShort(activity.getLastUpdateDate())),
                    executedByArg);

        } else if (TaskItem.VALUE_STATE_SKIPPED.equals(activity.getState())) {
            template = _("Skipped on %task_last_update% by %executed_by%",
                    new Arg("task_last_update", DateFormat.dateToDisplayShort(activity.getLastUpdateDate())),
                    executedByArg);
        }

        final Section techinicalDetailsSection = new Section(_("Technical details"));
        techinicalDetailsSection.setId(CssId.QD_SECTION_TECHNICAL_DETAILS);
        return techinicalDetailsSection
                .addBody(new Text(template));
    }

    private Arg buildExecutedByArg(final IActivityAccessor activity) {
        return new Arg("executed_by", DeployedUserReader.readUser(activity.getItem(), IFlowNodeItem.ATTRIBUTE_EXECUTED_BY_USER_ID));
    }

}
