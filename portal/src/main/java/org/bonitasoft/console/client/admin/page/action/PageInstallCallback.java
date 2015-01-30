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
package org.bonitasoft.console.client.admin.page.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Map;

import org.bonitasoft.console.client.admin.page.view.CustomPagePermissionsValidationPopupPage;
import org.bonitasoft.console.client.common.view.CustomPageWithFrame;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

public class PageInstallCallback extends APICallback {

    /**
     *
     */
    public static final int FILENAME_MAX_LENGHT = 50;

    public static final String INVALID_TOKEN_MESSAGE = _("The name for the URL must start with %namePrefix% followed only by alphanumeric characters.",
            new Arg("namePrefix", CustomPageWithFrame.TOKEN));

    public static final String INVALID_ARCHIVE_MESSAGE = _("Zip file structure error. Check that your .zip contains a well-formed page.properties and either the index.html or the Index.groovy file. For details, see the documentation or the example page readme (available in the custom page list).");

    public static final String INVALID_CLASS_MESSAGE = _("Compilation failure. Verify that the Index.groovy class implements the PageController interface.");

    public static final String ALREADY_EXIST_MESSAGE = _("A page with this name already exists.");

    public static final String FILENAME_TOO_LONG = _("Filename too long. The zip filename must be no longer than %number% characters", new Arg("number",
            FILENAME_MAX_LENGHT));

    protected Form form;

    public PageInstallCallback(final Form form) {
        super();
        this.form = form;
    }

    @Override
    public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
        ViewController.getInstance().historyBack();
        refresh();
    }

    public static native void refresh()/*-{
        $wnd.location.reload(true);
    }-*/;

    @Override
    public void onError(final String message, final Integer errorCode) {
        final ServerException ex = parseException(message, errorCode);
        form.resetErrors();
        final String originalException = ex.getOriginalClassName();
        if (originalException != null && originalException.contains("AlreadyExistsException")) {
            form.addError(CustomPagePermissionsValidationPopupPage.ERROR_JS_ID, ALREADY_EXIST_MESSAGE);
        } else if (originalException != null && originalException.contains("InvalidPageTokenException")) {
            form.addError(CustomPagePermissionsValidationPopupPage.ERROR_JS_ID, INVALID_TOKEN_MESSAGE);
        } else if (originalException != null && originalException.contains("InvalidPageZipContentException")) {
            form.addError(CustomPagePermissionsValidationPopupPage.ERROR_JS_ID, INVALID_ARCHIVE_MESSAGE);
        } else if (originalException != null
                && (originalException.contains("CompilationFailedException") || originalException.contains("MultipleCompilationErrorsException"))) {
            form.addError(CustomPagePermissionsValidationPopupPage.ERROR_JS_ID, INVALID_CLASS_MESSAGE);
        } else {
            super.onError(message, errorCode);
        }
    }
}
