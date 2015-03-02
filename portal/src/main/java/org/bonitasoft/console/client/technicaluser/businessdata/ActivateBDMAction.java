package org.bonitasoft.console.client.technicaluser.businessdata;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.model.tenant.BusinessDataModelItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;

/**
 * Show popup and install bdm if ok clicked 
 */
class ActivateBDMAction extends FormAction {
    
    @Override
    public void execute() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(BusinessDataModelItem.ATTRIBUTE_FILE_UPLOAD_PATH, form.getEntryValue(BusinessDataModelItem.ATTRIBUTE_FILE_UPLOAD_PATH));
        ViewController.showPopup(BDMImportWarningPopUp.TOKEN, params);
    }
    
}