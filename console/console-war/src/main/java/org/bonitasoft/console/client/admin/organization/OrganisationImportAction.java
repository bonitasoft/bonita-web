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

import java.util.Map;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.callback.HttpCallback;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.ClosePopUpAction;
import org.bonitasoft.web.toolkit.client.ui.action.form.SendFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;

public class OrganisationImportAction extends SendFormAction {

    private static final String IMPORT_REST_API_URL = "../services/organization/import";
    
    private final static HttpCallback callBack = new HttpCallback() {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            ViewController.getInstance().showPopup(new Page() {
                
                @Override
                public String defineToken() {
                    return "confirmationpopup";
                }
                
                @Override
                public void buildView() {
                    addBody(new Paragraph("Organization succesfully imported."));
                    addBody(new Button("OK", "ok", new ClosePopUpAction()));
                    
                }

                @Override
                public void defineTitle() {
                    this.setTitle("Info");
                    
                }
            });
        }

    };

    public OrganisationImportAction() {
        super(IMPORT_REST_API_URL, callBack);
    }
    
    

}
