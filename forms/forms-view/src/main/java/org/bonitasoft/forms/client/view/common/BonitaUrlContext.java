package org.bonitasoft.forms.client.view.common;

import java.util.Map;

public class BonitaUrlContext {

    private Map<String, Object> hashParameters;
    private URLUtils urlUtils;

    private BonitaUrlContext(URLUtils urlUtils, Map<String, Object> hashParameters) {
        this.urlUtils = urlUtils;
        this.hashParameters = hashParameters;
    }

    public static BonitaUrlContext get() {
        URLUtils utils = URLUtilsFactory.getInstance();
        return new BonitaUrlContext(utils, utils.getHashParameters());
    }
    
    public Map<String, Object> getHashParameters() {
        return hashParameters;
    }
    
    public String getFormId() {
        return urlUtils.getFormID();
    }
    
    public String getApplicationMode() {
        return (String) hashParameters.get(URLUtils.VIEW_MODE_PARAM);
    }
    
    public String getThemeName() {
        return (String) hashParameters.get(URLUtils.THEME);
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
