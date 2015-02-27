package org.bonitasoft.console.client.menu.view.technicaluser;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuLink;

public class BPMServicesMenuItem extends MenuLink {

	public BPMServicesMenuItem() {
		
        super(new JsId("tenantMaintenance"), _("BPM services"), _("Configure BPM services for maintenance"),
                "tenantMaintenance");
//      TO DO: super(new JsId(TenantMaintenancePage.TOKEN), _("BPM services"), _("Configure BPM services for maintenance"),
//        TenantMaintenancePage.TOKEN);
        
//        addMenuItem(new MenuLink(new JsId(ProfileListingPage.TOKEN), _("Profiles"), _("Show all profiles of portal"), ProfileListingPage.TOKEN));
    }

}
