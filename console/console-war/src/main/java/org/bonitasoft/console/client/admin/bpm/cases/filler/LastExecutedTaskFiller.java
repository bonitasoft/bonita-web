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
package org.bonitasoft.console.client.admin.bpm.cases.filler;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

/**
 * @author Bastien Rohart
 * @author Colin PUY
 * 
 */
public class LastExecutedTaskFiller extends Filler<Text> {

    private static final String ORDER_BY_REACHED_STATE_DATE_DESC = ArchivedHumanTaskItem.ATTRIBUTE_REACHED_STATE_DATE + " DESC";

    private final CaseItem caseItem;

    public LastExecutedTaskFiller(final CaseItem caseItem) {
        this.caseItem = caseItem;
    }

    @Override
    protected void getData(final APICallback callback) {
        Map<String, String> filter = buildArchivedHumanTaskStateCompletedForCaseIdFilter(caseItem.getId());
        getArchivedHumanTaskAPICaller().search(0, 1, ORDER_BY_REACHED_STATE_DATE_DESC, null, filter, callback);
    }

    private Map<String, String> buildArchivedHumanTaskStateCompletedForCaseIdFilter(APIID caseId) {
        return MapUtil.asMap(new Arg(ArchivedHumanTaskItem.ATTRIBUTE_CASE_ID, caseId),
                new Arg(ArchivedHumanTaskItem.ATTRIBUTE_STATE, ArchivedHumanTaskItem.VALUE_STATE_COMPLETED));
    }

    private APICaller<? extends IHumanTaskItem> getArchivedHumanTaskAPICaller() {
        return ArchivedHumanTaskDefinition.get().getAPICaller();
    }

    @Override
    protected void setData(final String json, final Map<String, String> headers) {
        List<IItem> archivedHumanTasks = JSonItemReader.parseItems(json, ArchivedHumanTaskDefinition.get());
        if (!archivedHumanTasks.isEmpty()) {
            ArchivedHumanTaskItem archivedHumanTask = (ArchivedHumanTaskItem) archivedHumanTasks.get(0);
            target.getElement().setInnerText(DateFormat.dateToDisplay(archivedHumanTask.getReachStateDate()));
        }
    }
}
