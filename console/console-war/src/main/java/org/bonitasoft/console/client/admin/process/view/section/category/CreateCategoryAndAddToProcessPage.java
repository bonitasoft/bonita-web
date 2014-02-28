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
package org.bonitasoft.console.client.admin.process.view.section.category;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.section.category.action.CreateCategoryAction;
import org.bonitasoft.web.rest.model.bpm.process.ActorMemberDefinition;
import org.bonitasoft.web.rest.model.bpm.process.CategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.CategoryItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Rohart Bastien
 * 
 */
public class CreateCategoryAndAddToProcessPage extends AddProcessCategoryPage {

    private static final String PARAMETER_PROCESS_ID = "processId";

    private static final String PARAMETER_PROCESS_NAME = "processName";

    public static final String TOKEN = "CreateCategoryAndAddToProcess";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public CreateCategoryAndAddToProcessPage() {
    }

    public CreateCategoryAndAddToProcessPage(final APIID processId, final String processName) {
        addParameter(PARAMETER_PROCESS_ID, processId.toString());
        addParameter(PARAMETER_PROCESS_NAME, processName);
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Create a new category"));
    }

    @Override
    public void buildView() {
        addBody(newAddCategoryForm());
    }

    protected Form newAddCategoryForm() {

        final Form form = new Form(new JsId("addCategory"));

        form.addTextEntry(new JsId(CategoryItem.ATTRIBUTE_NAME), _("Name"), _("Enter a category name"))
                .addValidator(new JsId(CategoryItem.ATTRIBUTE_NAME), new MandatoryValidator())
                .addTextareaEntry(new JsId(CategoryItem.ATTRIBUTE_DESCRIPTION), _("Description"), _("Enter the description for this category"))
                .addButton(new JsId("addCategoryBtn"), _("create"), _("Add a new category"),
                        createNewCategoryAction(form))
                .addCancelButton();

        return form;
    }

    private CreateCategoryAction createNewCategoryAction(final Form addCategoryForm) {
        final String processId = this.getParameter(PARAMETER_PROCESS_ID);
        final Map<String, String> parameters = Collections.singletonMap(PARAMETER_PROCESS_ID, processId);
        return new CreateCategoryAction(addCategoryForm, parameters, createCategoryCreationCallback(processId));
    }

    /**
     * @return
     */
    private APICallback createCategoryCreationCallback(final String processId) {
        return new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                new APICaller(ProcessCategoryDefinition.get())
                        .add(createAssociationItem(processId, parseCategoryItem(response)),
                                new AddCategoryToProcessCallback());
            }

            private IItem createAssociationItem(final String processId, final CategoryItem categoryItem) {
                final ProcessCategoryItem item = new ProcessCategoryItem();
                item.setProcessId(processId);
                item.setCategoryId(categoryItem.getId());
                return item;
            }

            private CategoryItem parseCategoryItem(final String response) {
                return (CategoryItem) JSonItemReader.parseItem(response, CategoryDefinition.get());
            }
        };
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    /**
     * Redirect to right page after adding category to process
     */
    private class AddCategoryToProcessCallback extends APICallback {

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            ViewController.closePopup();
            ViewController.refreshCurrentPage();
        }
    }
}
