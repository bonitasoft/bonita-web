package org.bonitasoft.forms.client.view.common;

import java.util.HashMap;
import java.util.Map;

public class BonitaUrlContext {

    private Map<String, String> hashParameters;

    private BonitaUrlContext(Map<String, String> hashParameters) {
        this.hashParameters = hashParameters;
    }

    public static BonitaUrlContext get() {
        return new BonitaUrlContext(URLUtilsFactory.getInstance().getHashParameters());
    }
    
    // Need to be converted in Map<String, Object> for FormViewController methods
    // TODO refactor FormViewController
    public Map<String, Object> getHashParameters() {
        HashMap<String, Object> hash = new HashMap<String, Object>();
        for (String key : hashParameters.keySet()) {
            hash.put(key, hashParameters.get(key));
        }
        return hash;
    }
    
    public String getFormId() {
        return hashParameters.get(URLUtils.FORM_ID);
    }
    
    public String getApplicationMode() {
        return hashParameters.get(URLUtils.VIEW_MODE_PARAM);
    }
    
    public String getThemeName() {
        return hashParameters.get(URLUtils.THEME);
    }

    public boolean isFormApplicationMode() {
        return isFormInPortalApplicationMode() || isFormFullPageApplicationMode();
    }
    
    public boolean isFormInPortalApplicationMode() {
        return URLUtils.FORM_ONLY_APPLICATION_MODE.equals(getApplicationMode());
    }
    
    public boolean isFormFullPageApplicationMode() {
        return URLUtils.FULL_FORM_APPLICATION_MODE.equals(getApplicationMode());
    }
    
    public boolean isTodoList() {
        return Boolean.valueOf((String) hashParameters.get(URLUtils.TODOLIST_PARAM));
    }
}
