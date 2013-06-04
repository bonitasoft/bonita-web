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

import java.util.Map;

import org.bonitasoft.console.client.admin.category.action.DeleteCategoryAction;
import org.bonitasoft.console.client.model.bpm.process.CategoryDefinition;
import org.bonitasoft.console.client.model.bpm.process.CategoryItem;
import org.bonitasoft.console.common.client.FilterKey;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Haojie Yuan
 * 
 */
public class DeleteCategoryPage extends Page {

    public static final String TOKEN = "DeleteCategory";

    @Override
    public void defineTitle() {
        this.setTitle("");
        final String categoryId = this.getParameter(CategoryItem.ATTRIBUTE_ID);
        final ItemDefinition itemDef = Definitions.get(CategoryDefinition.TOKEN);
        itemDef.getAPICaller().get(categoryId, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final CategoryItem categoryItem = (CategoryItem) JSonItemReader.parseItem(response, itemDef);
                DeleteCategoryPage.this.setTitle(_("Delete %category_name%",
                        new Arg("category_name", categoryItem.getAttributeValue(CategoryItem.ATTRIBUTE_NAME))));
                super.onSuccess(httpStatusCode, response, headers);
            }

        });
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {

        final String categoryId = this.getParameter(CategoryItem.ATTRIBUTE_ID);
        final String viewType = this.getParameter(FilterKey.VIEW.name());
        final ItemDefinition itemDef = Definitions.get(CategoryDefinition.TOKEN);

        addBody(new Paragraph(new Text(_("Are you sure you want to delete this category ?"))), new Paragraph(new Text(_("This action will be irreversible."))));

        addBody(new Form(new JsId(TOKEN))
                .addCheckboxEntry(new JsId("isDeleteCategory"), _("Delete the apps in this category"), _("Delete the apps in this category"), "true")
                .addButton(new JsId("deleteCategoryBtn"), _("Save"), _("Delete this category"),
                        new DeleteCategoryAction(categoryId, itemDef, viewType)).addCancelButton());

    }

}
