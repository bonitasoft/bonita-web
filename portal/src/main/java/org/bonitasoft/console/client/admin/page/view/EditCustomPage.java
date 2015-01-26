/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.admin.page.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.admin.page.action.OpenValidationPopupAction;
import org.bonitasoft.web.rest.model.portal.page.PageDefinition;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.AbstractStringFormatValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormFiller;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FileUpload;

/**
 * @author Anthony Birembaut
 *
 */
public class EditCustomPage extends Page {

    public static final String TOKEN = "PageUpdate";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(PageListingPage.TOKEN);
    }

    public static final String PARAMETER_PAGE_ID = "pageId";

    public static final String PARAMETER_PAGE_DISPLAY_NAME = "pageDisplayName";

    protected static final String ACTION_EDIT = "?action=edit";

    private static final String POPUP_CONTEXT_MESSAGE = _("Saving a page will change it immediately for all users who can view it.");

    public EditCustomPage() {
        super();
    }

    public EditCustomPage(final APIID pageId, final String pageDisplayName) {
        addParameter(PARAMETER_PAGE_ID, pageId.toString());
        addParameter(PARAMETER_PAGE_DISPLAY_NAME, pageDisplayName);
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Edit %pageName%", new Arg("pageName", getParameter(PARAMETER_PAGE_DISPLAY_NAME))));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        final APIID pageId = APIID.makeAPIID(getParameter(PARAMETER_PAGE_ID));
        final String pageDisplayName = getParameter(PARAMETER_PAGE_DISPLAY_NAME);

        addBody(new Text(POPUP_CONTEXT_MESSAGE));
        final Form form = new Form(new JsId("pageUploadForm"));
        final FileUpload pageUploader = new FileUpload(AddCustomPage.PAGE_UPLOAD_URL + ACTION_EDIT, AddCustomPage.FILE_ENTRY_JS_ID, _("Page archive"),
                _("A page archive has a .zip extension"), _("To change the content of this custom page, specify a new zip file in the field below."));
        form.addEntry(pageUploader);
        form.addButton(AddCustomPage.INSTALL_BUTTON_JS_ID, _("Next >"), _("Update the page"),
                new OpenValidationPopupAction(pageUploader, pageId, pageDisplayName))
                .addCancelButton();
        pageUploader.addValidator(new AbstractStringFormatValidator(AddCustomPage.UPLOAD_SERVLET_VALID_RESPONSE_REGEXP) {

            @Override
            protected String defineErrorMessage() {
                return _("This file format is not allowed. Only .zip files are allowed.");

            }
        });
        //
        pageUploader.addValidator(new MandatoryValidator());
        pageUploader.addFilter(new PageUploadFilter());
        form.addFiller(new FormFiller() {

            @Override
            protected void getData(final APICallback callback) {
                new APICaller(PageDefinition.get()).get(pageId, callback);
            }
        });
        addBody(form);
    }

}
