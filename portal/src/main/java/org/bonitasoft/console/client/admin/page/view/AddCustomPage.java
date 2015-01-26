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
import org.bonitasoft.console.client.common.view.CustomPageWithFrame;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.AbstractStringFormatValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FileUpload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

/**
 * @author Anthony Birembaut
 *
 */
public class AddCustomPage extends Page {

    public static final String TOKEN = "PageUpload";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(PageListingPage.TOKEN);
    }

    public static final JsId FILE_ENTRY_JS_ID = new JsId("pageZip");

    public static final JsId INSTALL_BUTTON_JS_ID = new JsId("installUpload");

    public static final String UPLOAD_SERVLET_VALID_RESPONSE_REGEXP = ".*\\.(zip):.*$";

    protected static final String PAGE_UPLOAD_URL = GWT.getModuleBaseURL() + "pageUpload";

    protected static final String ACTION_ADD = "?action=add";

    protected static final String TOKEN_DESCRIPTION = _("Format: %namePrefix% followed only by alphanumeric characters.", new Arg("namePrefix",
            CustomPageWithFrame.TOKEN));

    private static final String POPUP_CONTEXT_MESSAGE = _("Add a new page that was created using Groovy and/or HTML.<br/>The page archive (.zip file) must contain page.properties, either the index.html or the Index.groovy file, and the resources needed for the page.<br/>For details, see the documentation or the example page readme (available in the custom page list).");

    @Override
    public void defineTitle() {
        this.setTitle(_("Add a custom page"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        addBody(new UiComponent(new HTML(POPUP_CONTEXT_MESSAGE)));
        final Form form = new Form(new JsId("pageUploadForm"));
        final FileUpload pageUploader = new FileUpload(PAGE_UPLOAD_URL + ACTION_ADD, FILE_ENTRY_JS_ID, _("Page archive"),
                _("A page archive has a .zip extension"));
        form.addEntry(pageUploader);
        form.addButton(INSTALL_BUTTON_JS_ID, _("Next >"), _("Add a page"), new OpenValidationPopupAction(pageUploader)).addCancelButton();
        pageUploader.addValidator(new AbstractStringFormatValidator(UPLOAD_SERVLET_VALID_RESPONSE_REGEXP) {

            @Override
            protected String defineErrorMessage() {
                return _("This file format is not allowed. Only .zip files are allowed.");

            }
        });
        pageUploader.addValidator(new MandatoryValidator());
        pageUploader.addFilter(new PageUploadFilter());
        addBody(form);
    }
}
