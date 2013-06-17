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
package org.bonitasoft.console.client.admin.process.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.api.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.api.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.FileExtensionAllowedValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.form.SendFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.BarUploadFilter;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FileUpload;

import com.google.gwt.core.client.GWT;

/**
 * @author Haojie Yuan
 * 
 */
public class UploadProcessPage extends Page {

    public static final String TOKEN = "ProcessUpload";

    public static final String URL_API = "../API/bpm/process";

    private Form form;

    private final JsId fileEntryJsId = new JsId("fileupload");

    @Override
    public void defineTitle() {
        this.setTitle(_("Install an app"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        addBody(new Text(_("Browse your computer and select the business archive containing the app definition to install.")));

        this.form = new Form(new JsId("uploadForm"))
                .addFileEntry(this.fileEntryJsId, "Business archive", _("A business archive has a .bar extension"),
                        GWT.getModuleBaseURL() + "processUpload")

                .addValidator(this.fileEntryJsId, new FileExtensionAllowedValidator("bar"))
                .addValidator(this.fileEntryJsId, new MandatoryValidator())

                .addButton(new JsId("installUpload"), _("Install"), _("Install a app"), new SendFormAction(URL_API, new ProcessInstallCallback()))
                .addCancelButton();

        // add upload filter
        ((FileUpload) this.form.getEntry(this.fileEntryJsId)).addFilter(new BarUploadFilter());

        addBody(this.form);

    }

    private final class ProcessInstallCallback extends APICallback {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final ProcessItem p = (ProcessItem) JSonItemReader.parseItem(response, ProcessDefinition.get());
            final HashMap<String, String> params = new HashMap<String, String>();
            params.put("id", p.getAttributeValue("id"));
            ViewController.showView(ProcessMoreDetailsAdminPage.TOKEN, params);
        }

        @Override
        public void onError(final String message, final Integer errorCode) {
            final ServerException ex = parseException(message, errorCode);

            if ("class org.bonitasoft.engine.exception.InvalidBusinessArchiveFormat".equals(ex.getOriginalClassName())) {
                UploadProcessPage.this.form.addError(UploadProcessPage.this.fileEntryJsId,
                        _("The archive file can't be read. This can be due to a corrupted file or a file created with another version of Bonita."));
            } else if ("class org.bonitasoft.engine.exception.ObjectAlreadyExistsException".equals(ex.getOriginalClassName())) {
                UploadProcessPage.this.form.addError(UploadProcessPage.this.fileEntryJsId,
                        _("This app has already been installed."));
            } else {
                super.onError(message, errorCode);
            }
        }
    }

}
