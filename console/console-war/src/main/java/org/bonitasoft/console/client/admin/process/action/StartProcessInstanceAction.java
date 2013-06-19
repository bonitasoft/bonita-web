/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.client.admin.process.action;

import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ItemAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

import com.google.gwt.user.client.Window;

/**
 * @author Zhiheng Yang, Haojie Yuan
 */
@Deprecated
public class StartProcessInstanceAction extends ItemAction {

    private String id;

    @Deprecated
    public StartProcessInstanceAction() {
        super(Definitions.get(CaseDefinition.TOKEN));
    }

    @Deprecated
    public StartProcessInstanceAction(final String id) {
        super(Definitions.get(CaseDefinition.TOKEN));
        this.id = id;
    }

    @Override
    @Deprecated
    public void execute() {
        if (this.getParameter("id") != null) {
            this.id = this.getParameter("id");
        }
        final Form form = new Form(new JsId("startProcess"));
        form.addHiddenEntry("process_id", this.id);
        new APICaller<ProcessItem>(Definitions.get(CaseDefinition.TOKEN)).add(form, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                Window.alert("Start Success!");
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                Window.alert(message);
            }
        });

    }

}
