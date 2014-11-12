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

import org.bonitasoft.web.rest.model.bpm.process.CategoryItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.NameAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.form.DeleteItemFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.view.DeleteItemPage;

/**
 * @author Colin PUY
 * 
 */
public class RemoveCategoryPopUp extends DeleteItemPage {

    private ProcessItem process;
    private CategoryItem category;

    public RemoveCategoryPopUp(ProcessItem process, CategoryItem category) {
        this.process = process;
        this.category = category;
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Confirm removing ?"));
    }
    
    @Override
    public void buildView() {
        addBody(new Text(makeMessage()));
        addBody(deletionForm());
    }

    private Form deletionForm() {
        Form form = new Form(new JsId("deleteForm"));
        form.addButton(new JsId("deleteActionForm"), _("Remove"), _("Confirm removing"),
                        new DeleteItemFormAction<Item>(ProcessCategoryDefinition.get()));
        form.addCancelButton();
        form.addHiddenEntry(PARAM_ID, makeProcessCategorieApiId().toString());
        return form;
    }

    private String makeMessage() {
        return _("Are you sure you want to remove category '%categoryName%' from process '%appName%' ?", 
                new Arg("categoryName", new NameAttributeReader().read(category)), 
                new Arg("appName", new NameAttributeReader().read(process)));
    }

    private APIID makeProcessCategorieApiId() {
        return APIID.makeAPIID(process.getId().toLong(), category.getId().toLong());
    }
}
