/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.admin.page.action;

import org.bonitasoft.web.rest.model.portal.page.PageDefinition;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FileUpload;

public class UpdatePageAction extends Action {

    protected APIID pageId;

    protected Form form;

    protected FileUpload pageUploader;

    public UpdatePageAction(final APIID pageId, final Form form) {
        this.form = form;
        this.pageId = pageId;
    }

    @Override
    public void execute() {
        final APICaller<PageItem> apiCaller = new APICaller<PageItem>(PageDefinition.get());
        apiCaller.update(pageId, form, new PageInstallCallback(form));
    }
}
