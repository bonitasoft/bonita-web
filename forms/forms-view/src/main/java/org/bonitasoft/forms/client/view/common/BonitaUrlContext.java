package org.bonitasoft.forms.client.view.common;

import java.util.Map;

public class BonitaUrlContext {

    private Map<String, Object> urlContext;
    private URLUtils urlUtils;

    private BonitaUrlContext(URLUtils urlUtils) {
        this.urlUtils = urlUtils;
        urlContext = urlUtils.getHashParameters();
    }

    public static BonitaUrlContext get() {
        return new BonitaUrlContext(URLUtilsFactory.getInstance());
    }
    
    public Map<String, Object> getHashParameters() {
        return urlContext;
    }
    
    public String getFormId() {
        return urlUtils.getFormID();
    }
    
    public String getApplicationMode() {
        return (String) urlContext.get(URLUtils.VIEW_MODE_PARAM);
    }
}
