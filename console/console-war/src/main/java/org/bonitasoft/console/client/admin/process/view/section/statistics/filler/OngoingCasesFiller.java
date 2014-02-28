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
package org.bonitasoft.console.client.admin.process.view.section.statistics.filler;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.ApiSearchResultPager;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

/**
 * @author Séverin Moussel
 * 
 */
public class OngoingCasesFiller extends Filler<Link> {

    private final ProcessItem process;

    public OngoingCasesFiller(final ProcessItem process) {
        this.process = process;
    }

    @Override
    protected void getData(final APICallback callback) {
        new APICaller(CaseDefinition.get()).search(0, 0, null, null,
                MapUtil.asMap(new Arg(CaseItem.ATTRIBUTE_PROCESS_ID, this.process.getId())), callback);
    }

    @Override
    protected void setData(final String json, final Map<String, String> headers) {
        final ApiSearchResultPager resultPager = ApiSearchResultPager.parse(headers.get("Content-Range"));
        this.target.setLabel(_("%nb_ongoing% ongoing", new Arg("nb_ongoing", resultPager.getNbTotalResults())));
        if (resultPager.getNbTotalResults() == 0) {
            this.target.setEnabled(false);
        }
    }
}
