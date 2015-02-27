/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.technicaluser.businessdata;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.tenant.view.TenantMaintenancePage;
import org.bonitasoft.console.client.common.component.section.WarningCell;
import org.bonitasoft.web.rest.model.system.TenantAdminDefinition;
import org.bonitasoft.web.rest.model.system.TenantAdminItem;
import org.bonitasoft.web.rest.model.tenant.BusinessDataModelItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.FileExtensionAllowedValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.callout.Callout;
import org.bonitasoft.web.toolkit.client.ui.component.callout.CalloutDanger;
import org.bonitasoft.web.toolkit.client.ui.component.callout.CalloutWarning;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormSubmitButton;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FileUpload;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.ZipUploadFilter;

import com.google.gwt.core.client.GWT;

public class BDMImportPage extends Page {

    public static final String BDM_FILE_EXTENSION = "zip";
    public static final String TOKEN = "businessdatamodelimport";

    private FileUpload fileUpload;
    private FormSubmitButton formSubmitButton;
    private Callout pauseServicesCallout;
    private Callout resumeServicesCallout;

    public static final List<String> PRIVILEGES = new ArrayList<String>();
    static {
        PRIVILEGES.add(BDMImportPage.TOKEN);
    }

    public BDMImportPage() {
        addClass(CssClass.NO_FILTER_PAGE);
    }

    @Override
    public void defineTitle() {
        setTitle(_("Business Data Model"));
    }

    @Override
    public void buildView() {
        pauseServicesCallout = pauseServicesCallout();

        resumeServicesCallout = resumeServicesCallout();
        resumeServicesCallout.setVisible(false);

        fileUpload = uploadBdmFileUpload();
        formSubmitButton = buildFormSubmitButton();

        Section section = new Section(new JsId("import"), _("Import and activate a new Business Data Model"));
        section.setId(CssId.SECTION_IMPORT_BDM);
        section.addHeader(pauseServicesCallout);
        section.addHeader(resumeServicesCallout);
        section.addBody(new WarningCell());
        section.addBody(importContainer(fileUpload, formSubmitButton));

        addBody(section);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        APIRequest.get(TenantAdminDefinition.UNUSED_ID, TenantAdminDefinition.get(), new FormStateCallBack()).run();
    }

    /**
     * Enable form if tenant is paused. Disable form and print callout if tenant is not paused
     */
    private class FormStateCallBack extends APICallback {

        @Override
        public void onSuccess(int httpStatusCode, String response, Map<String, String> headers) {
            TenantAdminItem tenant = JSonItemReader.parseItem(response, TenantAdminDefinition.get());
            if (tenant.isPaused()) {
                enableForm();
            } else {
                disableForm();
            }
        }

        private void disableForm() {
            pauseServicesCallout.setVisible(true);
            resumeServicesCallout.setVisible(false);
            fileUpload.disable();
            formSubmitButton.setEnabled(false);
        }

        private void enableForm() {
            pauseServicesCallout.setVisible(false);
            resumeServicesCallout.setVisible(true);
            fileUpload.enable();
            formSubmitButton.setEnabled(true);
        }
    }

    private Callout pauseServicesCallout() {
        Text text = new Text(_("The BPM Services must be paused before importing a new Business Data Model."));
        return new CalloutDanger(_("The BPM Services must be paused"), text, newGoToBpmServicesLink());
    }

    private Callout resumeServicesCallout() {
        Text text = new Text(_("When you are done, the BPM Services must be resumed to allow users to log in again."));
        return new CalloutWarning(_("Resume BPM Services"), text, newGoToBpmServicesLink());
    }

    private Link newGoToBpmServicesLink() {
        return new Link(_("Go to BPM Services"), _("Go to Configuration > BPM Services."), new ActionShowView(new TenantMaintenancePage()));
    }

    private ContainerStyled<Component> importContainer(FileUpload fileUpload, FormSubmitButton formSubmitButton) {
        ContainerStyled<Component> container = new ContainerStyled<Component>();
        container.addClass(CssClass.CELL);
        container.append(new Paragraph(_("Only one Business Data Model can be active at a time.")));
        container.append(new Paragraph(_("Make sure all your processes are compatible with the new Business Data Model.")));
        container.append(uploadForm(fileUpload, formSubmitButton));
        return container;
    }

    private Form uploadForm(FileUpload fileUpload, FormSubmitButton formSubmitButton) {
        Form form = new Form();
        form.addEntry(fileUpload);
        form.addButton(formSubmitButton);
        return form;
    }

    private FormSubmitButton buildFormSubmitButton() {
        return new FormSubmitButton(new JsId("bdmUpload"), _("Activate"), _("Activate a BDM"), new ActivateBDMAction());
    }

    private FileUpload uploadBdmFileUpload() {
        FileUpload fileUpload = new FileUpload(GWT.getModuleBaseURL() + "fileUpload", new JsId(BusinessDataModelItem.ATTRIBUTE_FILE_UPLOAD_PATH),
                _("Business Data Model"), _("A Business Data Model has a .%fileExtension% extension", new Arg("fileExtension", BDM_FILE_EXTENSION)));
        fileUpload.addValidator(new FileExtensionAllowedValidator(BDM_FILE_EXTENSION));
        fileUpload.addValidator(new MandatoryValidator());
        fileUpload.addFilter(new ZipUploadFilter());
        return fileUpload;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
