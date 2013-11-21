/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.profile.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.RequestQueue;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Image;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormFiller;
import org.bonitasoft.web.toolkit.client.ui.component.form.ItemForm;

import com.google.gwt.core.client.GWT;

/**
 * @author Zhiheng Yang
 * 
 */
public class EditProfilePage extends Page {

    public static final String TOKEN = "EditProfile";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProfileListingPage.TOKEN);
    }

    private static final String ICON_URL = "themeResource?theme=default&location=images/profile.png";

    @Override
    public void defineTitle() {
        this.setTitle(_("%% Edit profile"), new Image(GWT.getModuleBaseURL() + ICON_URL, 12, 12, ""));
    }

    @Override
    public String defineJsId() {
        return "editProfilePage";

    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        final String itemId = this.getParameter("id");
        final JsId editformJsId = new JsId("editProfileForm");
        final ItemDefinition profileDefinition = Definitions.get(ProfileDefinition.TOKEN);

        final ItemForm<ProfileItem> editForm = new ItemForm<ProfileItem>(editformJsId, profileDefinition, itemId);
        editForm.addEntry(ProfileItem.ATTRIBUTE_NAME, _("Name"), "");
        editForm.addEntry(ProfileItem.ATTRIBUTE_DESCRIPTION, _("Description"), "");
        editForm.setFiller(new FormFiller() {

            @Override
            protected void getData(final APICallback callback) {
                final RequestQueue queue = new RequestQueue(false, new Action() {

                    @Override
                    public void execute() {
                        editForm.stopLoading();
                    }
                });

                if (itemId != null) {
                    queue.addRequest(APIRequest.get(itemId, profileDefinition, new APICallback() {

                        @Override
                        public void onSuccess(final int httpStatusCode, final String response,
                                final Map<String, String> headers) {

                            editForm.setJson(response);
                            editForm.stopLoading();
                        }
                    }));
                }
                queue.run();

            }
        });
        addBody(editForm);
    }

}
