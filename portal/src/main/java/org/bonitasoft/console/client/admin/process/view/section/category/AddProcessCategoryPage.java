/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.process.view.section.category;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.web.rest.model.bpm.process.CategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.CategoryItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Strong;
import org.bonitasoft.web.toolkit.client.ui.component.form.ItemForm;

/**
 * @author Nicolas Tith
 */
public class AddProcessCategoryPage extends Page {

    private static final String PARAMETER_PROCESS_ID = "processId";

    private static final String PARAMETER_PROCESS_NAME = "processName";

    public static final String TOKEN = "addProcessCategoryPage";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public AddProcessCategoryPage() {
    }

    public AddProcessCategoryPage(APIID processId, String processName) {
        addParameter(PARAMETER_PROCESS_ID, processId.toString());
        addParameter(PARAMETER_PROCESS_NAME, processName);
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Add a new category to %%"), new Strong(this.getParameter(PARAMETER_PROCESS_NAME)));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    /**
     * build the subtask form
     * 
     */
    @Override
    public void buildView() {

        final ItemForm<ProcessCategoryItem> form = createForm()
                // hidden entries
                .addHiddenEntry(ProcessCategoryItem.ATTRIBUTE_PROCESS_ID, this.getParameter(PARAMETER_PROCESS_ID))
                // displayed entries
                .addEntryAsAutoComplete(
                        ProcessCategoryItem.ATTRIBUTE_CATEGORY_ID,
                        _("Category"),
                        _("Select the category which the process will be linked to"),
                        Definitions.get(CategoryDefinition.TOKEN),
                        CategoryItem.ATTRIBUTE_NAME,
                        CategoryItem.ATTRIBUTE_ID);

        form.getEntry(new JsId(ProcessCategoryItem.ATTRIBUTE_CATEGORY_ID))
                .addValidator(new MandatoryValidator(_("This category was not found or this field is mandatory")));
        addBody(form);
    }

    private ItemForm<ProcessCategoryItem> createForm() {
        return new ItemForm<ProcessCategoryItem>(new JsId("addProcessCategoryForm"),
                Definitions.get(ProcessCategoryDefinition.TOKEN));
    }

}
