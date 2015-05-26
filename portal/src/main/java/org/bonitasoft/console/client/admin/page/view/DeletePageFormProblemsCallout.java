/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.page.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.callout.CalloutInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Callout listing living Applications using the page to delete
 *
 * @author Julien MEGE
 */
public class DeletePageFormProblemsCallout extends CalloutInfo {

    public DeletePageFormProblemsCallout(final JSONArray formMappings, String pageName) {
        super(_("%pageName% is used by:", new Arg("pageName", pageName)));
        List<String> processDefinitionIds = new ArrayList<String>();
        for (int i=0; i < formMappings.size(); ++i) {
            JSONObject item=formMappings.get(i).isObject();
            JSONValue value = item.get("processDefinitionId");
            JSONValue type = item.get("type");
            JSONValue taskName = item.get("task");
            String processDefinitionId = value.isString().stringValue();
            if (!processDefinitionIds.contains(processDefinitionId)) {
                processDefinitionIds.add(processDefinitionId);
                new APICaller(ProcessDefinition.get()).get(processDefinitionId, new GetProcessNameCallback());
            }
        }
    }

    private class GetProcessNameCallback extends APICallback {
        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final ProcessItem process = JSonItemReader.parseItem(response, ProcessDefinition.get());
            append(new Paragraph(process.getDisplayName() + " (" + process.getVersion() + ") "));
        }
    }
}
