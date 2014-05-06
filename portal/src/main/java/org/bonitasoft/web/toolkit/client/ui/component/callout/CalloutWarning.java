package org.bonitasoft.web.toolkit.client.ui.component.callout;

import org.bonitasoft.web.toolkit.client.ui.component.core.Component;


public class CalloutWarning extends Callout {

    public CalloutWarning(String title, Component... body) {
        super(title, body);
        addClass("callout-warning");
    }

}
