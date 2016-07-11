/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.admin.process.view.section.entitymapping;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.common.view.SelectRoleAndDoPageOnItem;
import org.bonitasoft.web.rest.model.bpm.process.ActorDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ActorItem;
import org.bonitasoft.web.rest.model.bpm.process.ActorMemberDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ActorMemberItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;

/**
 * popup
 *
 * @author Julien Mege
 * 
 */
public class SelectRoleForActorPage extends SelectRoleAndDoPageOnItem<ActorItem> {

    public static final String TOKEN = "selectRoleforactor";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public SelectRoleForActorPage(final APIID itemId) {
        super(itemId, ActorDefinition.get());
    }

    public SelectRoleForActorPage() {
        super(ActorDefinition.get());
    }

    public SelectRoleForActorPage(final String itemId) {
        super(itemId, ActorDefinition.get());
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected FormAction defineSubmitButtonAction() {
        return new FormAction() {

            @Override
            public void execute() {

                final ActorMemberItem item = new ActorMemberItem();
                item.setAttribute(ActorMemberItem.ATTRIBUTE_ROLE_ID, this.getParameter(ROLE_ID));
                item.setAttribute(ActorMemberItem.ATTRIBUTE_ACTOR_ID, this.getParameter("id"));

                new APICaller(ActorMemberDefinition.get()).add(item, new APICallback() {

                    @Override
                    public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                        ViewController.showPopup(ListProcessActorRolePage.TOKEN, MapUtil.asMap(
                                new Arg("id", SelectRoleForActorPage.this.getParameter("id"))));
                    }

                });
            }
        };
    }

    @Override
    protected String defineSubmitButtonLabel(final ActorItem item) {
        return _("Add");
    }

    @Override
    protected String defineSubmitButtonTooltip(final ActorItem item) {
        return _("Add this role to %actor_name%", new Arg("actor_name", item.getAttributeValue(ActorItem.ATTRIBUTE_DISPLAY_NAME)));
    }

    @Override
    protected void defineTitle(final ActorItem item) {
        setTitle(_("Add a role to %actor_name%", new Arg("actor_name", item.getAttributeValue(ActorItem.ATTRIBUTE_DISPLAY_NAME))));

    }

}
