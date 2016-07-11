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

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ItemAction;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.form.ItemForm;

/**
 * @author Julien Mege
 */
public class EditItemPage extends ItemPage {

    public final static String TOKEN = "edititempage";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void defineTitle() {
        this.setTitle(TOKEN + " title");
    }

    @Override
    public void buildView() {
        Section editItemSection = new Section(
                new JsId(this.getParameter("id") != null ? "edititem" : "additem"),
                this.getParameter("id") != null ? _("Edit") : _("Add"),
                new ItemForm<Item>(
                        new JsId("edititem"),
                        Definitions.get(this.getParameter(ItemAction.PARAM_ITEM_DEFINITION_NAME)),
                        this.getParameter("id")
                ));
        editItemSection.setId(CssId.SECTION_EDIT_ITEM);
        addBody(editItemSection);
    }
}
