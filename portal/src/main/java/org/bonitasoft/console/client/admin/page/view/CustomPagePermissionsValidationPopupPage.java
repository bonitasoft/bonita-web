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

import java.util.HashSet;

import org.bonitasoft.console.client.admin.page.action.PageInstallCallback;
import org.bonitasoft.console.client.admin.page.action.UpdatePageAction;
import org.bonitasoft.web.rest.model.portal.page.PageDefinition;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.form.SendFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.callout.Callout;
import org.bonitasoft.web.toolkit.client.ui.component.callout.CalloutDanger;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.StaticText;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Element;


/**
 * @author Fabio Lombardi
 *
 */
public class CustomPagePermissionsValidationPopupPage extends Page {

    private static final String PAGE_ZIP = "pageZip";

    public static final String TOKEN = "customPagePermissionsValidationPopup";

    public static final JsId PAGE_UPLOAD_FORM_JS_ID = new JsId("pageUploadForm");

    public static final JsId INSTALL_BUTTON_JS_ID = new JsId("installUpload");

    public static final JsId ERROR_JS_ID = new JsId("formError");

    public static final JsId BACK_BUTTON_JS_ID = new JsId("backButton");

    public static final String RESOURCES_REGEXP = "<.*>";

    private String uploadValue = null;

    private APIID pageId = null;

    private String pageDisplayName;

    public CustomPagePermissionsValidationPopupPage() {

    }

    public CustomPagePermissionsValidationPopupPage(final String uploadValue, final APIID pageId, final String pageDisplayName) {
        this.uploadValue = uploadValue;
        this.pageId = pageId;
        this.pageDisplayName = pageDisplayName;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        final String[] uploadValues = uploadValue.split("::");
        if (uploadValues[uploadValues.length - 1].equals("[]")) {
            addBody(new Paragraph(
                    _("This page has no authorization permissions defined. If the page uses REST resources and authorization checking is activated,\n update the page definition to specify the list of resources used.")));
        } else {
            addBody(new Paragraph(_("The page allows users with relevant profiles to access the following permissions:")));
        }
        addPermissionsForm(uploadValues);
    }

    private void addPermissionsForm(final String[] uploadValues) {
        final HashSet<String> invalidResources = new HashSet<String>();
        final String[] customPagePermissions = getCustomPagePermissionsFromUploadValue(uploadValues);
        for (final String customPagePermission : customPagePermissions) {
            final String trimmedCustomPagePermission = customPagePermission.trim();
            if (trimmedCustomPagePermission.length() > 0) {
                if (RegExp.compile(RESOURCES_REGEXP).test(trimmedCustomPagePermission)) {
                    invalidResources.add(trimmedCustomPagePermission);
                } else {
                    addBody(new Paragraph(" - " + customPagePermission));
                }
            }
        }
        handleInvalidResources(invalidResources);
        final Form form = new Form(PAGE_UPLOAD_FORM_JS_ID);
        form.addEntry(new StaticText(ERROR_JS_ID, "", ""));
        form.addHiddenEntry(PAGE_ZIP, uploadValues[0] + "::" + uploadValues[1]);
        if (pageId != null) {
            addBody(addButtonsForUpdate(form));
        } else {
            addBody(addButtonsForAdd(form));
        }
        notActionButton(BACK_BUTTON_JS_ID, form.getElement());
    }

    protected void handleInvalidResources(final HashSet<String> invalidResources) {
        if (!invalidResources.isEmpty()) {
            final Callout callout = new CalloutDanger(_("Resources not recognized:"));
            for (final String invalidResource : invalidResources) {
                callout.append(new Paragraph(" - " + invalidResource.substring(1, invalidResource.length() - 1)));
            }
            addBody(callout);
        }
    }

    private Form addButtonsForUpdate(final Form form) {
        form.addButton(BACK_BUTTON_JS_ID, "< ".concat(_("Back")), "", new ActionShowPopup(new EditCustomPage(pageId, pageDisplayName)));
        form.addButton(AddCustomPage.INSTALL_BUTTON_JS_ID, _("Confirm"), _("Update the page"), new UpdatePageAction(pageId, form)).addCancelButton();
        return form;
    }

    private Form addButtonsForAdd(final Form form) {
        form.addButton(BACK_BUTTON_JS_ID, "< ".concat(_("Back")), "", new ActionShowPopup(new AddCustomPage()));
        form.addButton(INSTALL_BUTTON_JS_ID, _("Confirm"), _("Confirm the page adding"),
                new SendFormAction(PageDefinition.get().getAPIUrl(), new PageInstallCallback(form))).addCancelButton();
        return form;
    }

    private String[] getCustomPagePermissionsFromUploadValue(final String[] uploadValueSplitted) {
        final int permissionsIndex = uploadValueSplitted.length - 1;
        final String permissionsString = uploadValueSplitted[permissionsIndex];
        return permissionsString.substring(1, permissionsString.length() - 1).split(",");
    }

    public native String notActionButton(JsId jsid, Element form)
    /*-{
         //$wnd.document.getElementById(jsid).disabled=true;
         $wnd.$("." + jsid, form).removeClass('btn-action');
    }-*/;

    @Override
    public void defineTitle() {
        if (pageId != null) {
            this.setTitle(_("Confirm update"));
        } else {
            this.setTitle(_("Confirm the new page"));
        }
    }

}
