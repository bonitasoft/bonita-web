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

import java.util.Map;

import org.bonitasoft.web.rest.api.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.api.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.api.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.ApiSearchResultPager;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

/**
 * @author Séverin Moussel
 * 
 */
public class FailedTasksFiller extends Filler<Link> {

    private final ProcessItem process;

    public FailedTasksFiller(final ProcessItem process) {
        this.process = process;
    }

    @Override
    protected void getData(final APICallback callback) {
        HumanTaskDefinition.get().getAPICaller().search(0, 0, null, null,
                MapUtil.asMap(
                        new Arg(HumanTaskItem.ATTRIBUTE_PROCESS_ID, this.process.getId()),
                        new Arg(HumanTaskItem.ATTRIBUTE_STATE, HumanTaskItem.VALUE_STATE_FAILED)
                        ), callback);
    }

    @Override
    protected void setData(final String json, final Map<String, String> headers) {
        final ApiSearchResultPager resultPager = ApiSearchResultPager.parse(headers);
        this.target.setLabel(String.valueOf(resultPager.getNbTotalResults()));
        if (resultPager.getNbTotalResults() == 0) {
            this.target.setEnabled(false);
        }
    }
}