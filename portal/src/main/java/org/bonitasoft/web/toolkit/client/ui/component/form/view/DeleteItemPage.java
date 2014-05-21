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
package org.bonitasoft.web.toolkit.client.ui.component.form.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ItemAction;
import org.bonitasoft.web.toolkit.client.ui.action.form.DeleteItemFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Julien Mege
 */
public class DeleteItemPage extends ItemPage {

    public final static String TOKEN = "deleteitempage";

    public final static String PARAM_ID = "id";
    public final static String PARAM_RESOURCE_NAME = "resourceName";
    public final static String PARAM_MESSAGE = "message";
    public final static String PARAM_REDIRECT_TOKEN = "redirecttoken";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_CONFIRMBUTTON_LABEL = "confirmButtonLabel";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void defineTitle() {
        if (hasParameter(PARAM_TITLE)) {
            setTitle(getParameter(PARAM_TITLE));
        } else {
            setTitle(_("Confirm delete ?"));
        }
    }

    @Override
    public void buildView() {
        if (this.hasParameter(PARAM_RESOURCE_NAME)) {
            this.setTitle(_("Delete %resource_name%", new Arg("resource_name", this.getParameter("resourceName"))));
        }

        if (this.hasParameter(PARAM_MESSAGE)) {
            addBody(new Text(this.getParameter(PARAM_MESSAGE)));
        }

        final Form form = new Form(new JsId("deleteForm"))
                .addButton(new JsId("deleteActionForm"), getConfirmButtonLabel(), _("Confirm deletion"),
                        new DeleteItemFormAction<Item>(
                                Definitions.get(this.getParameter(ItemAction.PARAM_ITEM_DEFINITION_NAME))
                        )
                )
                .addCancelButton();

        if (this.getParameter(PARAM_ID) != null) {
            form.addHiddenEntry(PARAM_ID, this.getParameter(PARAM_ID));
        } else {
            for (final String id : this.getArrayParameter(PARAM_ID)) {
                form.addHiddenEntry(PARAM_ID, id);
            }
        }
        if (this.getParameter(PARAM_REDIRECT_TOKEN) != null) {
            form.addHiddenEntry(PARAM_REDIRECT_TOKEN, this.getParameter(PARAM_REDIRECT_TOKEN));
        }

        addBody(form);

    }

    private String getConfirmButtonLabel() {
        if (hasParameter(PARAM_CONFIRMBUTTON_LABEL)) {
            return getParameter(PARAM_CONFIRMBUTTON_LABEL);
        } else {
            return _("Delete");
        }
    }

}
