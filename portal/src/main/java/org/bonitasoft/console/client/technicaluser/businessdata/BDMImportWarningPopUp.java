/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.technicaluser.businessdata;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Map;

import org.bonitasoft.web.rest.model.tenant.BusinessDataModelDefinition;
import org.bonitasoft.web.rest.model.tenant.BusinessDataModelItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.callback.HttpCallback;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.form.SendFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.page.MessagePage;


public class BDMImportWarningPopUp extends Page {

    public static final String TOKEN = "bdmimportwarningpopup";
    
    @Override
    public void defineTitle() {
       setTitle(_("Warning"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        addBody(warningText());
        addBody(installForm());
    }

    private ContainerStyled<Component> warningText() {
        ContainerStyled<Component> container = new ContainerStyled<Component>();
        container.append(new Paragraph(_("The Business Data Model will now be installed.\nPlease note that existing business "
                + "database tables will be modified definitively. This action cannot be reversed. It is highly recommended "
                + "to backup the database before proceeding.")));
        container.append(new Paragraph(_("Click on Install to complete the deployment or Cancel to stop the deployment.")));
        return container;
    }

    private Form installForm() {
        Form form = new Form();
        form.addHiddenEntry(BusinessDataModelItem.ATTRIBUTE_FILE_UPLOAD_PATH, getParameter(BusinessDataModelItem.ATTRIBUTE_FILE_UPLOAD_PATH));
        form.addButton(_("Install"), _("Install the Business Data Model"), new InstallBDMAction());
        form.addCancelButton();
        return form;
    }

    private class InstallBDMAction extends SendFormAction {

        public InstallBDMAction() {
            super(BusinessDataModelDefinition.get().getAPIUrl(), new HttpCallback() {
               
                @Override
                public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                    ViewController.showPopup(new MessagePage(MessagePage.TYPE.SUCCESS, _("The Business Data Model was successfully installed.")));
                }
            });
        }
        
       
    }
}
