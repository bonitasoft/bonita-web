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

import java.util.Arrays;

import org.bonitasoft.console.client.admin.page.view.CustomPagePermissionsValidationPopupPage;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FileUpload;


/**
 * @author Fabio Lombardi
 *
 */
public class OpenValidationPopupAction extends Action {

    private final FileUpload pageUploader;

    private final APIID pageId;

    private final String pageDisplayName;

    public OpenValidationPopupAction(final FileUpload pageUploader, final APIID pageId, final String pageDisplayName) {
        this.pageUploader = pageUploader;
        this.pageId = pageId;
        this.pageDisplayName = pageDisplayName;
    }

    public OpenValidationPopupAction(final FileUpload pageUploader) {
        this.pageUploader = pageUploader;
        pageId = null;
        pageDisplayName = null;
    }

    @Override
    public void execute() {
        final String pageUploaderResponse = pageUploader._getValue();
        final String[] pageUploaderResponseArray = pageUploaderResponse.split("::");
        final String originalFileName = pageUploaderResponseArray[pageUploaderResponseArray.length - 2];
        final String authorization = pageUploaderResponseArray[pageUploaderResponseArray.length - 1];
        if (originalFileName.length() > PageInstallCallback.FILENAME_MAX_LENGHT) {
            pageUploader.addError(PageInstallCallback.FILENAME_TOO_LONG);
        } else if ("AlreadyExistsException".equals(authorization)) {
            pageUploader.addError(PageInstallCallback.ALREADY_EXIST_MESSAGE);
        } else if ("InvalidPageTokenException".equals(authorization)) {
            pageUploader.addError(PageInstallCallback.INVALID_TOKEN_MESSAGE);
        } else if (Arrays.asList("InvalidPageZipMissingPropertiesException", "InvalidPageZipMissingIndexException", "InvalidPageZipInconsistentException",
                "InvalidPageZipMissingAPropertyException", "InvalidPageZipContentException").contains(authorization)) {
            pageUploader.addError(PageInstallCallback.INVALID_ARCHIVE_MESSAGE);
        } else {
            ViewController.showPopup(new CustomPagePermissionsValidationPopupPage(pageUploaderResponse, pageId, pageDisplayName));
        }
    }

}
