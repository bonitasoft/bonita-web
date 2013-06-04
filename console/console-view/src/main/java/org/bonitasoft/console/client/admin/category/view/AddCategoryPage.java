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

import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.model.bpm.process.CategoryDefinition;
import org.bonitasoft.console.client.model.bpm.process.CategoryItem;
import org.bonitasoft.console.common.client.FilterKey;
import org.bonitasoft.console.common.client.ViewType;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Zhiheng Yang, Haojie Yuan
 * 
 */
public class AddCategoryPage extends Page {

    public static final String TOKEN = "AddCategory";

    @Override
    public void defineTitle() {
        this.setTitle(_("Add a new app category"));
    }

    @Override
    public String defineJsId() {
        return "addCategorySection";
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {

        final String viewType = this.getParameter(FilterKey.VIEW.name());

        final Form addCategoryForm = new Form(new JsId("addCategory"));
        addCategoryForm.addTextEntry(new JsId(CategoryItem.ATTRIBUTE_NAME), _("Name"), _("Enter a category name"))
                .addTextareaEntry(new JsId(CategoryItem.ATTRIBUTE_DESCRIPTION), _("Description"), _("Enter the description for this category"))
                .addButton(new JsId("addCategoryBtn"), _("Add"), _("Add a new category"), new Action() {

                    @Override
                    public void execute() {
                        Definitions.get(CategoryDefinition.TOKEN).getAPICaller().add(addCategoryForm, new APICallback() {

                            @Override
                            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                                ViewController.closePopup();
                                if (viewType.equals(ViewType.ADMINISTRATOR.name())) {
                                    ViewController.showView(ProcessListingAdminPage.TOKEN);
                                } else if (viewType.equals(ViewType.PROCESS_OWNER.name())) {
                                    ViewController.showView("ListProcessOwnerProcessPage.TOKEN");
                                }
                                ViewController.refreshCurrentPage();
                            }

                        });
                    }
                }).addCancelButton();
        addBody(addCategoryForm);
    }
}
