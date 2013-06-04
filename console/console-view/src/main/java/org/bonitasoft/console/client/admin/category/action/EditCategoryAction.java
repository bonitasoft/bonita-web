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
package org.bonitasoft.console.client.admin.category.action;

import java.util.Map;

import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.model.bpm.process.CategoryItem;
import org.bonitasoft.console.common.client.ViewType;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.action.form.ItemFormAction;

/**
 * @author Haojie Yuan
 * 
 */
public class EditCategoryAction extends ItemFormAction<CategoryItem> {

    private ItemDefinition categoryDefinition = null;

    private String categoryId = null;

    private String viewType = null;

    /**
     * Default Constructor.
     * 
     * @param itemDefinition
     */
    public EditCategoryAction(final String categoryId, final ItemDefinition itemDefinition, final String viewType) {
        super(itemDefinition);
        this.categoryDefinition = itemDefinition;
        this.categoryId = categoryId;
        this.viewType = viewType;
    }

    @Override
    public void execute() {

        this.categoryDefinition.getAPICaller().update(this.categoryId, this.form, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                ViewController.closePopup();
                if (EditCategoryAction.this.viewType.equals(ViewType.ADMINISTRATOR.name())) {
                    ViewController.showView(ProcessListingAdminPage.TOKEN);
                } else if (EditCategoryAction.this.viewType.equals(ViewType.PROCESS_OWNER.name())) {
                    ViewController.showView("ListProcessOwnerProcessPage.TOKEN");
                }
                ViewController.getInstance().historyBack();
            }

        });
    }

}
