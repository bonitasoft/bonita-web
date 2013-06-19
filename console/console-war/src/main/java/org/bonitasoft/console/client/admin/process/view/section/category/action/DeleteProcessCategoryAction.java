/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.process.view.section.category.action;

import org.bonitasoft.console.client.admin.process.view.section.category.RemoveCategoryPopUp;
import org.bonitasoft.web.rest.model.bpm.process.CategoryItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.ItemAction;

/**
 * @author Colin PUY
 */
public class DeleteProcessCategoryAction extends ItemAction {

    private CategoryItem categoryItem;
    private ProcessItem processItem;

    public DeleteProcessCategoryAction(ProcessItem processItem, CategoryItem categoryItem) {
        super(Definitions.get(ProcessCategoryDefinition.TOKEN));
        this.processItem = processItem;
        this.categoryItem = categoryItem;
    }

    @Override
    public void execute() {
        ViewController.showPopup(new RemoveCategoryPopUp(processItem, categoryItem));
    }
}
