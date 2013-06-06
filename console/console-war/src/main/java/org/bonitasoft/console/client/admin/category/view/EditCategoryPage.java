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
package org.bonitasoft.console.client.admin.category.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.category.action.EditCategoryAction;
import org.bonitasoft.console.client.model.bpm.process.CategoryDefinition;
import org.bonitasoft.console.client.model.bpm.process.CategoryItem;
import org.bonitasoft.console.common.client.FilterKey;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormFiller;

/**
 * @author Haojie Yuan
 * 
 */
public class EditCategoryPage extends Page {

    public static final String TOKEN = "EditCategory";

    @Override
    public void defineTitle() {
        this.setTitle(_("Edit category"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        final ItemDefinition itemDef = Definitions.get(CategoryDefinition.TOKEN);
        final String categoryId = this.getParameter("id");
        final String viewType = this.getParameter(FilterKey.VIEW.name());

        addBody(new Form(new JsId("itemForm"))
                .addItemAttributeEntry(itemDef.getAttribute(CategoryItem.ATTRIBUTE_NAME), _("Name"), _("Enter a category name"))
                .addItemAttributeEntry(itemDef.getAttribute(CategoryItem.ATTRIBUTE_DESCRIPTION), _("Description"), _("Enter the description for this category"))
                .addButton(new JsId("save"), _("Save"), _("Save"), new EditCategoryAction(categoryId, itemDef, viewType))
                .addCancelButton().addFiller(new FormFiller() {

                    @Override
                    protected void getData(final APICallback callback) {
                        itemDef.getAPICaller().get(categoryId, callback);
                    }

                }));

    }

}
