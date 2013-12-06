/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.admin.profile.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.admin.profile.action.DeleteProfileMemberAction;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Yongtao Guo
 */
public class DeleteProfileMemberPage extends Page {

    public final static String TOKEN = "deleteProfileMemberpage";    
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProfileListingPage.TOKEN);
    }

    public final static String PARAMETER_PROFILE_ID = "profileId";

    public final static String PARAMETER_PROFILE_MEMBER_ID = "profileMemberId";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    public DeleteProfileMemberPage() {
    }

    public DeleteProfileMemberPage(APIID profileId, APIID profileMemberId) {
        addParameter(PARAMETER_PROFILE_ID, profileId.toString());
        addParameter(PARAMETER_PROFILE_MEMBER_ID, profileMemberId.toString());
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Confirm deletion ?"));
    }

    @Override
    public void buildView() {
        final String profileId = this.getParameter(PARAMETER_PROFILE_ID);
        final String profileMemberId = this.getParameter(PARAMETER_PROFILE_MEMBER_ID);
        addBody(createDeleteForm(new DeleteProfileMemberAction(profileId, profileMemberId)));
    }

    private Form createDeleteForm(final Action action) {
        return new Form(new JsId("removeForm"))
                .addButton(new JsId("removeActionForm"), _("Remove"), _("Remove"), action)
                .addCancelButton();
    }

}
