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
package org.bonitasoft.console.client.admin.theme.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

import com.google.gwt.core.client.GWT;

/**
 * @author Gai Cuisha
 * 
 */
public class UploadThemePage extends Page {

    public static final String TOKEN = "themeupload";

    @Override
    public void defineTitle() {
        this.setTitle(_("Upload look'n'feel"));
    }

    @Override
    public String defineJsId() {
        return "uploadSection";
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        final Form uploadForm = new Form(new JsId("uploadForm"));
        uploadForm.addFileEntry(new JsId("fileupload"), _("Theme uploading"), _("Select a file"), GWT.getModuleBaseURL() + "themeUpload");
        uploadForm.addButton(new JsId("ok"), _("OK"), "", new RedirectionAction(ListThemePage.TOKEN));
        addBody(uploadForm);
    }
}
