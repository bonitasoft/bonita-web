/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.admin.bpm.task.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.bpm.accessor.IActivityAccessor;
import org.bonitasoft.console.client.common.component.snippet.SectionSnippet;
import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

/**
 * @author Vincent Elcrin
 *
 */
public class TaskTechnicalInformationSnippet implements SectionSnippet {

    protected final IActivityAccessor activity;

    /**
     * Default Constructor.
     *
     * Need {@link ActivityItem#ATTRIBUTE_EXECUTED_BY_USER_ID} to be deployed!
     */
    public TaskTechnicalInformationSnippet(final IActivityAccessor activity) {
        this.activity = activity;
    }

    @Override
    public Section build() {
        String template = "";

        final Arg executedByArg = buildExecutedByArg(this.activity);

        if (TaskItem.VALUE_STATE_READY.equals(this.activity.getState())) {
            template = _("Ready since %task_last_update%",
                    new Arg("task_last_update", DateFormat.dateToDisplayShort(this.activity.getLastUpdateDate())));

        } else if (TaskItem.VALUE_STATE_COMPLETED.equals(this.activity.getState())) {
            template = _("Done %task_last_update% by %executed_by%",
                    new Arg("task_last_update", DateFormat.dateToDisplayShort(this.activity.getLastUpdateDate())),
                    executedByArg);

        } else if (TaskItem.VALUE_STATE_FAILED.equals(this.activity.getState())) {
            template = _("Failed on %task_last_update%",
                    new Arg("task_last_update", DateFormat.dateToDisplay(this.activity.getLastUpdateDate())));

        } else if (TaskItem.VALUE_STATE_SKIPPED.equals(this.activity.getState())) {
            template = _("Skipped on %task_last_update% by %executed_by%",
                    new Arg("task_last_update", DateFormat.dateToDisplayShort(this.activity.getLastUpdateDate())),
                    executedByArg);
        }

        return new Section(_("Technical details"))
                .addBody(new Text(template));
    }

    private Arg buildExecutedByArg(final IActivityAccessor activity) {
        return new Arg("executed_by", DeployedUserReader
                .readUser(activity.getItem(),
                        IFlowNodeItem.ATTRIBUTE_EXECUTED_BY_USER_ID));
    }

}
