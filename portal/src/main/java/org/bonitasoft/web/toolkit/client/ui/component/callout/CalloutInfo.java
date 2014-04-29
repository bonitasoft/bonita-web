package org.bonitasoft.web.toolkit.client.ui.component.callout;

import org.bonitasoft.web.toolkit.client.ui.component.core.Component;


public class CalloutInfo extends Callout {

    public CalloutInfo(String title, Component... body) {
        super(title, body);
        addClass("callout-info");
    }

}
