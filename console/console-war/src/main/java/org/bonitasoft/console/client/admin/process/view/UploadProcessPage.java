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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.callback.HttpCallback;
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
 * Page containing a form to upload a process and install it
 */
public class UploadProcessPage extends Page {

    public static final String TOKEN = "processupload";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
    }

    private HttpCallback httpCallback;

    public UploadProcessPage() {
        setCallBack(new ProcessInstallCallback());
    }

    private HttpCallback httpCallback;

    public UploadProcessPage() {
        setCallBack(new ProcessInstallCallback());
    }

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
        addBody(uploadProcessForm());
    }

    private Form uploadProcessForm() {
        Form form = new Form();
        form.addEntry(uploadProcessFileUpload());
        form.addDisabledButton(new JsId("installUpload"), _("Install"), _("Install a app"),
                new SendFormAction(ProcessDefinition.get().getAPIUrl(), getCallBack()));
        form.addCancelButton();
        return form;
    }

    private HttpCallback getCallBack() {
        return httpCallback;
    }

    public void setCallBack(HttpCallback callBack) {
        httpCallback = callBack;
    }

    private FileUpload uploadProcessFileUpload() {
        FileUpload fileUpload = new FileUpload(GWT.getModuleBaseURL() + "processUpload", new JsId("fileupload"),
                "Business archive", _("A business archive has a .bar extension"));
        fileUpload.addFilter(new BarUploadFilter());
        fileUpload.addValidator(new FileExtensionAllowedValidator("bar"));
        fileUpload.addValidator(new MandatoryValidator());
        return fileUpload;
    }

    /**
     * APICallback for process installation.
     * 
     * Redirect to process more view on installation succ
     * Show an error pop-up on installation failure
     */
    private final class ProcessInstallCallback extends APICallback {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            ProcessItem process = JSonItemReader.parseItem(response, ProcessDefinition.get());
            ViewController.showView(new ProcessMoreDetailsAdminPage(process));
        }
    }
}
