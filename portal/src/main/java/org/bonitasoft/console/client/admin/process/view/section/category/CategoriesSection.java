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

import org.bonitasoft.console.client.admin.process.view.section.category.action.DeleteProcessCategoryAction;
import org.bonitasoft.web.rest.model.bpm.process.CategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.CategoryItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableActionSet;

/**
 * @author Colin PUY
 * 
 */
public class CategoriesSection extends Section {

    public CategoriesSection(final ProcessItem item) {
        super(new JsId("Categories"), _("Categories"));
        setId(CssId.MD_SECTION_PROCESSCATEGORIES);
        addBody(newCategorieTable(item));
        addBody(newAddCategoryButton(item.getId(), item.getDisplayName()));
        addBody(newCreateCategoryButton(item.getId(), item.getDisplayName()));
    }

    private ItemTable newCategorieTable(final ProcessItem processItem) {
        final ItemTable categoriesTable = new ItemTable(new JsId("categories"), Definitions.get(CategoryDefinition.TOKEN))
                .addHiddenFilter(ProcessItem.ATTRIBUTE_ID, processItem.getId())
                .setShowSearch(false)
                // displayed columns
                .addColumn(CategoryItem.ATTRIBUTE_NAME, _("Name")
                ).setActions(new ItemTableActionSet<CategoryItem>() {

                    @Override
                    protected void defineActions(final CategoryItem categoryItem) {
                        this.addAction(new ItemTableAction(_("Remove"), _("Remove this category"),
                                new DeleteProcessCategoryAction(processItem, categoryItem)));
                    }
                }

                );
        return categoriesTable;
    }

    private Button newAddCategoryButton(final APIID processId, final String processName) {
        return new ButtonAction("btn-addCategory", _("Add"), _("Add a category"),
                new CheckValidSessionBeforeAction(new ActionShowPopup(new AddProcessCategoryPage(processId, processName))));
    }

    private Button newCreateCategoryButton(final APIID processId, final String processName) {
        return new ButtonAction("btn-createCategory", _("Create"), _("Create a new category"),
                new CheckValidSessionBeforeAction(new ActionShowPopup(new CreateCategoryAndAddToProcessPage(processId, processName))));
    }
}
