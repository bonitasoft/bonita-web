/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.organization;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.api.callback.HttpCallback;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.form.SendFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.AbstractForm;

public class OrganisationImportAction extends SendFormAction {

    private static final String IMPORT_REST_API_URL = "../services/organization/import";
    
    private static AbstractForm form;
    
    private final static HttpCallback callBack = new HttpCallback() {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            form.addError(new JsId("organizationDataUpload"), _("Organization successfully imported."));
            form.addClass("success");
        }

    };

    
    @Override
    public void execute() {
        // TODO Auto-generated method stub
        super.execute();
        form = this.getForm();
    }

    @Override
    public void setOnError(Action onError) {
        // TODO Auto-generated method stub
        super.setOnError(onError);
        form.removeClass("success");
    }

    public OrganisationImportAction() {
        super(IMPORT_REST_API_URL, callBack);
    }
    
    

}
