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
package org.bonitasoft.console.client.admin.process.view.section.statistics;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.section.statistics.filler.ArchivedCasesFiller;
import org.bonitasoft.console.client.admin.process.view.section.statistics.filler.FailedTasksFiller;
import org.bonitasoft.console.client.admin.process.view.section.statistics.filler.OngoingCasesFiller;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Section;

/**
 * @author Colin PUY
 *
 */
public class StatisticsSection extends Section {

    public StatisticsSection(ProcessItem process) {
        super(new JsId("statistics"), _("Statistics"));
        setId(CssId.QD_SECTION_PROCESS_STATISTICS);
        addBody(numberOfCasesDefinition(process));
        addBody(failedTasksDefinition(process));
    }

    private Definition failedTasksDefinition(ProcessItem process) {
        return new Definition(_("Tasks in failed status: %nb_tasks%", new Arg("nb_tasks", "")), "%%", nbFailedTasksLink(process));
    }

    private Link nbFailedTasksLink(ProcessItem process) {
        Link nbFailedTasks = new Link(new JsId("nbfailedTasks"), _("?"), _("The number of failed tasks."), newShowTaskListingAction());
        nbFailedTasks.addFiller(new FailedTasksFiller(process));
        return nbFailedTasks;
    }

    protected Action newShowTaskListingAction() {
        return new RedirectionAction(TaskListingAdminPage.TOKEN, getParamMap("_f", TaskListingAdminPage.FILTER_PRIMARY_FAILED));
    }

    private Definition numberOfCasesDefinition(ProcessItem process) {
        return new Definition(_("Number of cases: %nb_cases%", new Arg("nb_cases", "")), "%% / %%",
                nbCasesOngoingLink(process), nbCasesArchivedLink(process));
    }

    private Link nbCasesArchivedLink(ProcessItem process) {
        final Link nbCasesArchived = new Link(new JsId("nbCasesArchived"), _("%nb_archived% archived", new Arg("nb_archived", "?")),
                _("The number of archived cases."), newShowArchivedCaseListingAction());
        nbCasesArchived.addFiller(new ArchivedCasesFiller(process));
        return nbCasesArchived;
    }

    private Link nbCasesOngoingLink(ProcessItem process) {
        final Link nbCasesOngoing = new Link(new JsId("nbCasesOngoing"), _("%nb_ongoing% ongoing", new Arg("nb_ongoing", "?")),
                _("The number of on going cases."), newShowCaseListingAction(process));
        nbCasesOngoing.addFiller(new OngoingCasesFiller(process));
        return nbCasesOngoing;
    }

    protected Action newShowArchivedCaseListingAction() {
        return new RedirectionAction(CaseListingAdminPage.TOKEN, getParamMap("_f", CaseListingAdminPage.FILTER_SECONDARY_ARCHIVED_CASES));
    }

    protected Action newShowCaseListingAction(ProcessItem process) {
        return new RedirectionAction(CaseListingAdminPage.TOKEN, getParamMap("_fid", process.getId().toString()));
    }

    protected Map<String, String> getParamMap(final String name, final String value) {
        final HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(name, value);
        return paramMap;
    }
}
