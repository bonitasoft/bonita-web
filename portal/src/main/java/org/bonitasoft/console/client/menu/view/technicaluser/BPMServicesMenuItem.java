package org.bonitasoft.console.client.menu.view.technicaluser;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.common.view.CustomPageWithFrame;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuLink;

public class BPMServicesMenuItem extends MenuLink {

	public BPMServicesMenuItem() {
		
        super(new JsId(CustomPageWithFrame.TENANT_STATUS), _("BPM services"), _("Configure BPM services for maintenance"),
                CustomPageWithFrame.TENANT_STATUS);
    }

}
