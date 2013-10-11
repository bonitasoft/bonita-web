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
import org.bonitasoft.console.client.data.item.attribute.reader.UserAttributeReader;
import org.bonitasoft.web.rest.model.bpm.process.ActorDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ActorItem;
import org.bonitasoft.web.rest.model.bpm.process.ActorMemberDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ActorMemberItem;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.AutoCompleteEntry;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

/**
 * 
 * @author Colin PUY
 */
public class SelectUserForActorPage extends PageOnItem<ActorItem> {

    public static final String TOKEN = "selectUserforactor";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public SelectUserForActorPage(String actorId) {
        super(actorId, ActorDefinition.get());
    }

    public SelectUserForActorPage() {
        // Used by page Factory  -- To be deleted
        super(ActorDefinition.get());
    }

    @Override
    protected void defineTitle(ActorItem actor) {
        setTitle(_("Add a user to %actor_name%", new Arg("actor_name", actor.getDisplayName())));
    }

    @Override
    protected void buildView(ActorItem item) {
        addBody(addUserForm(item));
    }

    private Form addUserForm(ActorItem actor) {
        Form form = new Form();
        form.addEntry(selectUserAutoComplete());
        form.addHiddenEntry(ActorMemberItem.ATTRIBUTE_ACTOR_ID, actor.getId().toString());
        form.addButton(_("Add"), _("Add this user to %actor_name%", 
                new Arg("actor_name", actor.getAttributeValue(ActorItem.ATTRIBUTE_DISPLAY_NAME))), 
                new AddUserToActorFormAction());
        form.addCancelButton();
        return form;
    }

    private AutoCompleteEntry selectUserAutoComplete() {
        AutoCompleteEntry autoComplete = new AutoCompleteEntry(new JsId(ActorMemberItem.ATTRIBUTE_USER_ID), 
                _("Select a user"), _("Select a user to be added to actor"), 
                UserDefinition.get(), new UserAttributeReader(), UserItem.ATTRIBUTE_ID, null);
        autoComplete.addFilter(UserItem.ATTRIBUTE_ENABLED, "true");
        autoComplete.addValidator(new MandatoryValidator());
        return autoComplete;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
    
    /** */
    private class AddUserToActorFormAction extends FormAction {

        @Override
        public void execute() {
            final ActorMemberItem item = new ActorMemberItem();
            item.setAttribute(ActorMemberItem.ATTRIBUTE_USER_ID, getParameter(ActorMemberItem.ATTRIBUTE_USER_ID));
            item.setAttribute(ActorMemberItem.ATTRIBUTE_ACTOR_ID, getParameter(ActorMemberItem.ATTRIBUTE_ACTOR_ID));

            ActorMemberDefinition.get().getAPICaller().add(item, new AddUserToActorAPICallback());
        }
        
    }
    
    /** */
    private final class AddUserToActorAPICallback extends APICallback {
        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            ViewController.showPopup(ListProcessActorUserPage.TOKEN, MapUtil.asMap(new Arg("id", getItemId().toString())));
        }
    }

}
